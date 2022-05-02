package com.jsorrell.skyblock.mixin;

import com.jsorrell.skyblock.gen.SkyBlockChunkGenerator;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FeaturePlacementContext.class)
public class FeaturePlacementContextMixin extends HeightContext {
  @Shadow
  @Final
  private ChunkGenerator generator;

  @Shadow
  @Final
  private StructureWorldAccess world;

  public FeaturePlacementContextMixin(ChunkGenerator generator, HeightLimitView world) {
    super(generator, world);
  }

  // Force features to use generation heightmap, not current (empty) heightmap
  @Inject(
    method = "getTopY",
    cancellable = true,
    at = @At(value = "HEAD"))
  private void useGenerationHeightmap(Heightmap.Type heightmap, int x, int z, CallbackInfoReturnable<Integer> cir) {
    if (generator instanceof SkyBlockChunkGenerator chunkGenerator) {
      NoiseConfig noiseConfig = NoiseConfig.create(chunkGenerator.getSettings().value(), world.getRegistryManager().get(Registry.NOISE_KEY), world.getSeed());
      cir.setReturnValue(chunkGenerator.getHeight(x, z, heightmap, world, noiseConfig));
      cir.cancel();
    }
  }
}
