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
            ServerLevelAccessor world,
            MobSpawnType spawnReason,
            BlockPos pos,
            RandomSource random) {
        // Conditionally implement registering SpawnRestriction.Location.ON_GROUND
        if (CarpetSettings.piglinsSpawningInBastions) {
            BlockPos underBlockPos = pos.below();
            BlockState underBlock = world.getBlockState(underBlockPos);
            if (!underBlock.isValidSpawn(world, underBlockPos, type)) {
                return false;
            }
            BlockPos aboveBlockPos = pos.above();
            return NaturalSpawner.isValidEmptySpawnBlock(
                            world, pos, world.getBlockState(pos), world.getFluidState(pos), type)
                    && NaturalSpawner.isValidEmptySpawnBlock(
                            world,
                            aboveBlockPos,
                            world.getBlockState(aboveBlockPos),
                            world.getFluidState(aboveBlockPos),
                            type)
                    // Mimic piglin spawning restrictions b/c that's the closest mob
                    && Piglin.checkPiglinSpawnRules(EntityType.PIGLIN, world, spawnReason, pos, random);
        }

        return true;
    }
}
