package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.google.common.collect.ImmutableSet;
import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsEntityPredicate;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public record SkyAdditionsLootItemEntityPropertyCondition(
        Optional<SkyAdditionsEntityPredicate> predicate, LootContext.EntityTarget entityTarget)
        implements LootItemCondition {
    public static final MapCodec<SkyAdditionsLootItemEntityPropertyCondition> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                            SkyAdditionsEntityPredicate.CODEC
                                    .optionalFieldOf("predicate")
                                    .forGetter(SkyAdditionsLootItemEntityPropertyCondition::predicate),
                            LootContext.EntityTarget.CODEC
                                    .fieldOf("entity")
                                    .forGetter(SkyAdditionsLootItemEntityPropertyCondition::entityTarget))
                    .apply(instance, SkyAdditionsLootItemEntityPropertyCondition::new));

    @Override
    public LootItemConditionType getType() {
        return SkyAdditionsLootItemConditions.ENTITY_PROPERTIES;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN, entityTarget.getParam());
    }

    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(entityTarget.getParam());
        Vec3 origin = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        return predicate.isEmpty() || predicate.get().matches(lootContext.getLevel(), origin, entity);
    }

    public static Builder hasProperties(LootContext.EntityTarget target, SkyAdditionsEntityPredicate predicate) {
        return () -> new SkyAdditionsLootItemEntityPropertyCondition(Optional.of(predicate), target);
    }
}
