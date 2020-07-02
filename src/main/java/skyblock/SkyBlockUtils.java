package skyblock;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import skyblock.mixin.DimensionTypeAccessor;

public class SkyBlockUtils {
    public static SimpleRegistry<DimensionOptions> getSkyblockSimpleRegistry(long seed) {
        SimpleRegistry<DimensionOptions> simpleRegistry = new SimpleRegistry<>(Registry.DIMENSION_OPTIONS, Lifecycle.stable());
        simpleRegistry.add(DimensionOptions.NETHER, new DimensionOptions(DimensionTypeAccessor::getTheNether, SkyBlockUtils.createNetherGenerator(seed)));
        simpleRegistry.add(DimensionOptions.END, new DimensionOptions(DimensionTypeAccessor::getTheEnd, SkyBlockUtils.createEndGenerator(seed)));
        simpleRegistry.markLoaded(DimensionOptions.NETHER);
        simpleRegistry.markLoaded(DimensionOptions.END);
        return simpleRegistry;
    }

    public static SkyblockChunkGenerator createOverworldGenerator(long seed) {
        return new SkyblockChunkGenerator(seed, new VanillaLayeredBiomeSource(seed, false, false), ChunkGeneratorType.Preset.OVERWORLD.getChunkGeneratorType());
    }

    public static SkyblockChunkGenerator createNetherGenerator(long seed) {
        return new SkyblockChunkGenerator(seed, MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(seed), ChunkGeneratorType.Preset.NETHER.getChunkGeneratorType());
    }

    public static SkyblockChunkGenerator createEndGenerator(long seed) {
        return new SkyblockChunkGenerator(seed, new TheEndBiomeSource(seed), ChunkGeneratorType.Preset.END.getChunkGeneratorType());
    }
}
