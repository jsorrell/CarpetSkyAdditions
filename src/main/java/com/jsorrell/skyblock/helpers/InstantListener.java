package com.jsorrell.skyblock.helpers;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.GameEventTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

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
  public PositionSource getPositionSource() {
    return this.positionSource;
  }

  @Override
  public int getRange() {
    return this.range;
  }

  @Override
  public boolean listen(ServerWorld world, GameEvent.class_7447 event) {
    if (onCooldown) {
      return false;
    }

    if (!callback.canAccept(event.method_43724(), event.method_43727())) {
      return false;
    }

    Vec3d originPos = event.method_43726();
    callback.accept(world, this, originPos, event.method_43724(), event.method_43727());
    onCooldown = true;
    return true;
  }

  public interface Callback {
    default TagKey<GameEvent> getTag() {
      return GameEventTags.VIBRATIONS;
    }

    default boolean canAccept(GameEvent gameEvent, GameEvent.Emitter emitter) {
      if (!gameEvent.isIn(this.getTag())) {
        return false;
      }
      Entity entity = emitter.sourceEntity();
      if (entity != null) {
        if (entity.isSpectator()) {
          return false;
        }
        if (entity.bypassesSteppingEffects() && gameEvent.isIn(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
          if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.AVOID_VIBRATION.trigger(serverPlayerEntity);
          }
          return false;
        }
        if (entity.occludeVibrationSignals()) {
          return false;
        }
      }
      if (emitter.affectedState() != null) {
        return !emitter.affectedState().isIn(BlockTags.DAMPENS_VIBRATIONS);
      }
      return true;
    }

    void accept(ServerWorld world, GameEventListener listener, Vec3d originPos, GameEvent gameEvent, GameEvent.Emitter emitter);
  }
}
