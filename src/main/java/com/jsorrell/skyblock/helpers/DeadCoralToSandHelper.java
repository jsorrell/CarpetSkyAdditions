package com.jsorrell.skyblock.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DeadCoralToSandHelper {
  protected static final double BREAK_CHANCE = 0.03;

  public static int getSandDropDelay(Random random) {
    return 40 + random.nextInt(40);
  }

  public static boolean tryDropSand(BlockState state, World world, BlockPos pos, Random random) {
    FluidState fluidState = world.getFluidState(pos);
    if (!fluidState.isOf(Fluids.WATER)) {
      return false;
    }

    Vec3d waterVelocity = fluidState.getVelocity(world, pos);
    if (waterVelocity.equals(Vec3d.ZERO)) {
      return false;
    }

    if (!world.isClient) {
      Vec3d sandVelocity = waterVelocity.multiply(0.1);
      Item sandItem = state.getBlock().getLootTableId().getPath().contains("fire") ? Items.RED_SAND : Items.SAND;
      ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, new ItemStack(sandItem), sandVelocity.getX(), sandVelocity.getY(), sandVelocity.getZ());
      itemEntity.setToDefaultPickupDelay();
      world.spawnEntity(itemEntity);
    }

    if (random.nextFloat() < BREAK_CHANCE) {
      world.removeBlock(pos, false);
      world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 0.5f, 1f);
      return false;
    }

    return true;
  }
}
