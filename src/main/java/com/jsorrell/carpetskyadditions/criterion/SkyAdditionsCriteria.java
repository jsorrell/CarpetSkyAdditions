package com.jsorrell.carpetskyadditions.criterion;

import net.minecraft.advancements.CriteriaTriggers;

public class SkyAdditionsCriteria {
    public static final GenerateGeodeCriterion GENERATE_GEODE = new GenerateGeodeCriterion();
    public static final ConvertSpiderCriterion CONVERT_SPIDER = new ConvertSpiderCriterion();
    public static final AllayVexCriterion ALLAY_VEX = new AllayVexCriterion();

    public static void registerAll() {
        CriteriaTriggers.register(GENERATE_GEODE);
        CriteriaTriggers.register(CONVERT_SPIDER);
        CriteriaTriggers.register(ALLAY_VEX);
    }
}
