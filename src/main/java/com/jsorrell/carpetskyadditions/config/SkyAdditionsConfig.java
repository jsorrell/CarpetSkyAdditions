package com.jsorrell.carpetskyadditions.config;

import com.jsorrell.carpetskyadditions.Build;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Build.MODID)
public class SkyAdditionsConfig implements ConfigData {
  public enum InitialTreeType {
    Oak,
    Acacia,
  }

  @ConfigEntry.Category("newWorld")
  public boolean skyBlockWorldDefault = false;

  @ConfigEntry.Category("newWorld")
  @ConfigEntry.Gui.Tooltip
  public boolean enableDatapackByDefault = false;

  @ConfigEntry.Category("newWorld")
  @ConfigEntry.Gui.Tooltip
  public InitialTreeType initialTreeType = InitialTreeType.Oak;

  @ConfigEntry.Category("newWorld")
  public boolean autoEnableDefaultSettings = true;
}
