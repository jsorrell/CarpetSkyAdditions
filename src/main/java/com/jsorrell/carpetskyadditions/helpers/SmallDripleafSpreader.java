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

    public static boolean canTick(BlockState state) {
        return state.getValue(SmallDripleafBlock.HALF) == DoubleBlockHalf.LOWER
                && state.getValue(BlockStateProperties.WATERLOGGED);
    }

    public static boolean canPropagate(BlockState state, ServerLevel level, BlockPos pos) {
        return canTick(state)
                && level.getBlockState(pos.below()).is(Blocks.CLAY)
                && level.getMaxLocalRawBrightness(pos.above()) == REQUIRED_LIGHT
                && !level.getBlockState(pos.above()).getValue(BlockStateProperties.WATERLOGGED);
    }

    protected static boolean canPropagateTo(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.WATER)
                && level.getBlockState(pos.below()).is(Blocks.CLAY)
                && !level.isOutsideBuildHeight(pos.above())
                && level.isEmptyBlock(pos.above())
                && level.getMaxLocalRawBrightness(pos.above()) == REQUIRED_LIGHT
                && getDensity(level, pos) <= MAX_DENSITY;
    }

    protected static int getDensity(ServerLevel level, BlockPos pos) {
        return (int) BlockPos.betweenClosedStream(new BlockPos(-2, 0, -2), new BlockPos(2, 2, 2))
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

    public static void tryPropagate(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (SPREAD_CHANCE < random.nextFloat()) return;
        if (!canPropagate(state, level, pos)) return;

        for (int i = 0; i < 3; i++) {
            int xOffset = binomialOffset(5, random);
            int yOffset = binomialOffset(2, random);
            int zOffset = binomialOffset(5, random);
            BlockPos tryPos = pos.offset(xOffset, yOffset, zOffset);
            if (canPropagateTo(level, tryPos)) {
                placeNewDripleaf(level, tryPos, random);
                return;
            }
        }
    }
}
