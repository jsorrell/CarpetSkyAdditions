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
import com.jsorrell.skyblock.util.SkyBlockIdentifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
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
    settingsManager = new SettingsManager(Build.VERSION, Build.MODID, Build.NAME);
    settingsManager.parseSettingsClass(SkyBlockSettings.class);
    // Restrict Piglin Brute spawning when piglinsSpawningInBastions is true
    SpawnRestrictionAccessor.register(EntityType.PIGLIN_BRUTE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new PiglinBruteSpawnPredicate());
    SkyBlockWorldPresets.registerAll();
    SkyBlockCriteria.registerAll();
    MinecartComparatorLogicRegistry.register(EntityType.MINECART, new SkyBlockMinecartComparatorLogic());

    // Add the embedded datapack as an option on the create world screen
    FabricLoader.getInstance().getModContainer(Build.MODID)
      .map(container -> ResourceManagerHelper.registerBuiltinResourcePack(new SkyBlockIdentifier(Build.EMBEDDED_DATAPACK_NAME), container, ResourcePackActivationType.NORMAL))
      .filter(success -> !success)
      .ifPresent(success -> SkyBlockSettings.LOG.warn("Could not register built-in resource pack."));
  }

  @Override
  public SettingsManager customSettingsManager() {
    return settingsManager;
  }

  @Override
  public String version() {
    return Build.MODID + " " + Build.VERSION;
  }
}
