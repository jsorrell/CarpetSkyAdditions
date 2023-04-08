package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.datafix.SkyBlockGeneratorNameFix;
import com.jsorrell.carpetskyadditions.datafix.SkyBlockGeneratorNameFix2;
import com.jsorrell.carpetskyadditions.datafix.schemas.V3079;
import com.jsorrell.carpetskyadditions.datafix.schemas.V3106;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataFixers.class)
public abstract class SchemasMixin {
    @Inject(
            method = "addFixers",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/mojang/datafixers/DataFixerBuilder;addFixer(Lcom/mojang/datafixers/DataFix;)V",
                            ordinal = 225,
                            remap = false))
    private static void addSkyBlockGeneratorNameFix(DataFixerBuilder builder, CallbackInfo ci) {
        Schema schema3079 = builder.addSchema(3079, 1, V3079::new);
        builder.addFixer(new SkyBlockGeneratorNameFix(schema3079));
    }

    @Inject(
            method = "addFixers",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/mojang/datafixers/DataFixerBuilder;addFixer(Lcom/mojang/datafixers/DataFix;)V",
                            ordinal = 241,
                            remap = false))
    private static void addSkyBlockGeneratorNameFix2(DataFixerBuilder builder, CallbackInfo ci) {
        Schema schema3106 = builder.addSchema(3106, V3106::new);
        builder.addFixer(new SkyBlockGeneratorNameFix2(schema3106));
    }
}
