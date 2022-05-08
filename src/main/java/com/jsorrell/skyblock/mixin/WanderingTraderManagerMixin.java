package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.world.GameRules;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {
  private int currentSpawnTimer;

  @Shadow
  private int spawnChance;

  @Shadow
  private int spawnTimer;

  @Shadow
  @Final
  private ServerWorldProperties properties;

  @Shadow
  private int spawnDelay;

  @Shadow
  @Final
  private AbstractRandom random;

  @Shadow
  protected abstract boolean trySpawn(ServerWorld world);

  private boolean usesDefaultSettings() {
    return SkyBlockSettings.wanderingTraderSpawnRate == 24000 && SkyBlockSettings.maxWanderingTraderSpawnChance == 0.075;
  }

  // For some reason vanilla has 2 probability guards that do nothing but makes the chance not be able to go above 0.1
  // Merging these two checks will slightly change the chance of resetting the spawn chance when no players are online
  @Redirect(method = "trySpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/AbstractRandom;nextInt(I)I"))
  private int skipSecondChanceCheck(AbstractRandom random, int bound) {
    return 100 < this.spawnChance ? 0 : random.nextInt(bound);
  }

  @Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
  public void spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir) {
    // Ensure we don't make changes when the settings are default
    // It should be the same, but it's safer to just use the vanilla version
    if (usesDefaultSettings()) {
      return;
    }

    cir.setReturnValue(0);

    if (!world.getGameRules().getBoolean(GameRules.DO_TRADER_SPAWNING)) {
      return;
    }

    // Cut the timer short if necessary
    if (SkyBlockSettings.wanderingTraderSpawnRate < spawnDelay) {
      spawnDelay = SkyBlockSettings.wanderingTraderSpawnRate;
      currentSpawnTimer = Math.min(1200, spawnDelay);
      spawnTimer = currentSpawnTimer;
    }

    if (--spawnTimer > 0) {
      return;
    }

    spawnDelay -= currentSpawnTimer;

    boolean trySpawn = spawnDelay <= 0;

    spawnDelay = trySpawn ? SkyBlockSettings.wanderingTraderSpawnRate : spawnDelay;
    currentSpawnTimer = Math.min(1200, spawnDelay);
    spawnTimer = currentSpawnTimer;

    this.properties.setWanderingTraderSpawnDelay(spawnDelay);

    if (trySpawn && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
      // Bound changed for high spawn chances b/c the 90% chance for trySpawn to fail is removed
      if (this.random.nextInt(100 < spawnChance ? 1000 : 100) < spawnChance && this.trySpawn(world)) {
        spawnChance = 25;
        cir.setReturnValue(1);
      } else {
        spawnChance = MathHelper.clamp(spawnChance + 25, 25, (int) Math.round(SkyBlockSettings.maxWanderingTraderSpawnChance * 1000d));
      }

      properties.setWanderingTraderSpawnChance(spawnChance);
    }
  }
}
