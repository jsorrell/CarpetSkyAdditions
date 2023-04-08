package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;

public class GeodeGenerator {
    // 1/CONVERSION_RATE chance per random tick
    public static final int CONVERSION_RATE = 100;

    public static boolean checkGeodeFormation(Level level, BlockPos lavaCenter) {
        return Blocks.LAVA.equals(level.getBlockState(lavaCenter).getBlock())
                && level.getBlockState(lavaCenter).getValue(LiquidBlock.LEVEL) == 0
                &&
                // Calcite
                Blocks.CALCITE.equals(level.getBlockState(lavaCenter.above()).getBlock())
                && Blocks.CALCITE.equals(level.getBlockState(lavaCenter.below()).getBlock())
                && Blocks.CALCITE.equals(level.getBlockState(lavaCenter.north()).getBlock())
                && Blocks.CALCITE.equals(level.getBlockState(lavaCenter.south()).getBlock())
                && Blocks.CALCITE.equals(level.getBlockState(lavaCenter.east()).getBlock())
                && Blocks.CALCITE.equals(level.getBlockState(lavaCenter.west()).getBlock())
                &&
                // Smooth Basalt
                Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.above(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.below(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.north(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.south(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.east(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.west(2)).getBlock())
                &&
                // Diagonal Smooth Basalt
                Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(0, 1, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(0, 1, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(0, -1, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(0, -1, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(1, 0, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(1, 0, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(-1, 0, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(-1, 0, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(1, 1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(1, -1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(-1, 1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        level.getBlockState(lavaCenter.offset(-1, -1, 0)).getBlock());
    }
}
