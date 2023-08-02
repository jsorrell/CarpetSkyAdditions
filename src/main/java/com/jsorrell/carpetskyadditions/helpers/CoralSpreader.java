package com.jsorrell.carpetskyadditions.helpers;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseRouter;

public abstract class CoralSpreader {
    public static final double TARGET_TEMP = 0.65;
    public static final double TARGET_CONTINENTALNESS = -0.3;

    // Returns a suitability value in the range of 0 to 1
    public static double calculateCoralSuitability(ServerLevel level, BlockPos pos) {
        if (level.dimensionTypeId() != BuiltinDimensionTypes.OVERWORLD) {
            return 0;
        }

        ServerChunkCache chunkCache = level.getChunkSource();
        if (chunkCache.getGenerator() instanceof NoiseBasedChunkGenerator) {
            NoiseRouter noiseRouter = chunkCache.randomState().router();
            DensityFunction.SinglePointContext context =
                    new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
            double temp = noiseRouter.temperature().compute(context);
            double continentalness = noiseRouter.continents().compute(context);
            double squaredDifference =
                    (Mth.square(temp - TARGET_TEMP) + Mth.square(continentalness - TARGET_CONTINENTALNESS));
            return Mth.clamp(1 - squaredDifference, 0, 1);
        }
        return 0.5;
    }

    private static List<Block> getPossibleConversions(ServerLevel level, BlockPos pos, RandomSource random) {
        // Find all coral blocks within a 3x3
        Multiset<Block> blockMap = HashMultiset.create(BuiltInRegistries.BLOCK
                .getTag(BlockTags.CORAL_BLOCKS)
                .orElseThrow()
                .size());
        BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
                .map(pos::offset)
                .map(level::getBlockState)
                .filter(b -> b.is(BlockTags.CORAL_BLOCKS))
                .forEach(b -> blockMap.add(b.getBlock()));
        // Choose one with at least 8 occurences
        return blockMap.entrySet().stream()
                .filter(e -> 8 <= e.getCount())
                .map(Multiset.Entry::getElement)
                .toList();
    }

    public static boolean isConvertible(ServerLevel level, BlockPos pos) {
        return !getPossibleConversions(level, pos, level.getRandom()).isEmpty();
    }

    public static class CustomCalciteBlock extends Block {
        public CustomCalciteBlock(Properties properties) {
            super(properties);
        }

        @Override
        public boolean isRandomlyTicking(BlockState state) {
            return true;
        }

        private static float successChanceFromSuitability(double suitability) {
            final float minSuccessChance = 0.01f;
            final float maxSuccessChance = 0.5f;
            return Mth.lerp((float) Mth.square(suitability), minSuccessChance, maxSuccessChance);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (!SkyAdditionsSettings.spreadingCoral) return;
            if (!Blocks.TUBE_CORAL_BLOCK.defaultBlockState().canSurvive(level, pos)) return;
            double suitability = calculateCoralSuitability(level, pos);
            float successChance = successChanceFromSuitability(suitability);
            if (successChance < random.nextFloat()) return;

            List<Block> validCorals = getPossibleConversions(level, pos, random);
            if (validCorals.isEmpty()) return;

            Block coralBlock = validCorals.get(random.nextInt(validCorals.size()));
            level.setBlockAndUpdate(pos, coralBlock.defaultBlockState());
        }
    }
}
