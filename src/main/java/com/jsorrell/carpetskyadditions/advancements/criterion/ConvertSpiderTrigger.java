package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.google.gson.JsonObject;
import java.util.Optional;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.storage.loot.LootContext;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ConvertSpiderTrigger extends SimpleCriterionTrigger<ConvertSpiderTrigger.TriggerInstance> {
    public void trigger(ServerPlayer player, Spider spider, CaveSpider caveSpider) {
        LootContext spiderLootContext = EntityPredicate.createContext(player, spider);
        LootContext caveSpiderLootContext = EntityPredicate.createContext(player, caveSpider);
        trigger(player, triggerInstance -> triggerInstance.matches(spiderLootContext, caveSpiderLootContext));
    }

    @Override
    public TriggerInstance createInstance(
            JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext context) {
        Optional<ContextAwarePredicate> spiderPredicate = EntityPredicate.fromJson(json, "spider", context);
        Optional<ContextAwarePredicate> caveSpiderPredicate = EntityPredicate.fromJson(json, "cave_spider", context);
        return new TriggerInstance(player, spiderPredicate, caveSpiderPredicate);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Optional<ContextAwarePredicate> spider;
        private final Optional<ContextAwarePredicate> caveSpider;

        public TriggerInstance(
                Optional<ContextAwarePredicate> player,
                Optional<ContextAwarePredicate> spider,
                Optional<ContextAwarePredicate> caveSpider) {
            super(player);
            this.spider = spider;
            this.caveSpider = caveSpider;
        }

        public boolean matches(LootContext spiderContext, LootContext caveSpiderContext) {
            return (spider.isEmpty() || spider.get().matches(spiderContext))
                    && (caveSpider.isEmpty() || caveSpider.get().matches(caveSpiderContext));
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonObject = super.serializeToJson();
            spider.ifPresent(s -> jsonObject.add("spider", s.toJson()));
            caveSpider.ifPresent(cs -> jsonObject.add("cave_spider", cs.toJson()));
            return jsonObject;
        }
    }
}
