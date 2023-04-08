package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import com.jsorrell.carpetskyadditions.gen.SkyAdditionsWorldPresets;
import com.jsorrell.carpetskyadditions.helpers.DataConfigurationHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DedicatedServerProperties.class)
public class ServerPropertiesHandlerMixin {
    @Redirect(
            method = "<init>",
            at =
                    @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target =
                                    "Lnet/minecraft/world/level/levelgen/presets/WorldPresets;NORMAL:Lnet/minecraft/resources/ResourceKey;"))
    private ResourceKey<WorldPreset> setDefaultSelectedWorldPreset() {
        SkyAdditionsConfig config =
                AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        return config.defaultToSkyBlockWorld ? SkyAdditionsWorldPresets.SKYBLOCK : WorldPresets.NORMAL;
    }

    @Redirect(
            method = "<init>",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/WorldDataConfiguration;dataPacks()Lnet/minecraft/world/level/DataPackConfig;"))
    private DataPackConfig enableSkyAdditionsDatapacks(WorldDataConfiguration dc) {
        return DataConfigurationHelper.updateDataConfiguration(dc).dataPacks();
    }
}
