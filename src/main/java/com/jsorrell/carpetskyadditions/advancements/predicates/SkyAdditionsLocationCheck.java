package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsLocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class SkyAdditionsLocationCheck implements LootItemCondition {
    final SkyAdditionsLocationPredicate predicate;
    final BlockPos offset;

    SkyAdditionsLocationCheck(SkyAdditionsLocationPredicate locationPredicate, BlockPos offset) {
        this.predicate = locationPredicate;
        this.offset = offset;
    }

    @Override
    public LootItemConditionType getType() {
        return SkyAdditionsLootItemConditions.LOCATION_CHECK;
    }

    public boolean test(LootContext lootContext) {
        Vec3 origin = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        return origin != null
                && predicate.matches(
                        lootContext.getLevel(),
                        origin.x() + offset.getX(),
                        origin.y() + offset.getY(),
                        origin.z() + offset.getZ());
    }

    public static Builder checkLocation(SkyAdditionsLocationPredicate locationPredicate) {
        return () -> new SkyAdditionsLocationCheck(locationPredicate, BlockPos.ZERO);
    }

    public static class Serializer
            implements net.minecraft.world.level.storage.loot.Serializer<SkyAdditionsLocationCheck> {
        public void serialize(
                JsonObject jsonObject,
                SkyAdditionsLocationCheck locationCheck,
                JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", locationCheck.predicate.serializeToJson());
            if (locationCheck.offset.getX() != 0) {
                jsonObject.addProperty("offsetX", locationCheck.offset.getX());
            }

            if (locationCheck.offset.getY() != 0) {
                jsonObject.addProperty("offsetY", locationCheck.offset.getY());
            }

            if (locationCheck.offset.getZ() != 0) {
                jsonObject.addProperty("offsetZ", locationCheck.offset.getZ());
            }
        }

        public SkyAdditionsLocationCheck deserialize(
                JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            SkyAdditionsLocationPredicate locationPredicate =
                    SkyAdditionsLocationPredicate.fromJson(jsonObject.get("predicate"));
            int i = GsonHelper.getAsInt(jsonObject, "offsetX", 0);
            int j = GsonHelper.getAsInt(jsonObject, "offsetY", 0);
            int k = GsonHelper.getAsInt(jsonObject, "offsetZ", 0);
            return new SkyAdditionsLocationCheck(locationPredicate, new BlockPos(i, j, k));
        }
    }
}
