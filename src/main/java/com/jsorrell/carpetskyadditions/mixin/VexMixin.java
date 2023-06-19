package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.fakes.VexInterface;
import com.jsorrell.carpetskyadditions.helpers.InstantListener;
import com.jsorrell.carpetskyadditions.helpers.VexAllayer;
import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexMixin extends Monster implements InstantListener.InstantListenerConfig, VexInterface {
    protected VexMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    private final VexAllayer vexAllayer = new VexAllayer(asVex());

    @Override
    public VexAllayer getAllayer() {
        return vexAllayer;
    }

    @SuppressWarnings("ConstantConditions")
    private Vex asVex() {
        if ((Monster) this instanceof Vex vex) {
            return vex;
        } else {
            throw new AssertionError("Not vex");
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickListener(CallbackInfo ci) {
        vexAllayer.tick();
        if (vexAllayer.isVexAllayed()) {
            ci.cancel();
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        vexAllayer.readFromNbt(nbt);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        vexAllayer.writeToNbt(nbt);
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        if (level() instanceof ServerLevel serverLevel) {
            listenerConsumer.accept(vexAllayer.getGameEventHandler(), serverLevel);
        }
    }
}
