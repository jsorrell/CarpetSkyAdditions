package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsEntityPredicate;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class SkyAdditionsLootItemEntityPropertyCondition implements LootItemCondition {
    final SkyAdditionsEntityPredicate predicate;
    final LootContext.EntityTarget entityTarget;

    SkyAdditionsLootItemEntityPropertyCondition(
            SkyAdditionsEntityPredicate entityPredicate, LootContext.EntityTarget entityTarget) {
        this.predicate = entityPredicate;
        this.entityTarget = entityTarget;
    }

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
        return predicate.matches(lootContext.getLevel(), origin, entity);
    }

    public static Builder hasProperties(LootContext.EntityTarget target, SkyAdditionsEntityPredicate predicate) {
        return () -> new SkyAdditionsLootItemEntityPropertyCondition(predicate, target);
    }

    public static class Serializer
            implements net.minecraft.world.level.storage.loot.Serializer<SkyAdditionsLootItemEntityPropertyCondition> {
        public void serialize(
                JsonObject jsonObject,
                SkyAdditionsLootItemEntityPropertyCondition lootItemEntityPropertyCondition,
                JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", lootItemEntityPropertyCondition.predicate.serializeToJson());
            jsonObject.add("entity", jsonSerializationContext.serialize(lootItemEntityPropertyCondition.entityTarget));
        }

        public SkyAdditionsLootItemEntityPropertyCondition deserialize(
                JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            SkyAdditionsEntityPredicate entityPredicate =
                    SkyAdditionsEntityPredicate.fromJson(jsonObject.get("predicate"));
            return new SkyAdditionsLootItemEntityPropertyCondition(
                    entityPredicate,
                    GsonHelper.getAsObject(
                            jsonObject, "entity", jsonDeserializationContext, LootContext.EntityTarget.class));
        }
    }
}
