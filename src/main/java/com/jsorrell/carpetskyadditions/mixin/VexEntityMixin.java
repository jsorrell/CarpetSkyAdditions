package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.fakes.VexEntityInterface;
import com.jsorrell.carpetskyadditions.helpers.InstantListener;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexEntityMixin extends Monster implements InstantListener.Callback, VexEntityInterface {
    protected DynamicGameEventListener<InstantListener> gameEventHandler;
    protected int numSuccessfulNotes;
    private static final String NUM_SUCCESSFUL_NOTES_KEY = "ConversionNotes";
    protected RandomSource conversionRandom;

    protected VexEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addGameEventListener(EntityType<? extends Vex> entityType, Level world, CallbackInfo ci) {
        this.gameEventHandler = new DynamicGameEventListener<>(
                new InstantListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this));
        this.conversionRandom = new LegacyRandomSource(0);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickListener(CallbackInfo ci) {
        this.gameEventHandler.getListener().tick();
    }

    public int getNote(int noteNum) {
        conversionRandom.setSeed(getUUID().getLeastSignificantBits());
        conversionRandom.consumeCount(noteNum);
        return conversionRandom.nextInt(12);
    }

    public int getNextNote() {
        return getNote(numSuccessfulNotes);
    }

    protected void listenToNote(ServerLevel world, int note) {
        if (note % 12 == getNextNote()) {
            numSuccessfulNotes++;
            world.sendParticles(
                    ParticleTypes.HEART,
                    getRandomX(1),
                    getRandomY() + 0.5,
                    getRandomZ(1),
                    5,
                    world.random.nextGaussian() * 0.02,
                    world.random.nextGaussian() * 0.02,
                    world.random.nextGaussian() * 0.02,
                    1);
            world.playSound(
                    null,
                    this,
                    SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM,
                    SoundSource.HOSTILE,
                    0.1f * (float) numSuccessfulNotes,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            if (5 <= numSuccessfulNotes) {
                this.convertToAllay(world);
            }
        } else {
            world.sendParticles(
                    ParticleTypes.CRIT,
                    getRandomX(1),
                    getRandomY() + 1,
                    getRandomZ(1),
                    5,
                    world.random.nextGaussian() * 0.02,
                    world.random.nextGaussian() * 0.02,
                    world.random.nextGaussian() * 0.02,
                    0.2);
            numSuccessfulNotes = 0;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Vex asVex() {
        if ((Monster) this instanceof Vex vex) {
            return vex;
        } else {
            throw new AssertionError("Not vex");
        }
    }

    public void convertToAllay(ServerLevel world) {
        Allay allay = convertTo(EntityType.ALLAY, false);
        if (allay != null) {
            world.playSound(
                    null,
                    allay.position().x(),
                    allay.position().y(),
                    allay.position().z(),
                    SoundEvents.ZOMBIE_VILLAGER_CURE,
                    SoundSource.HOSTILE,
                    0.5f,
                    2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f);

            AABB criteriaTriggerBox = this.getBoundingBox().inflate(20, 10, 20);
            world.getEntitiesOfClass(ServerPlayer.class, criteriaTriggerBox)
                    .forEach(p -> SkyAdditionsCriteria.ALLAY_VEX.trigger(p, this.asVex(), allay));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(NUM_SUCCESSFUL_NOTES_KEY)) {
            numSuccessfulNotes = nbt.getInt(NUM_SUCCESSFUL_NOTES_KEY);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeMixinNbt(CompoundTag nbt, CallbackInfo ci) {
        if (SkyAdditionsSettings.allayableVexes) {
            nbt.putInt(NUM_SUCCESSFUL_NOTES_KEY, numSuccessfulNotes);
        }
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (level instanceof ServerLevel serverWorld) {
            callback.accept(gameEventHandler, serverWorld);
        }
    }

    @Override
    public TagKey<GameEvent> getTag() {
        return GameEventTags.ALLAY_CAN_LISTEN;
    }

    @Override
    public void accept(
            ServerLevel world,
            GameEventListener listener,
            Vec3 originPos,
            GameEvent gameEvent,
            GameEvent.Context emitter) {
        if (SkyAdditionsSettings.allayableVexes && gameEvent == GameEvent.NOTE_BLOCK_PLAY) {
            BlockState noteBlockState = world.getBlockState(BlockPos.containing(originPos));
            if (noteBlockState.is(Blocks.NOTE_BLOCK)) {
                int note = noteBlockState.getValue(NoteBlock.NOTE);
                listenToNote(world, note);
            }
        }
    }
}
