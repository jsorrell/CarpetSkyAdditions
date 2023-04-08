package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractHugeMushroomFeature.class)
public class AbstractHugeMushroomFeatureMixin {
    private void generateMycelium(LevelAccessor level, RandomSource random, BlockPos pos) {
        AlterGroundDecorator decorator = new AlterGroundDecorator(BlockStateProvider.simple(Blocks.MYCELIUM));
        decorator.place(new TreeDecorator.Context(
                level,
                (blockPos, blockState) -> level.setBlock(blockPos, blockState, Block.UPDATE_ALL),
                random,
                Set.of(pos),
                Set.of(),
                Set.of()));
    }

    @Inject(method = "place", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void generateMycelium(
            FeaturePlaceContext<HugeMushroomFeatureConfiguration> context,
            CallbackInfoReturnable<Boolean> cir,
            WorldGenLevel level,
            BlockPos pos,
            RandomSource random) {
        if (SkyAdditionsSettings.hugeMushroomsSpreadMycelium) {
            generateMycelium(level, random, pos);
        }
    }
}
