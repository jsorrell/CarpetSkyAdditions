package com.jsorrell.carpetskyadditions;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.network.chat.Component;

public class SkyAdditionsDataPacks {
    public static final SkyAdditionsIdentifier SKYBLOCK = new SkyAdditionsIdentifier("skyblock");
    public static final SkyAdditionsIdentifier SKYBLOCK_ACACIA = new SkyAdditionsIdentifier("skyblock_acacia");

    public static void register() {
        // Add the embedded datapacks as an option on the create world screen
        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsIdentifier("skyblock"),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.skyblock"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsIdentifier("skyblock_acacia"),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.acacia"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_acacia\".");
        }
    }
}
