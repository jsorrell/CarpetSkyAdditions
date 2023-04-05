package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;

public class GeodeGenerator {
    // 1/CONVERSION_RATE chance per random tick
    public static final int CONVERSION_RATE = 100;

    public static boolean checkGeodeFormation(Level world, BlockPos lavaCenter) {
        return Blocks.LAVA.equals(world.getBlockState(lavaCenter).getBlock())
                && world.getBlockState(lavaCenter).getValue(LiquidBlock.LEVEL) == 0
                &&
                // Calcite
                Blocks.CALCITE.equals(world.getBlockState(lavaCenter.above()).getBlock())
                && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.below()).getBlock())
                && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.north()).getBlock())
                && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.south()).getBlock())
                && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.east()).getBlock())
                && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.west()).getBlock())
                &&
                // Smooth Basalt
                Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.above(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.below(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.north(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.south(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.east(2)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.west(2)).getBlock())
                &&
                // Diagonal Smooth Basalt
                Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(0, 1, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(0, 1, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(0, -1, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(0, -1, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(1, 0, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(1, 0, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(-1, 0, 1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(-1, 0, -1)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(1, 1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(1, -1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(-1, 1, 0)).getBlock())
                && Blocks.SMOOTH_BASALT.equals(
                        world.getBlockState(lavaCenter.offset(-1, -1, 0)).getBlock());
    }
}
