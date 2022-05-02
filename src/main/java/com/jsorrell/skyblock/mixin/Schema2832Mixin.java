package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.helpers.DuplicateMapKey;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.datafixer.schema.Schema2832;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(Schema2832.class)
public class Schema2832Mixin {
  @ModifyArg(method = "method_38837", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/DSL;taggedChoiceLazy(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/templates/TaggedChoice;"), index = 2)
  private static Map<String, Supplier<TypeTemplate>> addSkyblock(Map<String, Supplier<TypeTemplate>> templates) {
    return DuplicateMapKey.duplicateMapKey("minecraft:noise", "minecraft:skyblock", templates);
  }
}
