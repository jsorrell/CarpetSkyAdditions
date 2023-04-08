package com.jsorrell.carpetskyadditions.helpers;

import carpet.CarpetSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class PiglinBruteSpawnPredicate implements SpawnPlacements.SpawnPredicate<PiglinBrute> {
    @Override
    public boolean test(
            EntityType<PiglinBrute> type,
            ServerLevelAccessor level,
            MobSpawnType spawnReason,
            BlockPos pos,
            RandomSource random) {
        // Conditionally implement registering SpawnRestriction.Location.ON_GROUND
        if (CarpetSettings.piglinsSpawningInBastions) {
            BlockPos underBlockPos = pos.below();
            BlockState underBlock = level.getBlockState(underBlockPos);
            if (!underBlock.isValidSpawn(level, underBlockPos, type)) {
                return false;
            }
            BlockPos aboveBlockPos = pos.above();
            return NaturalSpawner.isValidEmptySpawnBlock(
                            level, pos, level.getBlockState(pos), level.getFluidState(pos), type)
                    && NaturalSpawner.isValidEmptySpawnBlock(
                            level,
                            aboveBlockPos,
                            level.getBlockState(aboveBlockPos),
                            level.getFluidState(aboveBlockPos),
                            type)
                    // Mimic piglin spawning restrictions b/c that's the closest mob
                    && Piglin.checkPiglinSpawnRules(EntityType.PIGLIN, level, spawnReason, pos, random);
        }

        return true;
    }
}
