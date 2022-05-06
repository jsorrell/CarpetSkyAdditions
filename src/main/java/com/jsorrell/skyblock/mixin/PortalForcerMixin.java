package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.SkyBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {
  @Shadow
  @Final
  private ServerWorld world;

  @Inject(method = "createPortal", at = @At(value = "CONSTANT", args = "intValue=-1", ordinal = 4), locals = LocalCapture.CAPTURE_FAILSOFT)
  private void addNetherrack(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir, Direction direction, double d, BlockPos blockPos) {
    if (SkyBlockSettings.renewableNetherrack) {
      BlockPos.Mutable mutablePos = new BlockPos.Mutable();
      Direction rotatedDirection = direction.rotateYClockwise();
      for (int i = -1; i < 3; ++i) { // i coordinate parallel to portal
        for (int j = -2; j < 3; ++j) { // j coordinate perpendicular to portal
          if ((Math.abs(j) == 1 && (i == -1 || i == 2)) ||
            (Math.abs(j) == 2 && (i == 0 || i == 1))) {
            world.setBlockState(mutablePos.set(blockPos, direction.getOffsetX() * i + rotatedDirection.getOffsetX() * j, -1, direction.getOffsetZ() * i + rotatedDirection.getOffsetZ() * j), Blocks.NETHERRACK.getDefaultState());
          }
        }
      }
    }
  }
}
