package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.gen.SkyBlockChunkGenerator;
import com.jsorrell.skyblock.gen.SkyBlockStructures;
import com.jsorrell.skyblock.settings.Fixers;
import com.jsorrell.skyblock.settings.SkyBlockDefaults;
import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.nio.file.Path;

// Lower priority to ensure loadWorld mixin is called before carpet loads the settings
@Mixin(value = MinecraftServer.class, priority = 999)
public abstract class MinecraftServerMixin {
  @Shadow
  @Final
  protected SaveProperties saveProperties;

  @Shadow
  public abstract Path getSavePath(WorldSavePath worldSavePath);

  @Inject(method = "loadWorld", at = @At("HEAD"))
  private void test(CallbackInfo ci) {
    Path worldSavePath = this.getSavePath(WorldSavePath.ROOT);
    // Fix existing settings
    try {
      Fixers.fixSettings(worldSavePath);
    } catch (IOException e) {
      SkyBlockSettings.LOG.error("[SkyBlock]: failed update config", e);
    }

    // Write defaults
    if (saveProperties.getGeneratorOptions().getDimensions().getOrThrow(DimensionOptions.OVERWORLD).getChunkGenerator() instanceof SkyBlockChunkGenerator && !this.saveProperties.getMainWorldProperties().isInitialized()) {
      try {
        SkyBlockDefaults.writeDefaults(worldSavePath);
      } catch (IOException e) {
        SkyBlockSettings.LOG.error("[SkyBlock]: failed write default configs", e);
      }
      try {
        SkyBlockDefaults.writeDefaults(worldSavePath);
      } catch (IOException e) {
        SkyBlockSettings.LOG.error("[SkyBlock]: failed write default configs", e);
      }
    }
  }

  @Inject(
    method = "setupSpawn",
    locals = LocalCapture.CAPTURE_FAILHARD,
    at =
    @At(
      value = "JUMP",
      opcode = Opcodes.IFEQ,
      ordinal = 1,
      shift = At.Shift.BEFORE))
  private static void generateSpawnPlatform(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
    if (world.getChunkManager().getChunkGenerator() instanceof SkyBlockChunkGenerator) {
      BlockPos worldSpawn = new BlockPos(worldProperties.getSpawnX(), worldProperties.getSpawnY(), worldProperties.getSpawnZ());
      new SkyBlockStructures.SpawnPlatform(worldSpawn).generate(world, world.random);
    }
  }
}
