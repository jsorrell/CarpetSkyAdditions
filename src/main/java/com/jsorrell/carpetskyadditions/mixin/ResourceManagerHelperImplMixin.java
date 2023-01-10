package com.jsorrell.carpetskyadditions.mixin;

import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.LinkedHashSet;
import java.util.Set;

/*
builtinResourcePacks is implemented using a HashSet, meaning iteration order is arbitrary and may change from run to run.
This means there is no way to ensure the SkyBlock datapack is enabled before the alternate spawn datapacks.
We fix this my changing it to a LinkedHashSet which is iterated through by insertion order.
 */

@Mixin(ResourceManagerHelperImpl.class)
public class ResourceManagerHelperImplMixin {
  @Mutable
  @Shadow
  @Final
  private static Set<Pair<Text, ModNioResourcePack>> builtinResourcePacks;

  @Redirect(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/fabricmc/fabric/impl/resource/loader/ResourceManagerHelperImpl;builtinResourcePacks:Ljava/util/Set;"))
  private static void makeOrderedSet(Set<Pair<Text, ModNioResourcePack>> value) {
    builtinResourcePacks = new LinkedHashSet<>();
  }
}
