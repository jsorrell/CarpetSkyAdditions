package com.jsorrell.skyblock.criterion;

import net.minecraft.advancement.criterion.Criteria;

public class SkyBlockCriteria {
  public static final GenerateGeodeCriterion GENERATE_GEODE = new GenerateGeodeCriterion();
  public static final ConvertSpiderCriterion CONVERT_SPIDER = new ConvertSpiderCriterion();
  public static final AllayVexCriterion ALLAY_VEX = new AllayVexCriterion();

  public static void registerAll() {
    Criteria.register(GENERATE_GEODE);
    Criteria.register(CONVERT_SPIDER);
    Criteria.register(ALLAY_VEX);
  }
}
