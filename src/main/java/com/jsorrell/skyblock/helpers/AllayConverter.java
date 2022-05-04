package com.jsorrell.skyblock.helpers;

import com.jsorrell.skyblock.criterion.SkyBlockCriteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.AbstractRandom;

import java.util.Iterator;

public class AllayConverter {
  public static final int NUM_NOTES = 5;
  private int numSuccessfulNotes = 0;
  private final AbstractRandom random = AbstractRandom.create();
  protected final VexEntity vex;

  public AllayConverter(VexEntity vex) {
    this.vex = vex;
  }

  public int getNote(int noteNum) {
    this.random.setSeed(this.vex.getUuid().getLeastSignificantBits());
    this.random.skip(noteNum);
    return this.random.nextInt(12);
  }

  public int getNextNote() {
    return getNote(numSuccessfulNotes);
  }

  public boolean listenToNote(ServerWorld world, int note) {
    if (note % 12 == getNextNote()) {
      numSuccessfulNotes++;
      world.spawnParticles(ParticleTypes.HEART,
        vex.getParticleX(1), vex.getRandomBodyY() + 0.5, vex.getParticleZ(1), 5, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, 1);
      world.playSound(null, vex.getPos().getX(), vex.getPos().getY() + 0.4, vex.getPos().getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.HOSTILE, 0.1f * (float) numSuccessfulNotes, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
      if (numSuccessfulNotes == NUM_NOTES) {
        AllayEntity allay = this.vex.convertTo(EntityType.ALLAY, false);
        if (allay != null) {
          world.playSound(null, allay.getPos().getX(), allay.getPos().getY(), allay.getPos().getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f);

          Iterator<ServerPlayerEntity> nearbyPlayers =
            world
              .getNonSpectatingEntities(
                ServerPlayerEntity.class, (new Box(this.vex.getBlockPos())).expand(20.0D, 10.0D, 20.0D))
              .iterator();
          nearbyPlayers.forEachRemaining(player -> SkyBlockCriteria.ALLAY_VEX.trigger(player, vex, allay));
          return true;
        }
      }
    } else {
      world.spawnParticles(ParticleTypes.CRIT,
        vex.getParticleX(1), vex.getRandomBodyY() + 1, vex.getParticleZ(1), 5, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, world.random.nextGaussian() * 0.02, 0.2);
      numSuccessfulNotes = 0;
    }
    return false;
  }
}
