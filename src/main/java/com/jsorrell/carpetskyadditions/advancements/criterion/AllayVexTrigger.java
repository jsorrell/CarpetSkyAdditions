package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.storage.loot.LootContext;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AllayVexTrigger extends SimpleCriterionTrigger<AllayVexTrigger.TriggerInstance> {
    public void trigger(ServerPlayer player, Vex vex, Allay allay) {
        LootContext vexLootContext = EntityPredicate.createContext(player, vex);
        LootContext allayLootContext = EntityPredicate.createContext(player, allay);
        trigger(player, triggerInstance -> triggerInstance.matches(vexLootContext, allayLootContext));
    }

    @Override
    public TriggerInstance createInstance(
            JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext context) {
        Optional<ContextAwarePredicate> vexPredicate = EntityPredicate.fromJson(json, "vex", context);
        Optional<ContextAwarePredicate> allayPredicate = EntityPredicate.fromJson(json, "allay", context);
        return new TriggerInstance(player, vexPredicate, allayPredicate);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Optional<ContextAwarePredicate> vex;
        private final Optional<ContextAwarePredicate> allay;

        public TriggerInstance(
                Optional<ContextAwarePredicate> player,
                Optional<ContextAwarePredicate> vex,
                Optional<ContextAwarePredicate> allay) {
            super(player);
            this.vex = vex;
            this.allay = allay;
        }

        public boolean matches(LootContext vexContext, LootContext allayContext) {
            return (vex.isEmpty() || vex.get().matches(vexContext))
                    && (allay.isEmpty() || allay.get().matches(allayContext));
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonObject = super.serializeToJson();

            vex.ifPresent(v -> jsonObject.add("vex", v.toJson()));
            allay.ifPresent(a -> jsonObject.add("allay", a.toJson()));
            return jsonObject;
        }
    }
}
