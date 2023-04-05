package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnPlacements.class)
public interface SpawnRestrictionAccessor {
    @Invoker("register")
    static <T extends Mob> void register(
            EntityType<T> type,
            SpawnPlacements.Type location,
            Heightmap.Types heightmapType,
            SpawnPlacements.SpawnPredicate<T> predicate) {
        throw new AssertionError();
    }
}
