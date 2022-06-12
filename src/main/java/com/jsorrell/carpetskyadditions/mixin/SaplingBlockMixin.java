package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends PlantBlock {

  public SaplingBlockMixin(Settings settings) {
    super(settings);
  }

  private boolean saplingIsOnSand(BlockView world, BlockPos pos) {
    BlockState underBlock = world.getBlockState(pos.down());
    return underBlock.isOf(Blocks.SAND) || underBlock.isOf(Blocks.RED_SAND);
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    if (SkyAdditionsSettings.saplingsDieOnSand) {
      PlantBlock thiss = this;
      if (!(thiss instanceof PropaguleBlock)) {
        return floor.isOf(Blocks.SAND) || floor.isOf(Blocks.RED_SAND) || super.canPlantOnTop(floor, world, pos);
      }
    }
    return super.canPlantOnTop(floor, world, pos);
  }

  @Inject(
    method = "randomTick",
    at = @At("HEAD"),
    cancellable = true
  )
  private void killIfOnSand(BlockState blockState, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
    if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(world, pos)) {
      if (random.nextFloat() < 0.2) {
        world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), Block.NOTIFY_ALL);
      }
      ci.cancel();
    }
  }

  @Inject(
    method = "isFertilizable",
    at = @At("HEAD"),
    cancellable = true
  )
  private void stopBonemealingOnSand(BlockView world, BlockPos pos, BlockState state, boolean isClient, CallbackInfoReturnable<Boolean> cir) {
    if (SkyAdditionsSettings.saplingsDieOnSand && saplingIsOnSand(world, pos)) {
      cir.setReturnValue(false);
    }
  }
}
