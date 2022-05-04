package com.jsorrell.skyblock.helpers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.GameEventTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.Optional;

public class InstantListener implements GameEventListener {
  protected final PositionSource positionSource;
  protected final int range;
  protected final InstantListener.Callback callback;
  protected int distance;

  public static Codec<InstantListener> createCodec(InstantListener.Callback callback) {
    return RecordCodecBuilder.create(
      instance ->
        instance.group(
            PositionSource.CODEC.fieldOf("source").forGetter(InstantListener::getPositionSource),
            Codecs.NONNEGATIVE_INT.fieldOf("range").forGetter(InstantListener::getRange),
            Codecs.NONNEGATIVE_INT.fieldOf("event_distance").orElse(0).forGetter(InstantListener::getDistance))
          .apply(instance, (positionSource, range, distance) -> new InstantListener(positionSource, range, callback, distance)));
  }

  public InstantListener(PositionSource positionSource, int range, InstantListener.Callback callback, int distance) {
    this.positionSource = positionSource;
    this.range = range;
    this.callback = callback;
    this.distance = distance;
  }

  @Override
  public PositionSource getPositionSource() {
    return this.positionSource;
  }

  @Override
  public int getRange() {
    return this.range;
  }

  private int getDistance() {
    return this.distance;
  }

  @Override
  public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d originPos) {
    if (!this.callback.canAccept(event, emitter)) {
      return false;
    }
    Optional<Vec3d> optionalPos = this.positionSource.getPos(world);
    if (optionalPos.isEmpty()) {
      return false;
    }
    Vec3d pos = optionalPos.get();

    this.distance = MathHelper.floor(originPos.distanceTo(pos));
    return this.callback.accept(world, this, originPos, event, emitter, this.distance);
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

    boolean accept(ServerWorld world, GameEventListener listener, Vec3d originPos, GameEvent gameEvent, GameEvent.Emitter emitter, int distance);
  }
}
