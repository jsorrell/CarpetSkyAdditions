package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeodeGenerator {
  // 1/CONVERSION_RATE chance per random tick
  public static final int CONVERSION_RATE = 100;

  public static boolean checkGeodeFormation(World world, BlockPos lavaCenter) {
    return Blocks.LAVA.equals(world.getBlockState(lavaCenter).getBlock())
        && world.getBlockState(lavaCenter).get(FluidBlock.LEVEL) == 0
        &&
        // Calcite
        Blocks.CALCITE.equals(world.getBlockState(lavaCenter.up()).getBlock())
        && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.down()).getBlock())
        && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.north()).getBlock())
        && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.south()).getBlock())
        && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.east()).getBlock())
        && Blocks.CALCITE.equals(world.getBlockState(lavaCenter.west()).getBlock())
        &&
        // Smooth Basalt
        Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.up(2)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.down(2)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.north(2)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.south(2)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.east(2)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.west(2)).getBlock())
        &&
        // Diagonal Smooth Basalt
        Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(0, 1, 1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(0, 1, -1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(0, -1, 1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(0, -1, -1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(1, 0, 1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(1, 0, -1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(-1, 0, 1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(-1, 0, -1)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(1, 1, 0)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(1, -1, 0)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(-1, 1, 0)).getBlock())
        && Blocks.SMOOTH_BASALT.equals(world.getBlockState(lavaCenter.add(-1, -1, 0)).getBlock());
  }
}
