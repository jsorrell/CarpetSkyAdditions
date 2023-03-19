package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
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
public abstract class EnderDragonEntityMixin extends MobEntity implements Monster {
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
        if (SkyAdditionsSettings.renewableDragonHeads) {
            nbt.putBoolean(SHOULD_DROP_HEAD_KEY, shouldDropHead);
        }
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
        if (SkyAdditionsSettings.renewableDragonHeads && shouldDropHead) {
            this.dropItem(Items.DRAGON_HEAD);
        }
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        if (SkyAdditionsSettings.renewableDragonHeads) {
            if (source.getAttacker() instanceof CreeperEntity killerCreeper) {
                if (killerCreeper.shouldDropHead()) {
                    killerCreeper.onHeadDropped();
                    this.shouldDropHead = true;
                }
            }
        }
    }
}
