package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderSpawnerMixin {
    private int currentSpawnTimer;

    @Shadow
    private int spawnChance;

    @Shadow
    private int tickDelay;

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Shadow
    private int spawnDelay;

    @Shadow
    @Final
    private RandomSource random;

    @Shadow
    protected abstract boolean spawn(ServerLevel level);

    private boolean usesDefaultSettings() {
        return SkyAdditionsSettings.wanderingTraderSpawnRate == 24000
                && SkyAdditionsSettings.maxWanderingTraderSpawnChance == 0.075;
    }

    // For some reason vanilla has 2 probability guards that do nothing but makes the chance not be able to go above 0.1
    // Merging these two checks will slightly change the chance of resetting the spawn chance when no players are online
    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int skipSecondChanceCheck(RandomSource random, int bound) {
        return 100 < spawnChance ? 0 : random.nextInt(bound);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(
            ServerLevel level, boolean spawnMonsters, boolean spawnAnimals, CallbackInfoReturnable<Integer> cir) {
        // Ensure we don't make changes when the settings are default
        // It should be the same, but it's safer to just use the vanilla version
        if (usesDefaultSettings()) {
            return;
        }

        cir.setReturnValue(0);

        if (!level.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) {
            return;
        }

        // Cut the timer short if necessary
        if (SkyAdditionsSettings.wanderingTraderSpawnRate < spawnDelay) {
            spawnDelay = SkyAdditionsSettings.wanderingTraderSpawnRate;
            currentSpawnTimer = Math.min(1200, spawnDelay);
            tickDelay = currentSpawnTimer;
        }

        if (--tickDelay > 0) {
            return;
        }

        spawnDelay -= currentSpawnTimer;

        boolean trySpawn = spawnDelay <= 0;

        spawnDelay = trySpawn ? SkyAdditionsSettings.wanderingTraderSpawnRate : spawnDelay;
        currentSpawnTimer = Math.min(1200, spawnDelay);
        tickDelay = currentSpawnTimer;

        serverLevelData.setWanderingTraderSpawnDelay(spawnDelay);

        if (trySpawn && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            // Bound changed for high spawn chances b/c the 90% chance for trySpawn to fail is removed
            if (random.nextInt(100 < spawnChance ? 1000 : 100) < spawnChance && spawn(level)) {
                spawnChance = 25;
                cir.setReturnValue(1);
            } else {
                spawnChance = Mth.clamp(spawnChance + 25, 25, (int)
                        Math.round(SkyAdditionsSettings.maxWanderingTraderSpawnChance * 1000d));
            }

            serverLevelData.setWanderingTraderSpawnChance(spawnChance);
        }
    }
}
