package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsIdentifier;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;

public class EndGatewayIslandFeature extends Feature<DefaultFeatureConfig> {
  public static final Identifier GATEWAY_ISLAND_FEATURE_ID = new SkyAdditionsIdentifier("end_gateway_island");
  public static Feature<DefaultFeatureConfig> GATEWAY_ISLAND_FEATURE = new EndGatewayIslandFeature(DefaultFeatureConfig.CODEC);
  public static RegistryKey<ConfiguredFeature<?, ?>> GATEWAY_ISLAND_FEATURE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, GATEWAY_ISLAND_FEATURE_ID);

  public EndGatewayIslandFeature(Codec<DefaultFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    if (!Feature.END_ISLAND.generate(context)) {
      return false;
    }

    StructureWorldAccess world = context.getWorld();

    int x = context.getOrigin().getX();
    int y = context.getOrigin().getY();
    int z = context.getOrigin().getZ();

    // Try to generate in a 11x11 area around the center of the island.
    // 20 tries should be more than enough, even for small islands.
    final int r = 5;
    for (BlockPos pos : BlockPos.iterateRandomly(context.getRandom(), 20, x - r, y, z - r, x + r, y, z + r)) {
      // Force not generating on edge
      if (Direction.Type.HORIZONTAL.stream().noneMatch(dir -> world.isAir(pos.offset(dir))) &&
        Feature.CHORUS_PLANT.generate(new FeatureContext<>(Optional.empty(), world, context.getGenerator(), context.getRandom(), pos.up(), FeatureConfig.DEFAULT))) {
        return true;
      }
    }
    return false;
  }

  // Finds a place to spawn a gateway that won't overwrite chorus
  // Allows a gateway that pops off chorus flowers
  public static BlockPos findGatewayLocation(WorldView world, BlockPos origin) {
    return BlockPos.streamOutwards(origin, 7, 0, 7).filter(pos ->
      world.getBlockState(pos).isOf(Blocks.END_STONE) &&
        Direction.stream().allMatch(direction -> world.isAir(pos.up(11).offset(direction))) &&
        Direction.stream().allMatch(direction -> world.isAir(pos.up(9).offset(direction)))
    ).findFirst().orElse(origin);
  }
}
