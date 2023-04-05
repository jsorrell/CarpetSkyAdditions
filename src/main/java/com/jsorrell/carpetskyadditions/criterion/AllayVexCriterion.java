package com.jsorrell.carpetskyadditions.criterion;

import com.google.gson.JsonObject;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.storage.loot.LootContext;

public class AllayVexCriterion extends SimpleCriterionTrigger<AllayVexCriterion.Conditions> {
    static final ResourceLocation ID = new SkyAdditionsIdentifier("allay_vex");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, Vex vex, Allay allay) {
        LootContext vexLootContext = EntityPredicate.createContext(player, vex);
        LootContext allayLootContext = EntityPredicate.createContext(player, allay);
        this.trigger(player, conditions -> conditions.matches(vexLootContext, allayLootContext));
    }

    @Override
    public Conditions createInstance(
            JsonObject jsonObject,
            EntityPredicate.Composite playerPredicate,
            DeserializationContext advancementEntityPredicateDeserializer) {
        EntityPredicate.Composite vexPredicate =
                EntityPredicate.Composite.fromJson(jsonObject, "vex", advancementEntityPredicateDeserializer);
        EntityPredicate.Composite allayPredicate =
                EntityPredicate.Composite.fromJson(jsonObject, "allay", advancementEntityPredicateDeserializer);
        return new Conditions(playerPredicate, vexPredicate, allayPredicate);
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite vex;
        private final EntityPredicate.Composite allay;

        public Conditions(
                EntityPredicate.Composite player, EntityPredicate.Composite vex, EntityPredicate.Composite allay) {
            super(ID, player);
            this.vex = vex;
            this.allay = allay;
        }

        public boolean matches(LootContext vexContext, LootContext allayContext) {
            return this.vex.matches(vexContext) && this.allay.matches(allayContext);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("vex", this.vex.toJson(predicateSerializer));
            jsonObject.add("allay", this.allay.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}
