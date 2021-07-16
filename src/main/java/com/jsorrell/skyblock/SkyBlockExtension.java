package com.jsorrell.skyblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.registry.Registry;

import com.jsorrell.skyblock.gen.SkyBlockChunkGenerator;
import com.jsorrell.skyblock.gen.SkyBlockGenerationSettings;
import com.jsorrell.skyblock.gen.SkyBlockGeneratorTypes;
import com.jsorrell.skyblock.mixin.GeneratorTypeAccessor;

public class SkyBlockExtension implements CarpetExtension, ModInitializer, ClientModInitializer {
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
  }

  @Override
  public void onInitializeClient() {
    GeneratorTypeAccessor.getValues().add(SkyBlockGeneratorTypes.SKYBLOCK);
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
