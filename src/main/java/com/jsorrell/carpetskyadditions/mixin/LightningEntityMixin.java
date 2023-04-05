package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.LightningConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningEntityMixin extends Entity {
    public LightningEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow(prefix = "shadow$")
    protected abstract BlockPos shadow$getStrikePosition();

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/LightningBolt;powerLightningRod()V",
                            shift = At.Shift.AFTER))
    private void onBlockStruck(CallbackInfo ci) {
        LightningConverter.strike(this.level, this.shadow$getStrikePosition());
    }
}
