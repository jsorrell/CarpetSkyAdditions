package com.jsorrell.carpetskyadditions;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;

public class SkyAdditionsDataPacks {
    public static final SkyAdditionsIdentifier SKYBLOCK = new SkyAdditionsIdentifier("skyblock");
    public static final SkyAdditionsIdentifier SKYBLOCK_ACACIA = new SkyAdditionsIdentifier("skyblock_acacia");

    public static void register() {
        // Add the embedded datapacks as an option on the create world screen
        ModContainer modContainer =
                FabricLoader.getInstance().getModContainer(Build.MODID).orElseThrow();

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsIdentifier("skyblock"),
                modContainer,
                Text.translatable("datapack.carpetskyadditions.skyblock"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsIdentifier("skyblock_acacia"),
                modContainer,
                Text.translatable("datapack.carpetskyadditions.acacia"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_acacia\".");
        }
    }
}
