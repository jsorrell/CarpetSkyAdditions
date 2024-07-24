package com.jsorrell.carpetskyadditions.advancements.criterion;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class SkyAdditionsCriteriaTriggers {
    public static final PlayerTrigger GENERATE_GEODE =
            CriteriaTriggers.register(new PlayerTrigger(new SkyAdditionsResourceLocation("generate_geode")));
    public static final ConvertSpiderTrigger CONVERT_SPIDER = CriteriaTriggers.register(new ConvertSpiderTrigger());
    public static final AllayVexTrigger ALLAY_VEX = CriteriaTriggers.register(new AllayVexTrigger());

    public static void bootstrap() {}
}
