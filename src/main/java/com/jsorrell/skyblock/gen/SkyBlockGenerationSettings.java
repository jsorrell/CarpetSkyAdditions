package com.jsorrell.skyblock.gen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class SkyBlockGenerationSettings {

  public static final String NAME = "skyblock";

  public static SimpleRegistry<DimensionOptions> getSkyBlockDimensionOptions(
      Registry<DimensionType> dimensionTypeRegistry,
      Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
      Registry<Biome> biomeRegistry,
      Registry<ChunkGeneratorSettings> settingsRegistry,
      long seed) {
    SimpleRegistry<DimensionOptions> simpleRegistry =
        new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental());
    simpleRegistry.add(
        DimensionOptions.OVERWORLD,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.OVERWORLD_REGISTRY_KEY),
            createOverworldGenerator(noiseRegistry, biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    simpleRegistry.add(
        DimensionOptions.NETHER,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.THE_NETHER_REGISTRY_KEY),
            createNetherGenerator(noiseRegistry, biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    simpleRegistry.add(
        DimensionOptions.END,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.THE_END_REGISTRY_KEY),
            createEndGenerator(noiseRegistry, biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    return simpleRegistry;
  }

  public static ChunkGenerator createOverworldGenerator(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
                                                        Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    return new SkyBlockChunkGenerator(
        noiseRegistry,
        MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(biomeRegistry).withSeed(seed),
        seed,
        () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
  }

  public static ChunkGenerator createNetherGenerator(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
                                                     Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    return new SkyBlockChunkGenerator(
        noiseRegistry,
        MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(biomeRegistry).withSeed(seed),
        seed,
        () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.NETHER));
  }

  public static ChunkGenerator createEndGenerator(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
                                                  Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    return new SkyBlockChunkGenerator(
        noiseRegistry,
        new TheEndBiomeSource(biomeRegistry, seed),
        seed,
        () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.END));
  }
}
