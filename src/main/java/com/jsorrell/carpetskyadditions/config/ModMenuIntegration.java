package com.jsorrell.carpetskyadditions.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("carpetskyadditions.config.title"));

            ConfigHolder<SkyAdditionsConfig> configHolder = AutoConfig.getConfigHolder(SkyAdditionsConfig.class);
            builder.setSavingRunnable(configHolder::save);

            SkyAdditionsConfig config = configHolder.get();

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory newWorldCategory =
                    builder.getOrCreateCategory(Text.translatable("carpetskyadditions.config.category.newWorld"));

            newWorldCategory.addEntry(entryBuilder
                    .startBooleanToggle(
                            Text.translatable("carpetskyadditions.config.option.defaultToSkyBlockWorld"),
                            config.defaultToSkyBlockWorld)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> config.defaultToSkyBlockWorld = newValue)
                    .build());

            newWorldCategory.addEntry(entryBuilder
                    .startBooleanToggle(
                            Text.translatable("carpetskyadditions.config.option.enableDatapackByDefault"),
                            config.enableDatapackByDefault)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> config.enableDatapackByDefault = newValue)
                    .build());

            newWorldCategory.addEntry(entryBuilder
                    .startEnumSelector(
                            Text.translatable("carpetskyadditions.config.option.initialTreeType"),
                            SkyAdditionsConfig.InitialTreeType.class,
                            config.getInitialTreeType())
                    .setEnumNameProvider(tree -> Text.translatable(
                            "carpetskyadditions.tree." + tree.name().toLowerCase()))
                    .setDefaultValue(SkyAdditionsConfig.InitialTreeType.OAK)
                    .setTooltip(Text.translatable("carpetskyadditions.config.option.initialTreeType.tooltip"))
                    .setSaveConsumer(newValue -> config.initialTreeType = newValue.toString())
                    .build());

            newWorldCategory.addEntry(entryBuilder
                    .startBooleanToggle(
                            Text.translatable("carpetskyadditions.config.option.autoEnableDefaultSettings"),
                            config.autoEnableDefaultSettings)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> config.autoEnableDefaultSettings = newValue)
                    .build());

            return builder.build();
        };
    }
}
