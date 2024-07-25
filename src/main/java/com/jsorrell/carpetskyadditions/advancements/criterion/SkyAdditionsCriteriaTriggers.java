package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.jsorrell.carpetskyadditions.mixin.CriteriaTriggersAccessor;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

public class SkyAdditionsCriteriaTriggers {
    public static final PlayerTrigger GENERATE_GEODE = register("generate_geode", new PlayerTrigger());
    public static final ConvertSpiderTrigger CONVERT_SPIDER = register("convert_spider", new ConvertSpiderTrigger());
    public static final AllayVexTrigger ALLAY_VEX = register("allay_vex", new AllayVexTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        ResourceLocation resourceLocation = new SkyAdditionsResourceLocation(name);

        if (CriteriaTriggersAccessor.getCriteria().putIfAbsent(resourceLocation, trigger) != null) {
            throw new IllegalArgumentException("Duplicate criterion id " + resourceLocation);
        } else {
            return trigger;
        }
    }

    public static void bootstrap() {}
}
