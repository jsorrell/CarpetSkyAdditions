package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class SkyAdditionsLootItemConditions {
    public static final LootItemConditionType LOCATION_CHECK =
            register("location_check", SkyAdditionsLocationCheck.CODEC);
    public static final LootItemConditionType ENTITY_PROPERTIES =
            register("entity_properties", SkyAdditionsLootItemEntityPropertyCondition.CODEC);

    private static LootItemConditionType register(String registryName, Codec<? extends LootItemCondition> codec) {
        return Registry.register(
                BuiltInRegistries.LOOT_CONDITION_TYPE,
                new SkyAdditionsResourceLocation(registryName),
                new LootItemConditionType(codec));
    }

    public static void bootstrap() {}
}
