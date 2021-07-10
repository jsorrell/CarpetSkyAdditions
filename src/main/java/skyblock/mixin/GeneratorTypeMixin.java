package skyblock.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyblock.SkyBlockUtils;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
@SuppressWarnings("unused")
public abstract class GeneratorTypeMixin {
    @Shadow
    @Final
    protected static List<GeneratorType> VALUES;

    private static final GeneratorType SKYBLOCK = new GeneratorType("skyblock.skyblock") {
        @Override
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return SkyBlockUtils.createOverworldGenerator(biomeRegistry, chunkGeneratorSettingsRegistry, seed);
        }

        @Override
        public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest) {
            Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
            Registry<DimensionType> dimensionTypeRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<ChunkGeneratorSettings> settingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            SimpleRegistry<DimensionOptions> dimensionOptionsRegistry = SkyBlockUtils.getSkyBlockSimpleRegistry(dimensionTypeRegistry, biomeRegistry, settingsRegistry, seed);
            return new GeneratorOptions(seed, generateStructures, bonusChest, GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionTypeRegistry, dimensionOptionsRegistry, SkyBlockUtils.createOverworldGenerator(biomeRegistry, settingsRegistry, seed)));
        }
    };

    @Inject(method = "<clinit>", at=@At("TAIL"))
    @SuppressWarnings("unused")
    private static void addSkyBlockGenerator(CallbackInfo ci) {
        VALUES.add(SKYBLOCK);
    }
}
