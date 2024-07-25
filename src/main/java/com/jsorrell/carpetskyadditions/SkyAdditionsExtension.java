package com.jsorrell.carpetskyadditions;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import com.jsorrell.carpetskyadditions.advancements.criterion.SkyAdditionsCriteriaTriggers;
import com.jsorrell.carpetskyadditions.advancements.predicates.SkyAdditionsLootItemConditions;
import com.jsorrell.carpetskyadditions.commands.SkyIslandCommand;
import com.jsorrell.carpetskyadditions.commands.SpawnTraderCommand;
import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyBlockChunkGenerator;
import com.jsorrell.carpetskyadditions.gen.feature.SkyAdditionsFeatures;
import com.jsorrell.carpetskyadditions.helpers.PiglinBruteSpawnPredicate;
import com.jsorrell.carpetskyadditions.helpers.SkyAdditionsMinecartComparatorLogic;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Map;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

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
        SpawnPlacements.register(
                EntityType.PIGLIN_BRUTE,
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                new PiglinBruteSpawnPredicate());

        Registry.register(
                BuiltInRegistries.CHUNK_GENERATOR,
                new SkyAdditionsResourceLocation("skyblock"),
                SkyBlockChunkGenerator.CODEC);
        SkyAdditionsFeatures.bootstrap();
        SkyAdditionsCriteriaTriggers.bootstrap();
        SkyAdditionsLootItemConditions.bootstrap();
        SkyAdditionsDataComponents.bootstrap();
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
            CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
        SkyIslandCommand.register(dispatcher);
        SpawnTraderCommand.register(dispatcher);
    }

    @Override
    public String version() {
        return MOD_ID + " " + MOD_VERSION;
    }
}
