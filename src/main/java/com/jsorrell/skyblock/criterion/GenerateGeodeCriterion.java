package com.jsorrell.skyblock.criterion;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GenerateGeodeCriterion extends AbstractCriterion<GenerateGeodeCriterion.Conditions> {

  static final Identifier ID = new Identifier("skyblock:generate_geode");

  @Override
  public Identifier getId() {
    return ID;
  }

  public void trigger(ServerPlayerEntity player) {
    this.test(player, conditions -> true);
  }

  public GenerateGeodeCriterion.Conditions conditionsFromJson(
      JsonObject jsonObject,
      EntityPredicate.Extended player,
      AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
    return new GenerateGeodeCriterion.Conditions(player);
  }

  public static class Conditions extends AbstractCriterionConditions {
    public Conditions(EntityPredicate.Extended player) {
      super(GenerateGeodeCriterion.ID, player);
    }
  }
}
