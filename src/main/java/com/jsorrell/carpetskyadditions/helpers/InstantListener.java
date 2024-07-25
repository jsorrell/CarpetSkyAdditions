package com.jsorrell.carpetskyadditions.helpers;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
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
    protected final InstantListenerConfig instantListenerConfig;
    protected boolean onCooldown;

    public InstantListener(PositionSource positionSource, int range, InstantListenerConfig instantListenerConfig) {
        this.positionSource = positionSource;
        this.range = range;
        this.instantListenerConfig = instantListenerConfig;
    }

    public void tick() {
        onCooldown = false;
    }

    @Override
    public PositionSource getListenerSource() {
        return positionSource;
    }

    @Override
    public int getListenerRadius() {
        return range;
    }

    @Override
    public boolean handleGameEvent(
            ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 originPos) {
        if (onCooldown) {
            return false;
        }

        if (!instantListenerConfig.canAccept(event, context)) {
            return false;
        }

        instantListenerConfig.accept(level, this, originPos, event, context);
        onCooldown = true;
        return true;
    }

    public interface InstantListenerConfig {
        default TagKey<GameEvent> getTag() {
            return GameEventTags.VIBRATIONS;
        }

        default boolean canAccept(Holder<GameEvent> gameEvent, GameEvent.Context context) {
            if (!gameEvent.is(getTag())) {
                return false;
            }
            Entity entity = context.sourceEntity();
            if (entity != null) {
                if (entity.isSpectator()) {
                    return false;
                }
                if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                    if (entity instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.AVOID_VIBRATION.trigger(serverPlayer);
                    }
                    return false;
                }
                if (entity.dampensVibrations()) {
                    return false;
                }
            }
            if (context.affectedState() != null) {
                return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
            }
            return true;
        }

        void accept(
                ServerLevel level,
                GameEventListener listener,
                Vec3 originPos,
                Holder<GameEvent> gameEvent,
                GameEvent.Context context);
    }
}
