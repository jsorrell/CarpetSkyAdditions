package com.jsorrell.carpetskyadditions.criterion;

import com.google.gson.JsonObject;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AllayVexCriterion extends AbstractCriterion<AllayVexCriterion.Conditions> {
  static final Identifier ID = new SkyAdditionsIdentifier("allay_vex");

  @Override
  public Identifier getId() {
    return ID;
  }

  public void trigger(ServerPlayerEntity player, VexEntity vex, AllayEntity allay) {
    LootContext vexLootContext = EntityPredicate.createAdvancementEntityLootContext(player, vex);
    LootContext allayLootContext = EntityPredicate.createAdvancementEntityLootContext(player, allay);
    this.trigger(player, conditions -> conditions.matches(vexLootContext, allayLootContext));
  }

  public Conditions conditionsFromJson(
    JsonObject jsonObject,
    EntityPredicate.Extended playerPredicate,
    AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
    EntityPredicate.Extended vexPredicate = EntityPredicate.Extended.getInJson(jsonObject, "vex", advancementEntityPredicateDeserializer);
    EntityPredicate.Extended allayPredicate = EntityPredicate.Extended.getInJson(jsonObject, "allay", advancementEntityPredicateDeserializer);
    return new Conditions(playerPredicate, vexPredicate, allayPredicate);
  }

  public static class Conditions extends AbstractCriterionConditions {
    private final EntityPredicate.Extended vex;
    private final EntityPredicate.Extended allay;

    public Conditions(EntityPredicate.Extended player, EntityPredicate.Extended vex, EntityPredicate.Extended allay) {
      super(ID, player);
      this.vex = vex;
      this.allay = allay;
    }

    public boolean matches(LootContext vexContext, LootContext allayContext) {
      return this.vex.test(vexContext) && this.allay.test(allayContext);
    }

    @Override
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
      JsonObject jsonObject = super.toJson(predicateSerializer);
      jsonObject.add("vex", this.vex.toJson(predicateSerializer));
      jsonObject.add("allay", this.allay.toJson(predicateSerializer));
      return jsonObject;
    }
  }
}
