package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.border.WorldBorder;
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

  private BlockState getPortalSkirtBlock(BlockPos pos) {
    if (world.getBiome(pos).matchesKey(BiomeKeys.CRIMSON_FOREST)) {
      return Blocks.CRIMSON_NYLIUM.getDefaultState();
    } else if (world.getBiome(pos).matchesKey(BiomeKeys.WARPED_FOREST)) {
      return Blocks.WARPED_NYLIUM.getDefaultState();
    }
    return Blocks.NETHERRACK.getDefaultState();
  }

  @Inject(method = "createPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;rotateYClockwise()Lnet/minecraft/util/math/Direction;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
  private void addNetherrack(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir, Direction direction, double d, BlockPos blockPos, double e, BlockPos blockPos2, WorldBorder worldBorder) {
    if (SkyAdditionsSettings.renewableNetherrack) {
      if (!worldBorder.contains(blockPos)) {
        return;
      }

      BlockPos.Mutable mutablePos = new BlockPos.Mutable();
      Direction rotatedDirection = direction.rotateYClockwise();
      for (int i = -1; i < 3; ++i) { // i coordinate parallel to portal
        for (int j = -2; j < 3; ++j) { // j coordinate perpendicular to portal
          if ((Math.abs(j) == 1 && (i == -1 || i == 2)) ||
            (Math.abs(j) == 2 && (i == 0 || i == 1))) {
            mutablePos.set(blockPos, direction.getOffsetX() * i + rotatedDirection.getOffsetX() * j, -1, direction.getOffsetZ() * i + rotatedDirection.getOffsetZ() * j);
            world.setBlockState(mutablePos, getPortalSkirtBlock(mutablePos));
          }
        }
      }
    }
  }
}
