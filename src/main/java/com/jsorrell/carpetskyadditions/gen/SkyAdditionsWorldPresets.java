package com.jsorrell.carpetskyadditions.gen;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;

public class SkyAdditionsWorldPresets {
  public static final RegistryKey<WorldPreset> SKYBLOCK = RegistryKey.of(RegistryKeys.WORLD_PRESET, new SkyAdditionsIdentifier("skyblock"));

  public static RegistryKey<WorldPreset> getInitiallySelectedPreset() {
    SkyAdditionsConfig config = AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
    return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.DEFAULT;
  }
}
