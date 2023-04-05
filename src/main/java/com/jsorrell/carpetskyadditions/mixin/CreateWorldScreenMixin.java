package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.DataConfigurationHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value = EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    // Try to reload config whenever Create New World is clicked
    @Inject(
            method = "openFresh(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At("HEAD"))
    private static void loadConfigFromFile(Minecraft client, Screen parent, CallbackInfo ci) {
        AutoConfig.getConfigHolder(SkyAdditionsConfig.class).load();
    }

    @Redirect(
            method = "openFresh(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/level/levelgen/presets/WorldPresets;NORMAL:Lnet/minecraft/resources/ResourceKey;"))
    private static ResourceKey<WorldPreset> setDefaultSelectedWorldPreset() {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.NORMAL;
    }

    @Redirect(
            method = "method_45686",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/levelgen/presets/WorldPresets;createNormalWorldDimensions(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/level/levelgen/WorldDimensions;"))
    private static WorldDimensions setDefaultWorldGenSettings(RegistryAccess drm) {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        if (config.defaultToSkyBlockWorld) {
            return drm.registryOrThrow(Registries.WORLD_PRESET)
                    .getHolderOrThrow(SkyAdditionsWorldPresets.SKYBLOCK)
                    .value()
                    .createWorldDimensions();
        } else {
            return WorldPresets.createNormalWorldDimensions(drm);
        }
    }

    @ModifyArg(
            method = "openFresh(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createDefaultLoadConfig(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;)Lnet/minecraft/server/WorldLoader$InitConfig;"))
    private static WorldDataConfiguration enableSkyAdditionsDatapacks(WorldDataConfiguration dc) {
        return DataConfigurationHelper.updateDataConfiguration(dc);
    }
}
