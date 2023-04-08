package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SonicBoom.class)
public class SonicBoomMixin {
    @Inject(method = "method_43265", at = @At(value = "TAIL"))
    private static void dropEchoShard(Warden warden, ServerLevel level, LivingEntity target, CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableEchoShards) {
            if (target instanceof Dolphin || target instanceof Bat) {
                if (target.isDeadOrDying()) {
                    target.spawnAtLocation(Items.ECHO_SHARD);
                }
            }
        }
    }
}
