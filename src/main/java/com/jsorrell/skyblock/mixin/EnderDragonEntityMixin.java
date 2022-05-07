package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.fakes.EnderDragonEntityInterface;
import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity implements Monster, EnderDragonEntityInterface {
  protected boolean shouldDropHead;
  private static final String SHOULD_DROP_HEAD_KEY = "ShouldDropHead";

  protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  private void readMixinNbt(NbtCompound nbt, CallbackInfo ci) {
    if (nbt.contains(SHOULD_DROP_HEAD_KEY)) {
      shouldDropHead = nbt.getBoolean(SHOULD_DROP_HEAD_KEY);
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  private void writeMixinNbt(NbtCompound nbt, CallbackInfo ci) {
    if (SkyBlockSettings.renewableDragonHeads) {
      nbt.putBoolean(SHOULD_DROP_HEAD_KEY, shouldDropHead);
    }
  }

  @Override
  public void setShouldDropHead() {
    this.shouldDropHead = true;
  }

  @Inject(
    method = "updatePostDeath",
    at =
    @At(
      value = "INVOKE",
      target =
        "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V",
      ordinal = 1))
  protected void dropDragonHead(CallbackInfo ci) {
    if (SkyBlockSettings.renewableDragonHeads && shouldDropHead) {
      this.dropItem(Items.DRAGON_HEAD);
    }
  }
}
