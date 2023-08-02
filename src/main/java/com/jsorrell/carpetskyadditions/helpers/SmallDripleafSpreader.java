package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public abstract class SmallDripleafSpreader {
    private static final int REQUIRED_LIGHT = 5;
    private static final int MAX_DENSITY = 7;
    private static final float SPREAD_CHANCE = 0.15f;

    public static boolean isSpreadableState(BlockState state) {
        return state.is(Blocks.SMALL_DRIPLEAF)
                && state.getValue(SmallDripleafBlock.HALF) == DoubleBlockHalf.LOWER
                && state.getValue(BlockStateProperties.WATERLOGGED);
    }

    public static boolean canSpreadFrom(BlockState state, ServerLevel level, BlockPos pos) {
        if (!isSpreadableState(state)) return false;
        BlockPos top = pos.above();
        BlockState topState = level.getBlockState(top);
        return topState.getValue(SmallDripleafBlock.HALF) == DoubleBlockHalf.UPPER
                && !topState.getValue(BlockStateProperties.WATERLOGGED)
                && level.getBlockState(pos.below()).is(Blocks.CLAY)
                && level.getMaxLocalRawBrightness(top) == REQUIRED_LIGHT;
    }

    protected static boolean canSpreadTo(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).is(Blocks.WATER)) return false;
        BlockPos top = pos.above();
        return !level.isOutsideBuildHeight(top)
                && level.isEmptyBlock(top)
                && level.getBlockState(pos.below()).is(Blocks.CLAY)
                && level.getMaxLocalRawBrightness(top) == REQUIRED_LIGHT
                && getDensity(level, pos) <= MAX_DENSITY;
    }

    protected static int getDensity(ServerLevel level, BlockPos pos) {
        return (int) BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2)
                .map(pos::offset)
                .filter(p -> level.getBlockState(p).is(Blocks.SMALL_DRIPLEAF))
                .count();
    }

    private static int binomialOffset(int range, RandomSource random) {
        int i = -range;
        for (int k = 0; k < range * 2; ++k) {
            if (random.nextBoolean()) {
                ++i;
            }
        }
        return i;
    }

    private static void placeNewDripleaf(ServerLevel level, BlockPos pos, RandomSource random) {
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockState commonState = Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue(SmallDripleafBlock.FACING, facing);
        BlockState bottomState = commonState.setValue(BlockStateProperties.WATERLOGGED, true);
        BlockState topState = commonState.setValue(SmallDripleafBlock.HALF, DoubleBlockHalf.UPPER);
        level.setBlockAndUpdate(pos, bottomState);
        level.setBlockAndUpdate(pos.above(), topState);
    }

    public static void trySpread(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (SPREAD_CHANCE < random.nextFloat()) return;
        if (!canSpreadFrom(state, level, pos)) return;

        for (int i = 0; i < 3; i++) {
            int xOffset = binomialOffset(5, random);
            int yOffset = binomialOffset(2, random);
            int zOffset = binomialOffset(5, random);
            BlockPos tryPos = pos.offset(xOffset, yOffset, zOffset);
            if (canSpreadTo(level, tryPos)) {
                placeNewDripleaf(level, tryPos, random);
                return;
            }
        }
    }
}
