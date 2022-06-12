package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.fakes.EnderDragonEntityInterface;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

  public LivingEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Inject(
    method = "onDeath",
    locals = LocalCapture.CAPTURE_FAILSOFT,
    at =
    @At(
      value = "INVOKE_ASSIGN",
      target =
        "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"))
  public void rememberDragonKiller(DamageSource source, CallbackInfo ci, Entity killer) {
    if (SkyAdditionsSettings.renewableDragonHeads) {
      if (this.getType() == EntityType.ENDER_DRAGON && killer instanceof CreeperEntity killerCreeper) {
        if (killerCreeper.shouldDropHead()) {
          killerCreeper.onHeadDropped();
          ((EnderDragonEntityInterface) this).setShouldDropHead();
        }
      }
    }
  }
}
