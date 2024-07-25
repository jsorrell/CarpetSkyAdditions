package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.storage.loot.LootContext;

public class ConvertSpiderTrigger extends SimpleCriterionTrigger<ConvertSpiderTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Spider spider, CaveSpider caveSpider) {
        LootContext spiderLootContext = EntityPredicate.createContext(player, spider);
        LootContext caveSpiderLootContext = EntityPredicate.createContext(player, caveSpider);
        trigger(player, triggerInstance -> triggerInstance.matches(spiderLootContext, caveSpiderLootContext));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<ContextAwarePredicate> spider,
            Optional<ContextAwarePredicate> caveSpider)
            implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("spider")
                                .forGetter(TriggerInstance::spider),
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("cave_spider")
                                .forGetter(TriggerInstance::caveSpider))
                .apply(instance, TriggerInstance::new));

        public boolean matches(LootContext spiderContext, LootContext caveSpiderContext) {
            return (spider.isEmpty() || spider.get().matches(spiderContext))
                    && (caveSpider.isEmpty() || caveSpider.get().matches(caveSpiderContext));
        }
    }
}
