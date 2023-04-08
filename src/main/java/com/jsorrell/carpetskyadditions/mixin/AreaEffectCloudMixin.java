package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeepslateConversionHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloud.class)
public class AreaEffectCloudMixin {
    @SuppressWarnings("ConstantConditions")
    private AreaEffectCloud asCloud() {
        if ((Object) this instanceof AreaEffectCloud cloud) {
            return cloud;
        } else {
            throw new AssertionError("Not AreaEffectCloud");
        }
    }

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/item/alchemy/Potion;getEffects()Ljava/util/List;"))
    private void convertDeepslateOnTick(CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableDeepslateFromSplash) {
            AreaEffectCloud cloud = asCloud();
            if (cloud.getPotion() == DeepslateConversionHelper.CONVERSION_POTION) {
                DeepslateConversionHelper.convertDeepslateInCloud(cloud.level, cloud.getBoundingBox());
            }
        }
    }
}
