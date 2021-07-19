package com.jsorrell.skyblock.mixin;

import java.util.Iterator;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.jsorrell.skyblock.criterion.Criteria;
import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.helpers.GeodeGenerator;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
  @Inject(method = "onRandomTick", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "HEAD"))
  private void tryCreateGeode(
      World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
    if (SkyBlockSettings.enableSkyBlockFeatures && SkyBlockSettings.renewableBuddingAmethysts) {
      if (random.nextInt(GeodeGenerator.CONVERSION_RATE) == 0) {
        if (GeodeGenerator.checkGeodeFormation(world, pos)) {
          world.setBlockState(pos, Blocks.BUDDING_AMETHYST.getDefaultState());

          Iterator<ServerPlayerEntity> nearbyPlayers =
              world
                  .getNonSpectatingEntities(
                      ServerPlayerEntity.class, (new Box(pos)).expand(50.0D, 20.0D, 50.0D))
                  .iterator();
          nearbyPlayers.forEachRemaining(Criteria.GENERATE_GEODE::trigger);
        }
      }
    }
  }
}
