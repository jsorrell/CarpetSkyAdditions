package com.jsorrell.carpetskyadditions.helpers;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;

public class TraderSpawnerHelper {
    public static WanderingTrader spawnTrader(ServerLevel serverLevel, RandomSource random, BlockPos sourcePos) {
        PoiManager poiManager = serverLevel.getPoiManager();
        Optional<BlockPos> poiPos = poiManager.find(
                holder -> holder.is(PoiTypes.MEETING), blockPosx -> true, sourcePos, 48, PoiManager.Occupancy.ANY);
        BlockPos playerOrMeetingPos = poiPos.orElse(sourcePos);
        BlockPos spawnPos = findSpawnPositionNear(serverLevel, random, playerOrMeetingPos, 48);

        if (spawnPos == null) {
            return null;
        }

        if (serverLevel.getBiome(spawnPos).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
            return null;
        }

        if (TraderCamelHelper.tradersRideCamelsAt(serverLevel, spawnPos)) {
            if (hasEnoughSpace(serverLevel, spawnPos, true)) {
                Camel traderCamel = EntityType.CAMEL.spawn(serverLevel, spawnPos, MobSpawnType.EVENT);
                if (traderCamel != null) {
                    WanderingTrader wanderingTrader = EntityType.WANDERING_TRADER.create(serverLevel);
                    if (wanderingTrader != null) {
                        if (serverLevel.getLevelData() instanceof ServerLevelData serverLevelData) {
                            serverLevelData.setWanderingTraderId(wanderingTrader.getUUID());
                        }
                        wanderingTrader.setDespawnDelay(48000);

                        traderCamel.equipSaddle(null);
                        wanderingTrader.moveTo(
                                traderCamel.getX(),
                                traderCamel.getY(),
                                traderCamel.getZ(),
                                traderCamel.getYRot(),
                                0.0F);
                        wanderingTrader.startRiding(traderCamel, true);
                        wanderingTrader.setWanderTarget(playerOrMeetingPos);
                        wanderingTrader.restrictTo(playerOrMeetingPos, 16);
                        serverLevel.addFreshEntity(wanderingTrader);
                        return wanderingTrader;
                    }
                }
            }
        } else {
            if (hasEnoughSpace(serverLevel, spawnPos, false)) {
                WanderingTrader wanderingTrader =
                        EntityType.WANDERING_TRADER.spawn(serverLevel, spawnPos, MobSpawnType.EVENT);
                if (wanderingTrader != null) {
                    for (int j = 0; j < 2; j++) {
                        tryToSpawnLlamaFor(serverLevel, random, wanderingTrader, 4);
                    }

                    if (serverLevel.getLevelData() instanceof ServerLevelData serverLevelData) {
                        serverLevelData.setWanderingTraderId(wanderingTrader.getUUID());
                    }

                    wanderingTrader.setDespawnDelay(48000);
                    wanderingTrader.setWanderTarget(playerOrMeetingPos);
                    wanderingTrader.restrictTo(playerOrMeetingPos, 16);
                    return wanderingTrader;
                }
            }
        }

        return null;
    }

    private static void tryToSpawnLlamaFor(
            ServerLevel serverLevel, RandomSource random, WanderingTrader trader, int maxDistance) {
        BlockPos blockPos = findSpawnPositionNear(serverLevel, random, trader.blockPosition(), maxDistance);
        if (blockPos != null) {
            TraderLlama traderLlama = EntityType.TRADER_LLAMA.spawn(serverLevel, blockPos, MobSpawnType.EVENT);
            if (traderLlama != null) {
                traderLlama.setLeashedTo(trader, true);
            }
        }
    }

    @Nullable
    private static BlockPos findSpawnPositionNear(
            ServerLevel level, RandomSource random, BlockPos pos, int maxDistance) {
        BlockPos spawnPos = null;
        SpawnPlacementType spawnPlacementType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);

        for (int i = 0; i < 10; i++) {
            int j = pos.getX() + random.nextInt(maxDistance * 2) - maxDistance;
            int k = pos.getZ() + random.nextInt(maxDistance * 2) - maxDistance;
            int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (spawnPlacementType.isSpawnPositionOk(level, blockPos2, EntityType.WANDERING_TRADER)) {
                spawnPos = blockPos2;
                break;
            }
        }

        return spawnPos;
    }

    private static boolean hasEnoughSpace(BlockGetter level, BlockPos pos, boolean camelRider) {
        Iterable<BlockPos> blocks = camelRider
                ? BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 2, 1))
                : BlockPos.betweenClosed(pos, pos.offset(1, 2, 1));
        for (BlockPos blockPos : blocks) {
            if (!level.getBlockState(blockPos)
                    .getCollisionShape(level, blockPos)
                    .isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
