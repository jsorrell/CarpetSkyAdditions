package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends Mob implements Enemy {
    protected boolean shouldDropHead;
    private static final String SHOULD_DROP_HEAD_KEY = "ShouldDropHead";

    protected EnderDragonMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(SHOULD_DROP_HEAD_KEY)) {
            shouldDropHead = nbt.getBoolean(SHOULD_DROP_HEAD_KEY);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableDragonHeads) {
            nbt.putBoolean(SHOULD_DROP_HEAD_KEY, shouldDropHead);
        }
    }

    @Inject(
            method = "tickDeath",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V",
                            ordinal = 1))
    protected void dropDragonHead(CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableDragonHeads && shouldDropHead) {
            spawnAtLocation(Items.DRAGON_HEAD);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        super.dropCustomDeathLoot(damageSource, looting, hitByPlayer);
        if (SkyAdditionsSettings.renewableDragonHeads) {
            if (damageSource.getEntity() instanceof Creeper killerCreeper) {
                if (killerCreeper.canDropMobsSkull()) {
                    killerCreeper.increaseDroppedSkulls();
                    shouldDropHead = true;
                }
            }
        }
    }
}
