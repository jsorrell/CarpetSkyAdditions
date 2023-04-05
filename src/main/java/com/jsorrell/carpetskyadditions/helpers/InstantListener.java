package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class InstantListener implements GameEventListener {
    protected final PositionSource positionSource;
    protected final int range;
    protected final Callback callback;
    protected boolean onCooldown;

    public InstantListener(PositionSource positionSource, int range, InstantListener.Callback callback) {
        this.positionSource = positionSource;
        this.range = range;
        this.callback = callback;
    }

    public void tick() {
        onCooldown = false;
    }

    @Override
    public PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        return this.range;
    }

    @Override
    public boolean handleGameEvent(ServerLevel world, GameEvent event, GameEvent.Context emitter, Vec3 originPos) {
        if (onCooldown) {
            return false;
        }

        if (!callback.canAccept(event, emitter)) {
            return false;
        }

        callback.accept(world, this, originPos, event, emitter);
        onCooldown = true;
        return true;
    }

    public interface Callback {
        default TagKey<GameEvent> getTag() {
            return GameEventTags.VIBRATIONS;
        }

        default boolean canAccept(GameEvent gameEvent, GameEvent.Context emitter) {
            if (!gameEvent.is(this.getTag())) {
                return false;
            }
            Entity entity = emitter.sourceEntity();
            if (entity != null) {
                if (entity.isSpectator()) {
                    return false;
                }
                if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                    if (entity instanceof ServerPlayer serverPlayerEntity) {
                        CriteriaTriggers.AVOID_VIBRATION.trigger(serverPlayerEntity);
                    }
                    return false;
                }
                if (entity.dampensVibrations()) {
                    return false;
                }
            }
            if (emitter.affectedState() != null) {
                return !emitter.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
            }
            return true;
        }

        void accept(
                ServerLevel world,
                GameEventListener listener,
                Vec3 originPos,
                GameEvent gameEvent,
                GameEvent.Context emitter);
    }
}
