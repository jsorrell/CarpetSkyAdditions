package com.jsorrell.carpetskyadditions;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import com.jsorrell.carpetskyadditions.commands.SkyIslandCommand;
import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.criterion.SkyAdditionsCriteria;
import com.jsorrell.carpetskyadditions.gen.SkyBlockChunkGenerator;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsFeatures;
import com.jsorrell.carpetskyadditions.helpers.PiglinBruteSpawnPredicate;
import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsMinecartComparatorLogic;
import com.jsorrell.carpetskyadditions.mixin.SpawnRestrictionAccessor;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Map;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.Heightmap;

public class SkyAdditionsExtension implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "carpetskyadditions";
    public static final ModContainer MOD_CONTAINER =
            FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
    public static final String MOD_VERSION =
            MOD_CONTAINER.getMetadata().getVersion().toString();
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();

    private static SettingsManager settingsManager;

    public SkyAdditionsExtension() {
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onInitialize() {
        settingsManager = new SettingsManager(MOD_VERSION, MOD_ID, MOD_NAME);

        AutoConfig.register(SkyAdditionsConfig.class, Toml4jConfigSerializer::new);

        // Restrict Piglin Brute spawning when piglinsSpawningInBastions is true
        SpawnRestrictionAccessor.register(
                EntityType.PIGLIN_BRUTE,
                SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                new PiglinBruteSpawnPredicate());

        Registry.register(
                Registries.CHUNK_GENERATOR, new SkyAdditionsIdentifier("skyblock"), SkyBlockChunkGenerator.CODEC);
        SkyAdditionsFeatures.registerAll();
        SkyAdditionsCriteria.registerAll();
        MinecartComparatorLogicRegistry.register(EntityType.MINECART, new SkyAdditionsMinecartComparatorLogic());
        SkyAdditionsDataPacks.register();
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
        return Translations.getTranslationFromResourcePath(
                String.format("assets/%s/carpet/lang/%s.json", MOD_ID, lang));
    }

    @Override
    public void registerCommands(
            CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext) {
        SkyIslandCommand.register(dispatcher);
    }

    @Override
    public String version() {
        return MOD_ID + " " + MOD_VERSION;
    }
}
