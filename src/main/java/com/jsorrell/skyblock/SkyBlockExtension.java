package com.jsorrell.skyblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.registry.Registry;

import com.jsorrell.skyblock.criterion.Criteria;
import com.jsorrell.skyblock.gen.SkyBlockChunkGenerator;
import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;

public class SkyBlockExtension implements CarpetExtension, ModInitializer {
  private static SettingsManager settingsManager;

  public SkyBlockExtension() {
    CarpetServer.manageExtension(this);
  }

  @Override
  public void onInitialize() {
    settingsManager = new SettingsManager(Build.VERSION, Build.ID, Build.NAME);
    settingsManager.parseSettingsClass(SkyBlockSettings.class);
    Registry.register(
        Registry.CHUNK_GENERATOR, SkyBlockGenerationSettings.NAME, SkyBlockChunkGenerator.CODEC);
    Criteria.registerAll();
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
