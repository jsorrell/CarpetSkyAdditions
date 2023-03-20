package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.AreaEffectCloudEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin {
    @SuppressWarnings("ConstantConditions")
    private AreaEffectCloudEntity asCloud() {
        if ((Object) this instanceof AreaEffectCloudEntity cloud) {
            return cloud;
        } else {
            throw new AssertionError("Not AreaEffectCloud");
        }
    }

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getEffects()Ljava/util/List;"))
    private void convertDeepslateOnTick(CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableDeepslateFromSplash) {
            AreaEffectCloudEntity cloud = this.asCloud();
            if (cloud.getPotion() == DeepslateConversionHelper.CONVERSION_POTION) {
                DeepslateConversionHelper.convertDeepslateInCloud(cloud.world, cloud.getBoundingBox());
            }
        }
    }
}
