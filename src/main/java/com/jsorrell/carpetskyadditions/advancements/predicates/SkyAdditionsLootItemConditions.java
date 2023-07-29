package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class SkyAdditionsLootItemConditions {
    public static final LootItemConditionType DESERT_PYRAMID_EXISTS =
            register("desert_pyramid_exists", new DesertPyramidExistsCondition.Serializer());

    private static LootItemConditionType register(
            String registryName, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(
                BuiltInRegistries.LOOT_CONDITION_TYPE,
                new SkyAdditionsResourceLocation(registryName),
                new LootItemConditionType(serializer));
    }

    public static void bootstrap() {}
}
