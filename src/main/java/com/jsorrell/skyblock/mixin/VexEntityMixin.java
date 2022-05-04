package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.fakes.VexEntityInterface;
import com.jsorrell.skyblock.helpers.AllayConverter;
import com.jsorrell.skyblock.helpers.InstantListener;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.GameEventTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends HostileEntity implements InstantListener.Callback, VexEntityInterface {
  protected EntityGameEventHandler<?> gameEventHandler;
  public AllayConverter allayConverter;

  protected VexEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
    super(entityType, world);
  }

  public AllayConverter getAllayConverter() {
    return allayConverter;
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void addGameEventListener(EntityType<? extends VexEntity> entityType, World world, CallbackInfo ci) {
    this.gameEventHandler = new EntityGameEventHandler<>(new InstantListener(new EntityPositionSource(this, this.getStandingEyeHeight()), 16, this, 0));
    HostileEntity thiss = this;
    if (thiss instanceof VexEntity vexEntity) {
      this.allayConverter = new AllayConverter(vexEntity);
    }
  }

  @Override
  public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
    World world = this.world;
    if (world instanceof ServerWorld serverWorld) {
      callback.accept(this.gameEventHandler, serverWorld);
    }
  }

  @Override
  public TagKey<GameEvent> getTag() {
    return GameEventTags.ALLAY_CAN_LISTEN;
  }

  @Override
  public boolean accept(ServerWorld world, GameEventListener listener, Vec3d originPos, GameEvent gameEvent, GameEvent.Emitter emitter, int distance) {
    if (SkyBlockSettings.renewableAllays && gameEvent == GameEvent.NOTE_BLOCK_PLAY) {
      BlockState noteBlockState = world.getBlockState(new BlockPos(originPos));
      if (noteBlockState.isOf(Blocks.NOTE_BLOCK)) {
        int note = noteBlockState.get(NoteBlock.NOTE);
        return this.allayConverter.listenToNote(world, note);
      }
    }
    return false;
  }
}
