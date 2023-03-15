package com.jsorrell.carpetskyadditions.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpawnPlatformFeature extends Feature<SpawnPlatformFeatureConfig> {
  public SpawnPlatformFeature(Codec<SpawnPlatformFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(FeatureContext<SpawnPlatformFeatureConfig> context) {
    StructureWorldAccess world = context.getWorld();
    MinecraftServer server = world.getServer();
    if (server == null) {
      return false;
    }
    SpawnPlatformFeatureConfig config = context.getConfig();
    StructureTemplate structure = server.getStructureTemplateManager().getTemplate(config.structure()).orElse(null);
    if (structure == null) {
      return false;
    }

    BlockPos structureOrigin = config.pos();
    if (config.relative()) {
      // Y is always absolute
      structureOrigin = structureOrigin.add(context.getOrigin().getX(), 0, context.getOrigin().getZ());
    }

    return structure.place(world, structureOrigin, structureOrigin, new StructurePlacementData(), context.getRandom(), Block.NOTIFY_LISTENERS);
  }
}
