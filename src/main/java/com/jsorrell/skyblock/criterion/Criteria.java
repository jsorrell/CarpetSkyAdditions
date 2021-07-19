package com.jsorrell.skyblock.criterion;

import com.jsorrell.skyblock.mixin.CriteriaAccessor;

public class Criteria {
  public static final GenerateGeodeCriterion GENERATE_GEODE = new GenerateGeodeCriterion();

  public static void registerAll() {
    CriteriaAccessor.register(GENERATE_GEODE);
  }
}
