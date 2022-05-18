package com.jsorrell.skyblock.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(EndermanEntity.PickUpBlockGoal.class)
public abstract class EndermanEntity_PickUpBlockGoalMixin {
  @Inject(method = "tick",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z", shift = At.Shift.BEFORE),
    locals = LocalCapture.CAPTURE_FAILSOFT,
    cancellable = true
  )
  private void inject(CallbackInfo ci, Random random, World world, int x, int y, int z, BlockPos targetBlockPos, BlockState targetBlockState) {
    Block targetBlock = targetBlockState.getBlock();
    if (targetBlock instanceof TallPlantBlock || targetBlock instanceof DoorBlock) {
      // Only allow picking up the bottom half
      if (targetBlockState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
        ci.cancel();
      }
    }
  }
}
