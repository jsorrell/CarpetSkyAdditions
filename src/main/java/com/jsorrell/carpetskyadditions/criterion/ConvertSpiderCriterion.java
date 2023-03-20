package com.jsorrell.carpetskyadditions.criterion;

import com.google.gson.JsonObject;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ConvertSpiderCriterion extends AbstractCriterion<ConvertSpiderCriterion.Conditions> {
    static final Identifier ID = new SkyAdditionsIdentifier("convert_spider");

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, SpiderEntity spider, CaveSpiderEntity caveSpider) {
        LootContext spiderLootContext = EntityPredicate.createAdvancementEntityLootContext(player, spider);
        LootContext caveSpiderLootContext = EntityPredicate.createAdvancementEntityLootContext(player, caveSpider);
        this.trigger(player, conditions -> conditions.matches(spiderLootContext, caveSpiderLootContext));
    }

    public Conditions conditionsFromJson(
            JsonObject jsonObject,
            EntityPredicate.Extended playerPredicate,
            AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        EntityPredicate.Extended spiderPredicate =
                EntityPredicate.Extended.getInJson(jsonObject, "spider", advancementEntityPredicateDeserializer);
        EntityPredicate.Extended caveSpiderPredicate =
                EntityPredicate.Extended.getInJson(jsonObject, "cave_spider", advancementEntityPredicateDeserializer);
        return new Conditions(playerPredicate, spiderPredicate, caveSpiderPredicate);
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final EntityPredicate.Extended spider;
        private final EntityPredicate.Extended caveSpider;

        public Conditions(
                EntityPredicate.Extended player, EntityPredicate.Extended spider, EntityPredicate.Extended caveSpider) {
            super(ID, player);
            this.spider = spider;
            this.caveSpider = caveSpider;
        }

        public boolean matches(LootContext spiderContext, LootContext caveSpiderContext) {
            return this.spider.test(spiderContext) && this.caveSpider.test(caveSpiderContext);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("spider", this.spider.toJson(predicateSerializer));
            jsonObject.add("cave_spider", this.caveSpider.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}
