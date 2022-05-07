package com.jsorrell.skyblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.jsorrell.skyblock.criterion.SkyBlockCriteria;
import com.jsorrell.skyblock.gen.SkyBlockWorldPresets;
import com.jsorrell.skyblock.helpers.PiglinBruteSpawnPredicate;
import com.jsorrell.skyblock.helpers.SkyBlockMinecartComparatorLogic;
import com.jsorrell.skyblock.mixin.SpawnRestrictionAccessor;
import com.jsorrell.skyblock.settings.SkyBlockSettings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

public class SkyBlockExtension implements CarpetExtension, ModInitializer {
  private static SettingsManager settingsManager;

  public SkyBlockExtension() {
    CarpetServer.manageExtension(this);
  }

  @Override
  public void onInitialize() {
    settingsManager = new SettingsManager(Build.VERSION, Build.ID, Build.NAME);
    settingsManager.parseSettingsClass(SkyBlockSettings.class);
    // Restrict Piglin Brute spawning when piglinsSpawningInBastions is true
    SpawnRestrictionAccessor.register(EntityType.PIGLIN_BRUTE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new PiglinBruteSpawnPredicate());
    SkyBlockWorldPresets.registerAll();
    SkyBlockCriteria.registerAll();
    MinecartComparatorLogicRegistry.register(EntityType.MINECART, new SkyBlockMinecartComparatorLogic());
  }

  @Override
  public SettingsManager customSettingsManager() {
    return settingsManager;
  }

  @Override
  public String version() {
    return Build.ID + " " + Build.VERSION;
  }
}
