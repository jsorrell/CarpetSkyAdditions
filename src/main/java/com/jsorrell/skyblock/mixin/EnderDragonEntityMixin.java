package com.jsorrell.skyblock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.helpers.DragonShouldDropHeadHelper;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity implements Monster {

  protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(
      method = "updatePostDeath",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V",
              shift = Shift.AFTER,
              ordinal = 1))
  protected void dropDragonHead(CallbackInfo ci) {
    if (SkyBlockSettings.enableSkyBlockFeatures && SkyBlockSettings.renewableDragonHeads) {
      if (DragonShouldDropHeadHelper.UUIDS.contains(this.getUuid())) {
        this.dropItem(Items.DRAGON_HEAD);
        DragonShouldDropHeadHelper.UUIDS.remove(this.getUuid());
      }
    }
  }
}
