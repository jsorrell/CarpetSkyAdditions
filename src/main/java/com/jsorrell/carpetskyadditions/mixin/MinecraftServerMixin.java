package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyBlockChunkGenerator;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsConfiguredFeatures;
import com.jsorrell.carpetskyadditions.settings.Fixers;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.settings.SkyBlockDefaults;
import java.io.IOException;
import java.nio.file.Path;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// Lower priority to ensure loadWorld mixin is called before carpet loads the settings
@Mixin(value = MinecraftServer.class, priority = 999)
public abstract class MinecraftServerMixin {
    @Shadow
    @Final
    protected SaveProperties saveProperties;

    @Shadow
    public abstract Path getSavePath(WorldSavePath worldSavePath);

    @Shadow
    @Final
    private CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries;

    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void fixSettingsFile(CallbackInfo ci) {
        Path worldSavePath = this.getSavePath(WorldSavePath.ROOT);
        // Fix existing settings
        try {
            Fixers.fixSettings(worldSavePath);
        } catch (IOException e) {
            SkyAdditionsSettings.LOG.error("Failed to update config", e);
        }

        // Write defaults
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        if (config.autoEnableDefaultSettings
                && this.combinedDynamicRegistries
                                .getCombinedRegistryManager()
                                .get(RegistryKeys.DIMENSION)
                                .getOrThrow(DimensionOptions.OVERWORLD)
                                .chunkGenerator()
                        instanceof SkyBlockChunkGenerator
                && !this.saveProperties.getMainWorldProperties().isInitialized()) {
            try {
                SkyBlockDefaults.writeDefaults(worldSavePath);
            } catch (IOException e) {
                SkyAdditionsSettings.LOG.error("Failed write default configs", e);
            }
        }
    }

    @Inject(
            method = "setupSpawn",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/ServerWorldProperties;setSpawnPos(Lnet/minecraft/util/math/BlockPos;F)V",
                            ordinal = 1,
                            shift = At.Shift.AFTER),
            cancellable = true)
    private static void generateSpawnPlatform(
            ServerWorld world,
            ServerWorldProperties worldProperties,
            boolean bonusChest,
            boolean debugWorld,
            CallbackInfo ci,
            ServerChunkManager serverChunkManager,
            ChunkPos spawnChunk,
            int spawnHeight) {
        ServerChunkManager chunkManager = world.getChunkManager();
        ChunkGenerator chunkGenerator = chunkManager.getChunkGenerator();
        if (!(chunkGenerator instanceof SkyBlockChunkGenerator)) return;
        BlockPos worldSpawn = spawnChunk.getCenterAtY(spawnHeight);

        ChunkRandom random = new ChunkRandom(new CheckedRandom(0));
        random.setCarverSeed(world.getSeed(), spawnChunk.x, spawnChunk.z);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> spawnPlatformFeature = world.getRegistryManager()
                .get(RegistryKeys.CONFIGURED_FEATURE)
                .entryOf(SkyAdditionsConfiguredFeatures.SPAWN_PLATFORM);

        if (!spawnPlatformFeature.value().generate(world, chunkGenerator, random, worldSpawn)) {
            SkyAdditionsSettings.LOG.error("Couldn't generate spawn platform");
        }

        ci.cancel();
    }
}
