package com.jsorrell.skyblock.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class DeepslateConversionHelper {
  public static final Potion CONVERSION_POTION = Potions.THICK;

  public static Optional<BlockState> canConvert(BlockState from) {
    if (from.isOf(Blocks.STONE)) {
      return Optional.of(Blocks.DEEPSLATE.getDefaultState());
    }
    return Optional.empty();
  }

  public static boolean convertDeepslateAtPos(World world, BlockState state, BlockPos blockPos) {
    return convertDeepslateAtPos(world, state, blockPos, blockPos);
  }

  public static boolean convertDeepslateAtPos(World world, BlockState state, BlockPos blockPos, BlockPos eventPos) {
    Optional<BlockState> optionalConvertedState = canConvert(state);
    if (optionalConvertedState.isPresent()) {
      if (!world.isClient) {
        ServerWorld serverWorld = (ServerWorld) world;
        for (int i = 0; i < 5; ++i) {
          serverWorld.spawnParticles(ParticleTypes.SPLASH, (double) eventPos.getX() + world.random.nextDouble(), eventPos.getY() + 1, (double) eventPos.getZ() + world.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
        }
      }
      world.playSound(null, eventPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
      world.emitGameEvent(null, GameEvent.FLUID_PLACE, eventPos);
      world.setBlockState(blockPos, optionalConvertedState.get());
      return true;
    }
    return false;
  }
}
