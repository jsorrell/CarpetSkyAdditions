package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.DataConfigurationHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPropertiesHandler.class)
public class ServerPropertiesHandlerMixin {
    @Redirect(
            method = "<init>",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/gen/WorldPresets;DEFAULT:Lnet/minecraft/registry/RegistryKey;"))
    private RegistryKey<WorldPreset> setDefaultSelectedWorldPreset() {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.DEFAULT;
    }

    @Redirect(
            method = "<init>",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/resource/DataConfiguration;dataPacks()Lnet/minecraft/resource/DataPackSettings;"))
    private DataPackSettings enableSkyAdditionsDatapacks(DataConfiguration dc) {
        return DataConfigurationHelper.updateDataConfiguration(dc).dataPacks();
    }
}
