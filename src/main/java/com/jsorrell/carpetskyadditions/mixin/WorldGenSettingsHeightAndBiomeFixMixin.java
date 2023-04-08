package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.util.datafix.fixes.WorldGenSettingsHeightAndBiomeFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldGenSettingsHeightAndBiomeFix.class)
public class WorldGenSettingsHeightAndBiomeFixMixin {
    @Redirect(
            method = "method_38834(ZZLcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private static boolean datafixSkyBlock(String s1, Object s2) {
        return s1.equals(s2) || "minecraft:skyblock".equals(s2);
    }
}
