package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(HugeMushroomFeature.class)
public class HugeMushroomFeatureMixin {
  private void generateMycelium(WorldAccess world, Random random, BlockPos pos) {
    AlterGroundTreeDecorator decorator = new AlterGroundTreeDecorator(BlockStateProvider.of(Blocks.MYCELIUM));
    decorator.generate(new TreeDecorator.Generator(world, ((blockPos, blockState) -> {
      world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
    }), random, Set.of(pos), Set.of(), Set.of()));
  }

  @Inject(method = "generate", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
  private void generateMycelium(FeatureContext<HugeMushroomFeatureConfig> context, CallbackInfoReturnable<Boolean> cir, StructureWorldAccess world, BlockPos pos, Random random) {
    if (SkyAdditionsSettings.hugeMushroomsSpreadMycelium) {
      generateMycelium(world, random, pos);
    }
  }
}
