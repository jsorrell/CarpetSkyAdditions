package com.jsorrell.carpetskyadditions;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.PiglinBruteSpawnPredicate;
import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsMinecartComparatorLogic;
import com.jsorrell.carpetskyadditions.mixin.SpawnRestrictionAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

import java.util.Map;

public class SkyAdditionsExtension implements CarpetExtension, ModInitializer {
  private static SettingsManager settingsManager;

  public SkyAdditionsExtension() {
    CarpetServer.manageExtension(this);
  }

  @Override
  public void onInitialize() {
    settingsManager = new SettingsManager(Build.VERSION, Build.MODID, Build.NAME);

    AutoConfig.register(SkyAdditionsConfig.class, Toml4jConfigSerializer::new);

    // Restrict Piglin Brute spawning when piglinsSpawningInBastions is true
    SpawnRestrictionAccessor.register(EntityType.PIGLIN_BRUTE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new PiglinBruteSpawnPredicate());
    SkyAdditionsWorldPresets.registerAll();
    SkyAdditionsCriteria.registerAll();
    MinecartComparatorLogicRegistry.register(EntityType.MINECART, new SkyAdditionsMinecartComparatorLogic());

    SkyAdditionsConfig config = AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();

    // Add the embedded datapacks as an option on the create world screen
    ModContainer modContainer = FabricLoader.getInstance().getModContainer(Build.MODID).get();

    if (!ResourceManagerHelper.registerBuiltinResourcePack(new SkyAdditionsIdentifier(Build.EMBEDDED_DATAPACK_NAME), modContainer, Text.of("Carpet Sky Additions: SkyBlock"), config.enableDatapackByDefault ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL)) {
      SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"" + Build.EMBEDDED_DATAPACK_NAME + "\".");
    }

    if (!ResourceManagerHelper.registerBuiltinResourcePack(new SkyAdditionsIdentifier("skyblock_acacia"), modContainer, Text.of("Carpet Sky Additions: Acacia"), config.enableDatapackByDefault && config.initialTreeType == SkyAdditionsConfig.InitialTreeType.Acacia ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL)) {
      SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_acacia\".");
    }
  }

  @Override
  public void onGameStarted() {
    settingsManager.parseSettingsClass(SkyAdditionsSettings.class);
  }

  @Override
  public SettingsManager extensionSettingsManager() {
    return settingsManager;
  }

  @Override
  public Map<String, String> canHasTranslations(String lang) {
    return Translations.getTranslationFromResourcePath(String.format("assets/%s/lang/%s.json", Build.MODID, lang));
  }

  @Override
  public String version() {
    return Build.MODID + " " + Build.VERSION;
  }
}
