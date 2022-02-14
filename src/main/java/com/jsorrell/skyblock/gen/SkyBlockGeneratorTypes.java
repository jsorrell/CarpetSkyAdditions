package com.jsorrell.skyblock.gen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class SkyBlockGeneratorTypes {
  public static final GeneratorType SKYBLOCK =
      new GeneratorType("skyblock") {
        @Override
        protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
          Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
          Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
          Registry<ChunkGeneratorSettings> settingsRegistry =
              registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
          return SkyBlockGenerationSettings.createOverworldGenerator(noiseRegistry, biomeRegistry, settingsRegistry, seed);
        }

        @Override
        public GeneratorOptions createDefaultOptions(
            DynamicRegistryManager.Impl registryManager,
            long seed,
            boolean generateStructures,
            boolean bonusChest) {
          Registry<DimensionType> dimensionTypeRegistry =
              registryManager.get(Registry.DIMENSION_TYPE_KEY);
          Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
          Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
          Registry<ChunkGeneratorSettings> settingsRegistry =
              registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);

          SimpleRegistry<DimensionOptions> dimensionOptionsRegistry =
              SkyBlockGenerationSettings.getSkyBlockDimensionOptions(
                  dimensionTypeRegistry, noiseRegistry, biomeRegistry, settingsRegistry, seed);
          return new GeneratorOptions(
              seed, generateStructures, bonusChest, dimensionOptionsRegistry);
        }
      };
}
