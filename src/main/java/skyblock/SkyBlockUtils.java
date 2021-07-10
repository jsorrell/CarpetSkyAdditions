package skyblock;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class SkyBlockUtils {
    public static SimpleRegistry<DimensionOptions> getSkyblockSimpleRegistry(Registry<DimensionType> dimensionTypeRegistry, Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
        SimpleRegistry<DimensionOptions> simpleRegistry = new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental());
        simpleRegistry.add(DimensionOptions.NETHER, new DimensionOptions(() -> dimensionTypeRegistry.get(DimensionType.THE_NETHER_REGISTRY_KEY), createNetherGenerator(biomeRegistry, settingsRegistry, seed)), Lifecycle.stable());
        simpleRegistry.add(DimensionOptions.END, new DimensionOptions(() -> dimensionTypeRegistry.get(DimensionType.THE_END_REGISTRY_KEY), createEndGenerator(biomeRegistry, settingsRegistry, seed)), Lifecycle.stable());
        return simpleRegistry;
    }

    public static SkyblockChunkGenerator createOverworldGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
        return new SkyblockChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
    }

    public static SkyblockChunkGenerator createNetherGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
        return new SkyblockChunkGenerator(MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(biomeRegistry, seed), seed, () -> settingsRegistry.get(ChunkGeneratorSettings.NETHER));
    }

    public static SkyblockChunkGenerator createEndGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
        return new SkyblockChunkGenerator(new TheEndBiomeSource(biomeRegistry, seed), seed, () -> settingsRegistry.get(ChunkGeneratorSettings.END));
    }
}