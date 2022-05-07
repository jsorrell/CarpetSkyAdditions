package com.jsorrell.skyblock.helpers;

import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LightningConverter {
  public static void strike(World world, BlockPos pos) {
    BlockState rawHitBlock = world.getBlockState(pos);
    BlockPos hitBlockPos;
    BlockState hitBlock;
    if (rawHitBlock.isOf(Blocks.LIGHTNING_ROD)) {
      hitBlockPos = pos.offset(rawHitBlock.get(LightningRodBlock.FACING).getOpposite());
      hitBlock = world.getBlockState(hitBlockPos);
    } else {
      hitBlockPos = pos;
      hitBlock = rawHitBlock;
    }

    alchemizeVinesToGlowLichen(world, hitBlockPos, hitBlock);
  }

  protected static void alchemizeVinesToGlowLichen(
      World world, BlockPos hitBlockPos, BlockState hitBlock) {
    if (!SkyBlockSettings.lightningElectrifiesVines
        || !hitBlock.getBlock().equals(Blocks.GLOWSTONE)) return;

    boolean conversionOccurred = false;
    for (Direction dir : Direction.values()) {
      BlockPos adjacentBlockPos = hitBlockPos.add(dir.getVector());
      BlockState adjacentBlock = world.getBlockState(adjacentBlockPos);
      if (adjacentBlock.getBlock().equals(Blocks.VINE)
          && adjacentBlock.get(VineBlock.getFacingProperty(dir.getOpposite()))) {
        world.setBlockState(
            adjacentBlockPos,
            Blocks.GLOW_LICHEN
                .getDefaultState()
                .with(GlowLichenBlock.getProperty(dir.getOpposite()), true));
        conversionOccurred = true;
      }
    }
  }
}
