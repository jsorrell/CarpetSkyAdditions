package com.jsorrell.skyblock.gen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class SkyBlockGenerationSettings {

  public static final String NAME = "skyblock";

  public static SimpleRegistry<DimensionOptions> getSkyBlockDimensionOptionsRegistry(
      DynamicRegistryManager drm,
      long seed) {
    SimpleRegistry<DimensionOptions> dimensionOptionsRegistry = new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental());

    Registry<DimensionType> dimensionTypeRegistry = drm.get(Registry.DIMENSION_TYPE_KEY);

    dimensionOptionsRegistry.add(
        DimensionOptions.OVERWORLD,
        new DimensionOptions(
            () -> dimensionTypeRegistry.getOrThrow(DimensionType.OVERWORLD_REGISTRY_KEY),
            createOverworldGenerator(drm, seed)),
        Lifecycle.stable());

    dimensionOptionsRegistry.add(
        DimensionOptions.NETHER,
        new DimensionOptions(
            () -> dimensionTypeRegistry.getOrThrow(DimensionType.THE_NETHER_REGISTRY_KEY),
            createNetherGenerator(drm, seed)),
        Lifecycle.stable());

    dimensionOptionsRegistry.add(
        DimensionOptions.END,
        new DimensionOptions(
            () -> dimensionTypeRegistry.getOrThrow(DimensionType.THE_END_REGISTRY_KEY),
            createEndGenerator(drm, seed)),
        Lifecycle.stable());
    return dimensionOptionsRegistry;
  }

  public static SkyBlockChunkGenerator createOverworldGenerator(DynamicRegistryManager drm, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = drm.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        drm.get(Registry.NOISE_WORLDGEN),
        MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(drm.get(Registry.BIOME_KEY), true),
        seed,
        () -> chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
  }

  public static SkyBlockChunkGenerator createNetherGenerator(DynamicRegistryManager drm, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = drm.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        drm.get(Registry.NOISE_WORLDGEN),
        MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(drm.get(Registry.BIOME_KEY)).withSeed(seed),
        seed,
        () -> chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.NETHER));
  }

  public static SkyBlockChunkGenerator createEndGenerator(DynamicRegistryManager drm, long seed) {
    Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = drm.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
    return new SkyBlockChunkGenerator(
        drm.get(Registry.NOISE_WORLDGEN),
        new TheEndBiomeSource(drm.get(Registry.BIOME_KEY), seed),
        seed,
        () -> chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.END));
  }
}
