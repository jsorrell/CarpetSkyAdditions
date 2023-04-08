package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DuplicateMapKey;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.schemas.V2832;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(V2832.class)
public class SchemaV2832Mixin {
    @ModifyArg(
            method = "method_38837",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/mojang/datafixers/DSL;taggedChoiceLazy(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/templates/TaggedChoice;"),
            index = 2,
            remap = false)
    private static Map<String, Supplier<TypeTemplate>> addSkyblock(Map<String, Supplier<TypeTemplate>> templates) {
        return DuplicateMapKey.duplicateMapKey("minecraft:noise", "minecraft:skyblock", templates);
    }
}
