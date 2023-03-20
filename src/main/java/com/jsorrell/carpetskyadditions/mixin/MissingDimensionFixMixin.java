package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DuplicateMapKey;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import net.minecraft.datafixer.fix.MissingDimensionFix;
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
                                    "Lcom/mojang/datafixers/types/templates/TaggedChoice$TaggedChoiceType;<init>(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)V",
                            ordinal = 0),
            index = 2,
            remap = false)
    private Map<String, Type<?>> modifyTypes(Map<String, Type<?>> types) {
        return DuplicateMapKey.duplicateMapKey("minecraft:noise", "minecraft:skyblock", types);
    }
}
