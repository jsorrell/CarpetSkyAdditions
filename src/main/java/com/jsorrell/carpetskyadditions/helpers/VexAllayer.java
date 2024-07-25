package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsCriteriaTriggers;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.entity.monster.Vex;
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

public class VexAllayer implements InstantListener.InstantListenerConfig {
    protected DynamicGameEventListener<InstantListener> gameEventHandler;
    private static final String NUM_SUCCESSFUL_NOTES_KEY = "ConversionNotes";
    protected int numSuccessfulNotes;
    protected RandomSource conversionRandom;
    private boolean vexAllayed = false;
    private final Vex vex;

    public VexAllayer(Vex vex) {
        this.vex = vex;
        gameEventHandler = new DynamicGameEventListener<>(
                new InstantListener(new EntityPositionSource(vex, vex.getEyeHeight()), 16, this));
        conversionRandom = new LegacyRandomSource(0);
    }

    public boolean isVexAllayed() {
        return vexAllayed;
    }

    public void tick() {
        if (vexAllayed) {
            convertToAllay();
            return;
        }
        gameEventHandler.getListener().tick();
    }

    public DynamicGameEventListener<InstantListener> getGameEventHandler() {
        return gameEventHandler;
    }

    protected void convertToAllay() {
        Allay allay = vex.convertTo(EntityType.ALLAY, false);
        if (allay != null) {
            float pitch =
                    2.6f + (vex.level().random.nextFloat() - vex.level().random.nextFloat()) * 0.8f;
            vex.level()
                    .playSound(
                            null,
                            vex.position().x(),
                            vex.position().y(),
                            vex.position().z(),
                            SoundEvents.ZOMBIE_VILLAGER_CURE,
                            SoundSource.HOSTILE,
                            0.5f,
                            pitch);

            AABB criteriaTriggerBox = vex.getBoundingBox().inflate(20, 10, 20);
            vex.level()
                    .getEntitiesOfClass(ServerPlayer.class, criteriaTriggerBox)
                    .forEach(p -> SkyAdditionsCriteriaTriggers.ALLAY_VEX.trigger(p, vex, allay));
        }
    }

    public int getNote(int noteNum) {
        conversionRandom.setSeed(vex.getUUID().getLeastSignificantBits());
        conversionRandom.consumeCount(noteNum);
        return conversionRandom.nextInt(12);
    }

    public int getNextNote() {
        return getNote(numSuccessfulNotes);
    }

    @Override
    public TagKey<GameEvent> getTag() {
        return GameEventTags.ALLAY_CAN_LISTEN;
    }

    protected void listenToNote(ServerLevel level, int note) {
        if (note % 12 == getNextNote()) {
            numSuccessfulNotes++;
            level.sendParticles(
                    ParticleTypes.HEART,
                    vex.getRandomX(1),
                    vex.getRandomY() + 0.5,
                    vex.getRandomZ(1),
                    5,
                    level.random.nextGaussian() * 0.02,
                    level.random.nextGaussian() * 0.02,
                    level.random.nextGaussian() * 0.02,
                    1);
            level.playSound(
                    null,
                    vex,
                    SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM,
                    SoundSource.HOSTILE,
                    0.1f * (float) numSuccessfulNotes,
                    (level.random.nextFloat() - level.random.nextFloat()) * 0.2f + 1.0f);

            if (5 <= numSuccessfulNotes) {
                vexAllayed = true;
            }
        } else {
            level.sendParticles(
                    ParticleTypes.CRIT,
                    vex.getRandomX(1),
                    vex.getRandomY() + 1,
                    vex.getRandomZ(1),
                    5,
                    level.random.nextGaussian() * 0.02,
                    level.random.nextGaussian() * 0.02,
                    level.random.nextGaussian() * 0.02,
                    0.2);
            numSuccessfulNotes = 0;
        }
    }

    public void readFromNbt(CompoundTag nbt) {
        if (nbt.contains(NUM_SUCCESSFUL_NOTES_KEY)) {
            numSuccessfulNotes = nbt.getInt(NUM_SUCCESSFUL_NOTES_KEY);
        }
    }

    public void writeToNbt(CompoundTag nbt) {
        if (SkyAdditionsSettings.allayableVexes) {
            nbt.putInt(NUM_SUCCESSFUL_NOTES_KEY, numSuccessfulNotes);
        }
    }

    @Override
    public void accept(
            ServerLevel level,
            GameEventListener listener,
            Vec3 originPos,
            Holder<GameEvent> gameEvent,
            GameEvent.Context emitter) {
        if (SkyAdditionsSettings.allayableVexes && gameEvent == GameEvent.NOTE_BLOCK_PLAY) {
            BlockState noteBlockState = level.getBlockState(BlockPos.containing(originPos));
            if (noteBlockState.is(Blocks.NOTE_BLOCK)) {
                int note = noteBlockState.getValue(NoteBlock.NOTE);
                listenToNote(level, note);
            }
        }
    }
}
