package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.SkyAdditionsDataPacks;
import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import java.util.ArrayList;
import java.util.List;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;

public class DataConfigurationHelper {
    public static WorldDataConfiguration updateDataConfiguration(WorldDataConfiguration dc) {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        if (config.enableDatapackByDefault) {
            List<String> enabled = new ArrayList<>(dc.dataPacks().getEnabled());
            List<String> disabled = new ArrayList<>(dc.dataPacks().getDisabled());

            String skyBlock = SkyAdditionsDataPacks.SKYBLOCK.toString();
            String acacia = SkyAdditionsDataPacks.SKYBLOCK_ACACIA.toString();

            if (!enabled.contains(skyBlock)) {
                enabled.add(skyBlock);
                disabled.remove(skyBlock);
            }

            if (config.getInitialTreeType() == SkyAdditionsConfig.InitialTreeType.ACACIA) {
                if (!enabled.contains(acacia)) {
                    enabled.add(acacia);
                    disabled.remove(acacia);
                }
            }
            return new WorldDataConfiguration(new DataPackConfig(enabled, disabled), dc.enabledFeatures());
        }
        return dc;
    }
}
