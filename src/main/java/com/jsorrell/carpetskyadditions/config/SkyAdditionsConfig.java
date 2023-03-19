package com.jsorrell.carpetskyadditions.config;

import com.jsorrell.carpetskyadditions.Build;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Build.MODID)
public class SkyAdditionsConfig implements ConfigData {
    public enum InitialTreeType {
        OAK,
        ACACIA,
    }

    public boolean defaultToSkyBlockWorld = false;
    public boolean enableDatapackByDefault = false;
    public InitialTreeType initialTreeType = InitialTreeType.OAK;
    public boolean autoEnableDefaultSettings = true;
}
