package com.jsorrell.skyblock.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

public class UsefulComposter {
  protected static final List<Block> possibleSurfaceDrops =
      new ArrayList<>(Arrays.asList(Blocks.SAND, Blocks.RED_SAND, Blocks.DIRT));

  public static Item getComposterProduct(Biome biome) {
    Block dropBlock = Blocks.DIRT;

    Block surfaceBlock =
        biome.getGenerationSettings().getSurfaceConfig().getTopMaterial().getBlock();
    if (possibleSurfaceDrops.contains(surfaceBlock)) {
      dropBlock = surfaceBlock;
    }

    return dropBlock.asItem();
  }

  // Provides a simple, single-item inventory for the full composter
  public static class FullComposterInventory extends SimpleInventory implements SidedInventory {
    private final BlockState state;
    private final WorldAccess world;
    private final BlockPos pos;
    private boolean dirty;

    public FullComposterInventory(BlockState state, WorldAccess world, BlockPos pos) {
      super(new ItemStack(getComposterProduct(world.getBiome(pos))));
      this.state = state;
      this.world = world;
      this.pos = pos;
    }

    public int getMaxCountPerStack() {
      return 1;
    }

    public int[] getAvailableSlots(Direction side) {
      return side == Direction.DOWN ? new int[] {0} : new int[0];
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
      return false;
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
      return !this.dirty
          && dir == Direction.DOWN
          && stack.getItem() == getComposterProduct(world.getBiome(pos));
    }

    public void markDirty() {
      emptyComposter(this.state, this.world, this.pos);
      this.dirty = true;
    }

    private static void emptyComposter(BlockState state, WorldAccess world, BlockPos pos) {
      BlockState blockState = state.with(ComposterBlock.LEVEL, 0);
      world.setBlockState(pos, blockState, 3);
    }
  }
}
