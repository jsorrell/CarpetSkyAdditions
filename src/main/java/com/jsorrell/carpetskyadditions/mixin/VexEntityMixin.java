package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.fakes.VexEntityInterface;
import com.jsorrell.carpetskyadditions.helpers.InstantListener;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.function.BiConsumer;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends HostileEntity implements InstantListener.Callback, VexEntityInterface {
  protected EntityGameEventHandler<InstantListener> gameEventHandler;
  protected int numSuccessfulNotes;
  private static final String NUM_SUCCESSFUL_NOTES_KEY = "ConversionNotes";
  protected Random conversionRandom;

  protected VexEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void addGameEventListener(EntityType<? extends VexEntity> entityType, World world, CallbackInfo ci) {
    this.gameEventHandler = new EntityGameEventHandler<>(new InstantListener(new EntityPositionSource(this, this.getStandingEyeHeight()), 16, this));
    this.conversionRandom = new CheckedRandom(0);
  }

  @Inject(method = "tick", at = @At("TAIL"))
  private void tickListener(CallbackInfo ci) {
    this.gameEventHandler.getListener().tick();
  }

  public int getNote(int noteNum) {
    conversionRandom.setSeed(getUuid().getLeastSignificantBits());
    conversionRandom.skip(noteNum);
    return conversionRandom.nextInt(12);
  }

  public int getNextNote() {
    return getNote(numSuccessfulNotes);
  }

  protected void listenToNote(ServerWorld world, int note) {
    if (note % 12 == getNextNote()) {
      numSuccessfulNotes++;
      world.spawnParticles(ParticleTypes.HEART,
        getParticleX(1), getRandomBodyY() + 0.5, getParticleZ(1), 5, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, 1);
      world.playSound(null, getPos().getX(), getPos().getY() + 0.4, getPos().getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.HOSTILE, 0.1f * (float) numSuccessfulNotes, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
      if (5 <= numSuccessfulNotes) {
        this.convertToAllay(world);
      }
    } else {
      world.spawnParticles(ParticleTypes.CRIT,
        getParticleX(1), getRandomBodyY() + 1, getParticleZ(1), 5, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, 0.2);
      numSuccessfulNotes = 0;
    }
  }

  public void convertToAllay(ServerWorld world) {
    AllayEntity allay = convertTo(EntityType.ALLAY, false);
    if (allay != null) {
      world.playSound(null, allay.getPos().getX(), allay.getPos().getY(), allay.getPos().getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f);

      Iterator<ServerPlayerEntity> nearbyPlayers =
        world
          .getNonSpectatingEntities(
            ServerPlayerEntity.class, (new Box(getBlockPos())).expand(20.0D, 10.0D, 20.0D))
          .iterator();
      HostileEntity thiss = this;
      if (thiss instanceof VexEntity vex) {
        nearbyPlayers.forEachRemaining(player -> SkyAdditionsCriteria.ALLAY_VEX.trigger(player, vex, allay));
      }
    }
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  private void readMixinNbt(NbtCompound nbt, CallbackInfo ci) {
    if (nbt.contains(NUM_SUCCESSFUL_NOTES_KEY)) {
      numSuccessfulNotes = nbt.getInt(NUM_SUCCESSFUL_NOTES_KEY);
    }
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  private void writeMixinNbt(NbtCompound nbt, CallbackInfo ci) {
    if (SkyAdditionsSettings.allayableVexes) {
      nbt.putInt(NUM_SUCCESSFUL_NOTES_KEY, numSuccessfulNotes);
    }
  }

  @Override
  public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
    if (world instanceof ServerWorld serverWorld) {
      callback.accept(gameEventHandler, serverWorld);
    }
  }

  @Override
  public TagKey<GameEvent> getTag() {
    return GameEventTags.ALLAY_CAN_LISTEN;
  }

  @Override
  public void accept(ServerWorld world, GameEventListener listener, Vec3d originPos, GameEvent gameEvent, GameEvent.Emitter emitter) {
    if (SkyAdditionsSettings.allayableVexes && gameEvent == GameEvent.NOTE_BLOCK_PLAY) {
      BlockState noteBlockState = world.getBlockState(new BlockPos(originPos));
      if (noteBlockState.isOf(Blocks.NOTE_BLOCK)) {
        int note = noteBlockState.get(NoteBlock.NOTE);
        listenToNote(world, note);
      }
    }
  }
}
