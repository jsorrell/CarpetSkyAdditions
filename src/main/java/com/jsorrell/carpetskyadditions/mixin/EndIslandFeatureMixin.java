package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndIslandFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EndIslandFeature.class)
public abstract class EndIslandFeatureMixin extends Feature<DefaultFeatureConfig> {

  public EndIslandFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
    super(configCodec);
  }

  @Inject(
    method = "generate",
    locals = LocalCapture.CAPTURE_FAILSOFT,
    at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 1))
  private void generateChorus(
    FeatureContext<DefaultFeatureConfig> context,
    CallbackInfoReturnable<Boolean> cir,
    StructureWorldAccess world,
    Random random,
    BlockPos blockPos,
    float islandSizeF,
    int level) {
    if (SkyAdditionsSettings.gatewaysSpawnChorus) {
      if (level == 0) {
        ChunkRandom randomChorus = new ChunkRandom(new CheckedRandom(0L));
        ChunkPos chunkPos = new ChunkPos(blockPos);
        randomChorus.setPopulationSeed(world.getSeed(), chunkPos.getStartX(), chunkPos.getStartZ());
        int islandRadius = MathHelper.ceil(islandSizeF);
        int xOffset = randomChorus.nextInt(2 * islandRadius) - islandRadius;
        int farthestZ =
          MathHelper.floor(Math.sqrt((islandSizeF + 1) * (islandSizeF + 1) - xOffset * xOffset));
        int zOffset = randomChorus.nextInt(2 * farthestZ) - farthestZ;
        BlockPos chorusPos = blockPos.add(xOffset, 1, zOffset);
        Feature.CHORUS_PLANT.generate(
          new FeatureContext<>(null, world, null, randomChorus, chorusPos, null));
      }
    }
  }
}
