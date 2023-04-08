package com.jsorrell.carpetskyadditions;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.network.chat.Component;

public class SkyAdditionsDataPacks {
    public static final SkyAdditionsResourceLocation SKYBLOCK = new SkyAdditionsResourceLocation("skyblock");
    public static final SkyAdditionsResourceLocation SKYBLOCK_ACACIA =
            new SkyAdditionsResourceLocation("skyblock_acacia");

    public static void register() {
        // Add the embedded datapacks as an option on the create world screen
        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock"),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.skyblock"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_acacia"),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.acacia"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_acacia\".");
        }
    }
}
