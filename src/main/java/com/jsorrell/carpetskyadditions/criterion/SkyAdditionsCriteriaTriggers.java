package com.jsorrell.carpetskyadditions.criterion;

import net.minecraft.advancements.CriteriaTriggers;

public class SkyAdditionsCriteriaTriggers {
    public static final GenerateGeodeTrigger GENERATE_GEODE = new GenerateGeodeTrigger();
    public static final ConvertSpiderTrigger CONVERT_SPIDER = new ConvertSpiderTrigger();
    public static final AllayVexTrigger ALLAY_VEX = new AllayVexTrigger();

    public static void registerAll() {
        CriteriaTriggers.register(GENERATE_GEODE);
        CriteriaTriggers.register(CONVERT_SPIDER);
        CriteriaTriggers.register(ALLAY_VEX);
    }
}
