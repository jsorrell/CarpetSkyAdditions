package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DuplicateMapKey;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import net.minecraft.util.datafix.fixes.MissingDimensionFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MissingDimensionFix.class)
public abstract class MissingDimensionFixMixin extends DataFix {
    public MissingDimensionFixMixin(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @ModifyArg(
            method = "makeRule",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/mojang/datafixers/DSL;taggedChoiceType(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/Type;",
                            ordinal = 1),
            index = 2,
            remap = false)
    private Map<String, Type<?>> modifyTypes(Map<String, Type<?>> types) {
        return DuplicateMapKey.duplicateMapKey("minecraft:noise", "minecraft:skyblock", types);
    }
}
