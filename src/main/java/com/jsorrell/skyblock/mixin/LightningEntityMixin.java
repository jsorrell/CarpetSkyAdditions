package com.jsorrell.skyblock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.jsorrell.skyblock.helpers.LightningConverter;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity {
  public LightningEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow(prefix = "shadow$")
  protected abstract BlockPos shadow$getAffectedBlockPos();

  @Inject(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/LightningEntity;powerLightningRod()V",
              shift = At.Shift.AFTER))
  private void onBlockStruck(CallbackInfo ci) {
    LightningConverter.strike(this.world, this.shadow$getAffectedBlockPos());
  }
}
