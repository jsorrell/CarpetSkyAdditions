package com.jsorrell.carpetskyadditions;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.PiglinBruteSpawnPredicate;
import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsMinecartComparatorLogic;
import com.jsorrell.carpetskyadditions.mixin.SpawnRestrictionAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

public class SkyAdditionsExtension implements CarpetExtension, ModInitializer {
  private static SettingsManager settingsManager;

  public SkyAdditionsExtension() {
    CarpetServer.manageExtension(this);
  }

  @Override
  public void onInitialize() {
    settingsManager = new SettingsManager(Build.VERSION, Build.MODID, Build.NAME);
    settingsManager.parseSettingsClass(SkyAdditionsSettings.class);
    // Restrict Piglin Brute spawning when piglinsSpawningInBastions is true
    SpawnRestrictionAccessor.register(EntityType.PIGLIN_BRUTE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new PiglinBruteSpawnPredicate());
    SkyAdditionsWorldPresets.registerAll();
    SkyAdditionsCriteria.registerAll();
    MinecartComparatorLogicRegistry.register(EntityType.MINECART, new SkyAdditionsMinecartComparatorLogic());

    // Add the embedded datapack as an option on the create world screen
    FabricLoader.getInstance().getModContainer(Build.MODID)
      .map(container -> ResourceManagerHelper.registerBuiltinResourcePack(new SkyAdditionsIdentifier(Build.EMBEDDED_DATAPACK_NAME), container, ResourcePackActivationType.NORMAL))
      .filter(success -> !success)
      .ifPresent(success -> SkyAdditionsSettings.LOG.warn("Could not register built-in resource pack."));
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
