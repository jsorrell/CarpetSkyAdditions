package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.Potion;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
    @Shadow
    protected abstract boolean isLingering();

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "onCollision",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onThickPotionCollision(HitResult hitResult, CallbackInfo ci, ItemStack stack, Potion potion) {
        if (SkyAdditionsSettings.renewableDeepslateFromSplash) {
            if (potion == DeepslateConversionHelper.CONVERSION_POTION) {
                Vec3d hitPos = hitResult.getType() == HitResult.Type.BLOCK ? hitResult.getPos() : this.getPos();
                if (this.isLingering()) {
                    // Create the cloud b/c vanilla doesn't when there are no potion effects
                    AreaEffectCloudEntity cloud =
                            new AreaEffectCloudEntity(world, hitPos.getX(), hitPos.getY(), hitPos.getZ());
                    cloud.setRadius(3.0f);
                    cloud.setWaitTime(10);
                    cloud.setRadiusGrowth(-cloud.getRadius() / cloud.getDuration());
                    cloud.setPotion(potion);
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains("CustomPotionColor", NbtElement.NUMBER_TYPE)) {
                        cloud.setColor(nbt.getInt("CustomPotionColor"));
                    }
                    world.spawnEntity(cloud);
                } else {
                    DeepslateConversionHelper.convertDeepslateAtSplash(world, hitPos);
                }
            }
        }
    }
}
