package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class SkyAdditionsCriteriaTriggers {
    public static final PlayerTrigger GENERATE_GEODE = register("generate_geode", new PlayerTrigger());
    public static final ConvertSpiderTrigger CONVERT_SPIDER = register("convert_spider", new ConvertSpiderTrigger());
    public static final AllayVexTrigger ALLAY_VEX = register("allay_vex", new AllayVexTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, new SkyAdditionsResourceLocation(name), trigger);
    }

    public static void bootstrap() {}
}
