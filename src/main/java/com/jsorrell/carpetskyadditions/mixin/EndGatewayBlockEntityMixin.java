package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.EndGatewayIslandFeature;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.EndConfiguredFeatures;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
  @Redirect(
    method = "method_46695",
    at = @At(
      value = "FIELD",
      opcode = Opcodes.GETSTATIC,
      target =
        "Lnet/minecraft/world/gen/feature/EndConfiguredFeatures;END_ISLAND:Lnet/minecraft/registry/RegistryKey;"))
  private static RegistryKey<ConfiguredFeature<?, ?>> replaceGeneratedEndIslandFeature() {
    System.out.println("Injection");
    if (SkyAdditionsSettings.gatewaysSpawnChorus) {
      System.out.println("Returning custom feature");
      return EndGatewayIslandFeature.GATEWAY_ISLAND_FEATURE_CONFIGURED;
    } else {
      System.out.println("Returning default feature");
      return EndConfiguredFeatures.END_ISLAND;
    }
  }

  // By default, the exit portal will find the highest block and generate 10 above that.
  // This will always put it directly above the highest chorus fruit block (which can be very high)
  // Additionally, the player will spawn on top of the highest chorus fruit that isn't directly under the portal.
  // This function ensures the portal spawns only 10 blocks above the End Stone.
  // This overrides the default, but that's OK.
  // The portal location can still be manipulated the same b/c this function only runs if the area is all void.
  @Inject(
    method = "setupExitPortalLocation",
    cancellable = true,
    at = @At(
      value = "INVOKE",
      target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V",
      shift = At.Shift.AFTER
    ),
    locals = LocalCapture.CAPTURE_FAILSOFT
  )
  private static void forceExitPortalPos(ServerWorld world, BlockPos pos, CallbackInfoReturnable<BlockPos> cir, Vec3d teleportLocation, WorldChunk portalChunk, BlockPos blockPos, BlockPos islandCenterPos) {
    if (SkyAdditionsSettings.gatewaysSpawnChorus) {
      BlockPos portalPos = EndGatewayIslandFeature.findGatewayLocation(world, islandCenterPos);
      cir.setReturnValue(portalPos);
    }
  }
}
