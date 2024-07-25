package com.jsorrell.carpetskyadditions.mixin;

import com.google.common.collect.ImmutableMap;
import com.jsorrell.carpetskyadditions.fakes.CamelInterface;
import com.jsorrell.carpetskyadditions.helpers.TraderCamelHelper;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse implements CamelInterface {
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
        boolean normallyCanBeLeashed = super.canBeLeashed(player);
        boolean canBeLeashed = normallyCanBeLeashed && !isTraderCamel();
        if (normallyCanBeLeashed && !canBeLeashed) {
            // TODO improve this
            // When mod is only installed serverside, the client thinks camels can be leashed.
            // This causes desync, which we can somewhat mitigate by instantly telling the lead to break.
            // This still causes the lead to be "used" client side (ie stack decreased),
            // but is fixed by moving the stack, relogging, etc.
            if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
                serverLevel.getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, null));
            }
        }
        return canBeLeashed;
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

    // This only works with the mod on the client side
    @Redirect(
            method = "getPassengerAttachmentPoint",
            at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;"))
    protected Vec3 moveTraderForward(double x, double y, double z) {
        if (isTraderCamel()) {
            return new Vec3(x, y - 0.45, z + 0.09);
        }
        return new Vec3(x, y, z);
    }
}
