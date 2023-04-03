package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.DataConfigurationHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value = EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    // Try to reload config whenever Create New World is clicked
    @Inject(
            method = "create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
            at = @At("HEAD"))
    private static void loadConfigFromFile(MinecraftClient client, Screen parent, CallbackInfo ci) {
        AutoConfig.getConfigHolder(SkyAdditionsConfig.class).load();
    }

    @Redirect(
            method = "create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/gen/WorldPresets;DEFAULT:Lnet/minecraft/registry/RegistryKey;"))
    private static RegistryKey<WorldPreset> setDefaultSelectedWorldPreset() {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.DEFAULT;
    }

    @Redirect(
            method = "method_45686",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/gen/WorldPresets;createDemoOptions(Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/world/dimension/DimensionOptionsRegistryHolder;"))
    private static DimensionOptionsRegistryHolder setDefaultWorldGenSettings(DynamicRegistryManager drm) {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        if (config.defaultToSkyBlockWorld) {
            return drm.get(RegistryKeys.WORLD_PRESET)
                    .entryOf(SkyAdditionsWorldPresets.SKYBLOCK)
                    .value()
                    .createDimensionsRegistryHolder();
        } else {
            return WorldPresets.createDemoOptions(drm);
        }
    }

    @ModifyArg(
            method = "create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;createServerConfig(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataConfiguration;)Lnet/minecraft/server/SaveLoading$ServerConfig;"))
    private static DataConfiguration enableSkyAdditionsDatapacks(DataConfiguration dc) {
        return DataConfigurationHelper.updateDataConfiguration(dc);
    }
}
