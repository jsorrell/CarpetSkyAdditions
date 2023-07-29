package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.TraderCamelHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderSpawnerMixin {
    @Unique
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

    @Unique
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

    @Unique
    private boolean hasEnoughSpace(BlockGetter level, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 2, 1))) {
            if (!level.getBlockState(blockPos)
                    .getCollisionShape(level, blockPos)
                    .isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Inject(
            method = "spawn",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;hasEnoughSpace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void spawnTrader(
            ServerLevel serverLevel,
            CallbackInfoReturnable<Boolean> cir,
            Player player,
            BlockPos playerPos,
            int i,
            PoiManager poiManager,
            Optional<BlockPos> optional,
            BlockPos playerOrMeetingPos,
            BlockPos spawnPos) {
        if (!TraderCamelHelper.tradersRideCamelsAt(serverLevel, spawnPos)) {
            return;
        }

        if (hasEnoughSpace(serverLevel, spawnPos)) {
            if (serverLevel.getBiome(spawnPos).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                cir.setReturnValue(false);
                return;
            }

            Camel traderCamel = EntityType.CAMEL.spawn(serverLevel, spawnPos, MobSpawnType.EVENT);
            if (traderCamel != null) {
                WanderingTrader wanderingTrader = EntityType.WANDERING_TRADER.create(serverLevel);
                if (wanderingTrader != null) {
                    serverLevelData.setWanderingTraderId(wanderingTrader.getUUID());
                    wanderingTrader.setDespawnDelay(48000);

                    traderCamel.equipSaddle(null);
                    wanderingTrader.moveTo(
                            traderCamel.getX(), traderCamel.getY(), traderCamel.getZ(), traderCamel.getYRot(), 0.0F);
                    wanderingTrader.startRiding(traderCamel, true);
                    wanderingTrader.setWanderTarget(playerOrMeetingPos);
                    wanderingTrader.restrictTo(playerOrMeetingPos, 16);
                    serverLevel.addFreshEntity(wanderingTrader);
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(false);
            }
        }
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
