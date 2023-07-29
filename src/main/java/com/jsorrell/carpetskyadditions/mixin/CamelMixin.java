package com.jsorrell.carpetskyadditions.mixin;

import com.google.common.collect.ImmutableMap;
import com.jsorrell.carpetskyadditions.fakes.CamelInterface;
import com.jsorrell.carpetskyadditions.helpers.TraderCamelHelper;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse implements CamelInterface {
    @Shadow
    public abstract LivingEntity getControllingPassenger();

    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    @SuppressWarnings("ConstantConditions")
    private Camel asCamel() {
        if ((AbstractHorse) this instanceof Camel camel) {
            return camel;
        } else {
            throw new AssertionError("Not camel");
        }
    }

    @Unique
    public boolean isTraderCamel() {
        return TraderCamelHelper.isTraderCamel(asCamel());
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return !isTraderCamel() && super.canBeLeashed(player);
    }

    @Inject(method = "canAddPassenger", at = @At("HEAD"), cancellable = true)
    public void canAddPassengerToTraderCamel(Entity passenger, CallbackInfoReturnable<Boolean> cir) {
        if (isTraderCamel()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    public void isFoodForTraderCamel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (isTraderCamel()) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private Dynamic<?> getBlankBrainDynamic() {
        NbtOps nbtOps = NbtOps.INSTANCE;
        return new Dynamic<>(
                nbtOps, nbtOps.createMap(ImmutableMap.of(nbtOps.createString("memories"), nbtOps.emptyMap())));
    }

    @Unique
    public void carpetSkyAdditions$makeTraderCamel() {
        brain = TraderCamelHelper.TraderCamelAI.makeBrain(
                CamelAi.brainProvider().makeBrain(getBlankBrainDynamic()));
    }

    @Unique
    public void carpetSkyAdditions$makeStandaloneCamel() {
        brain = makeBrain(getBlankBrainDynamic());
    }

    @Redirect(method = "positionRider", at = @At(value = "NEW", args = "class=net/minecraft/world/phys/Vec3"))
    protected Vec3 moveTraderForward(double x, double y, double z) {
        if (isTraderCamel()) {
            return new Vec3(x, y, z + 0.09);
        }
        return new Vec3(x, y, z);
    }

    @Inject(
            method = "positionRider",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/animal/camel/Camel;clampRotation(Lnet/minecraft/world/entity/Entity;)V"),
            cancellable = true)
    protected void fixTraderRotation(Entity passenger, MoveFunction callback, CallbackInfo ci) {
        if (isTraderCamel()) {
            getControllingPassenger().yBodyRot = yBodyRot;
            ci.cancel();
        }
    }
}
