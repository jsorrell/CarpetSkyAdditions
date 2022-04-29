package com.jsorrell.skyblock.gen;

import com.jsorrell.skyblock.SkyBlockSettings;
import com.jsorrell.skyblock.mixin.SinglePoolElementAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.BastionRemnantFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.StrongholdFeature;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomSeed;
import net.minecraft.world.gen.random.Xoroshiro128PlusPlusRandom;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkyBlockChunkGenerator extends NoiseChunkGenerator {
  private final long seed;
  private final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;

  public static final Codec<SkyBlockChunkGenerator> CODEC =
    RecordCodecBuilder.create(
      instance ->
        NoiseChunkGenerator.method_41042(instance).and(
            instance
              .group(
                RegistryOps.createRegistryCodec(
                    Registry.NOISE_WORLDGEN)
                  .forGetter(skyblockChunkGenerator -> skyblockChunkGenerator.noiseRegistry),
                BiomeSource.CODEC
                  .fieldOf("biome_source")
                  .forGetter(SkyBlockChunkGenerator::getBiomeSource),
                Codec.LONG
                  .fieldOf("seed")
                  .stable()
                  .forGetter(SkyBlockChunkGenerator::getSeed),
                ChunkGeneratorSettings.REGISTRY_CODEC
                  .fieldOf("settings")
                  .forGetter(SkyBlockChunkGenerator::getSettings)))
          .apply(instance, instance.stable(SkyBlockChunkGenerator::new)));

  public SkyBlockChunkGenerator(Registry<StructureSet> structureRegistry, Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
                                BiomeSource biomeSource, long seed, RegistryEntry<ChunkGeneratorSettings> settings) {
    super(structureRegistry, noiseRegistry, biomeSource, seed, settings);
    // Duplicate seed and noiseRegistry from super b/c they have private access
    this.seed = seed;
    this.noiseRegistry = noiseRegistry;
  }

  public long getSeed() {
    return this.seed;
  }

  public RegistryEntry<ChunkGeneratorSettings> getSettings() {
    return this.settings;
  }

  @Override
  protected Codec<? extends ChunkGenerator> getCodec() {
    return CODEC;
  }

  @Override
  public ChunkGenerator withSeed(long seed) {
    return new SkyBlockChunkGenerator(this.field_37053, this.noiseRegistry, this.biomeSource.withSeed(seed), seed, this.settings);
  }

  @Override
  public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
    if (region.getDimension().isNatural()) {
      BlockPos spawn =
        new BlockPos(
          region.getLevelProperties().getSpawnX(),
          region.getLevelProperties().getSpawnY(),
          region.getLevelProperties().getSpawnZ());
      if (chunk.getPos().getStartX() <= spawn.getX()
        && spawn.getX() <= chunk.getPos().getEndX()
        && chunk.getPos().getStartZ() <= spawn.getZ()
        && spawn.getZ() <= chunk.getPos().getEndZ()) {
        generateSpawnPlatform(
          region,
          spawn,
          new BlockBox(
            chunk.getPos().getStartX(),
            chunk.getBottomY(),
            chunk.getPos().getStartZ(),
            chunk.getPos().getStartX() + 15,
            chunk.getTopY(),
            chunk.getPos().getStartZ() + 15));
      }
    }
  }

  @Override
  public CompletableFuture<Chunk> populateNoise(
    Executor executor, Blender blender, StructureAccessor accessor, Chunk chunk) {
    return CompletableFuture.completedFuture(chunk);
  }

  @Override
  public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess access, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carver) {
  }

  // Duplicated from super b/c it has private access
  private static BlockBox getBlockBoxForChunk(Chunk chunk) {
    ChunkPos chunkPos = chunk.getPos();
    int startX = chunkPos.getStartX();
    int startZ = chunkPos.getStartZ();
    HeightLimitView heightLimitView = chunk.getHeightLimitView();
    int bottomY = heightLimitView.getBottomY() + 1;
    int topY = heightLimitView.getTopY() - 1;
    return new BlockBox(startX, bottomY, startZ, startX + 15, topY, startZ + 15);
  }

  @Override
  public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
    ChunkPos chunkPos = chunk.getPos();
    ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(chunkPos, world.getBottomSectionCoord());
    BlockPos minChunkPos = chunkSectionPos.getMinPos();

    Registry<ConfiguredStructureFeature<?, ?>> configuredStructureFeatures = world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY);
    Map<Integer, List<ConfiguredStructureFeature<?, ?>>> configuredStructureFeaturesByStep = configuredStructureFeatures.stream().collect(Collectors.groupingBy(configuredStructureFeature -> configuredStructureFeature.feature.getGenerationStep().ordinal()));
    List<BiomeSource.IndexedFeatures> indexedFeatures = this.populationSource.getIndexedFeatures();

    ChunkRandom chunkRandom = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
    long populationSeed = chunkRandom.setPopulationSeed(world.getSeed(), minChunkPos.getX(), minChunkPos.getZ());

    // Get all surrounding biomes for biome-based structures
    ObjectArraySet<Biome> biomeSet = new ObjectArraySet<>();
    ChunkPos.stream(chunkSectionPos.toChunkPos(), 1).forEach(curChunkPos -> {
      Chunk curChunk = world.getChunk(curChunkPos.x, curChunkPos.z);
      for (ChunkSection chunkSection : curChunk.getSectionArray()) {
        chunkSection.getBiomeContainer().method_39793(registryEntry -> biomeSet.add(registryEntry.value()));
      }
    });
    biomeSet.retainAll(this.populationSource.getBiomes().stream().map(RegistryEntry::value).collect(Collectors.toSet()));

    int numIndexedFeatures = indexedFeatures.size();
    try {
      Registry<PlacedFeature> placedFeatures = world.getRegistryManager().get(Registry.PLACED_FEATURE_KEY);
      int numSteps = Math.max(GenerationStep.Feature.values().length, numIndexedFeatures);
      for (int genStep = 0; genStep < numSteps; ++genStep) {
        int m = 0;
        if (structureAccessor.shouldGenerateStructures()) {
          List<ConfiguredStructureFeature<?, ?>> configuredStructureFeaturesForStep = configuredStructureFeaturesByStep.getOrDefault(genStep, Collections.emptyList());
          for (ConfiguredStructureFeature<?, ?> configuredStructureFeature : configuredStructureFeaturesForStep) {
            chunkRandom.setDecoratorSeed(populationSeed, m, genStep);
            Supplier<String> featureNameSupplier = () -> configuredStructureFeatures.getKey(configuredStructureFeature).map(Object::toString).orElseGet(configuredStructureFeature::toString);
            try {
              // Stronghold
              if (configuredStructureFeature.feature instanceof StrongholdFeature && (SkyBlockSettings.generateEndPortals || SkyBlockSettings.generateSilverfishSpawners)) {
                world.setCurrentlyGeneratingStructureName(featureNameSupplier);
                structureAccessor.getStructureStarts(chunkSectionPos, configuredStructureFeature).forEach(structureStart -> {
                  for (StructurePiece piece : structureStart.getChildren()) {
                    if (piece.intersectsChunk(chunkPos, 0) && piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                      ChunkRandom random = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
                      BlockBox chunkBox = getBlockBoxForChunk(chunk);
                      if (SkyBlockSettings.generateEndPortals) {
                        random.setCarverSeed(seed, chunkPos.x, chunkPos.z);
                        new SkyBlockStructures.EndPortalStructure(piece).generate(world, chunkBox, random);
                      }

                      if (SkyBlockSettings.generateSilverfishSpawners) {
                        new SkyBlockStructures.SilverfishSpawnerStructure(piece).generate(world, chunkBox, null);
                      }
                    }
                  }
                });
                // Bastion
              } else if (configuredStructureFeature.feature instanceof BastionRemnantFeature && SkyBlockSettings.generateMagmaCubeSpawners) {
                world.setCurrentlyGeneratingStructureName(featureNameSupplier);
                structureAccessor.getStructureStarts(chunkSectionPos, configuredStructureFeature).forEach(structureStart -> {
                  for (StructurePiece piece : structureStart.getChildren()) {
                    if (piece.intersectsChunk(chunkPos, 0) && piece instanceof PoolStructurePiece poolPiece) {
                      if (poolPiece.getPoolElement() instanceof SinglePoolElement singlePoolElement) {
                        Optional<Identifier> left = ((SinglePoolElementAccessor) singlePoolElement).getLocation().left();
                        if (left.isPresent() && left.get().equals(new Identifier("bastion/treasure/bases/lava_basin"))) {
                          new SkyBlockStructures.MagmaCubeSpawner(piece).generate(world, getBlockBoxForChunk(chunk), null);
                        }
                      }
                    }
                  }
                });
              }
            } catch (Exception e) {
              CrashReport crashReport = CrashReport.create(e, "Feature placement");
              crashReport.addElement("Feature").add("Description", featureNameSupplier::get);
              throw new CrashException(crashReport);
            }
            ++m;
          }
        }
        if (genStep >= numIndexedFeatures) continue;
        IntArraySet intSet = new IntArraySet();
        for (Biome biome : biomeSet) {
          List<RegistryEntryList<PlacedFeature>> biomeFeatureStepList = biome.getGenerationSettings().getFeatures();
          if (genStep >= biomeFeatureStepList.size()) continue;
          RegistryEntryList<PlacedFeature> biomeFeaturesForStep = biomeFeatureStepList.get(genStep);
          BiomeSource.IndexedFeatures indexedFeature = indexedFeatures.get(genStep);
          biomeFeaturesForStep.stream().map(RegistryEntry::value).forEach(placedFeature -> intSet.add(indexedFeature.indexMapping().applyAsInt(placedFeature)));
        }
        int n = intSet.size();
        int[] is = intSet.toIntArray();
        Arrays.sort(is);
        BiomeSource.IndexedFeatures indexedFeature = indexedFeatures.get(genStep);
        for (int o = 0; o < n; ++o) {
          int p = is[o];
          PlacedFeature placedFeature = indexedFeature.features().get(p);
          Supplier<String> placedFeatureNameSupplier = () -> placedFeatures.getKey(placedFeature).map(Object::toString).orElseGet(placedFeature::toString);
          chunkRandom.setDecoratorSeed(populationSeed, p, genStep);
          try {
            // Random End Gateways
            if (SkyBlockSettings.generateRandomEndGateways && placedFeature.feature().matchesId(new Identifier("end_gateway_return"))) {
              world.setCurrentlyGeneratingStructureName(placedFeatureNameSupplier);
              placedFeature.generate(world, this, chunkRandom, minChunkPos);
            }
          } catch (Exception e) {
            CrashReport crashReport = CrashReport.create(e, "Feature placement");
            crashReport.addElement("Feature").add("Description", placedFeatureNameSupplier::get);
            throw new CrashException(crashReport);
          }
        }
      }
      world.setCurrentlyGeneratingStructureName(null);
    } catch (Exception e) {
      CrashReport crashReport = CrashReport.create(e, "Biome decoration");
      crashReport.addElement("Generation").add("CenterX", chunkPos.x).add("CenterZ", chunkPos.z).add("Seed", populationSeed);
      throw new CrashException(crashReport);
    }
  }

  @Override
  public void populateEntities(ChunkRegion region) {
  }

  protected static void placeBlock(
    WorldAccess world,
    BlockState block,
    BlockPos referencePos,
    int x,
    int y,
    int z,
    BlockBox box) {
    BlockPos blockPos =
      new BlockPos(referencePos.getX() + x, referencePos.getY() + y, referencePos.getZ() + z);
    if (box.contains(blockPos)) {
      world.setBlockState(blockPos, block, Block.NOTIFY_LISTENERS);
    }
  }

  protected static void fillBlocks(
    WorldAccess world,
    BlockState block,
    BlockPos referencePos,
    int startX,
    int startY,
    int startZ,
    int endX,
    int endY,
    int endZ,
    BlockBox box) {
    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        for (int z = startZ; z <= endZ; z++) {
          placeBlock(world, block, referencePos, x, y, z, box);
        }
      }
    }
  }

  protected static void generateSpawnPlatform(
    ServerWorldAccess world, BlockPos spawnpoint, BlockBox bounds) {
    BlockState leavesBlockState = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
    fillBlocks(
      world, Blocks.GRASS_BLOCK.getDefaultState(), spawnpoint, -2, -1, -7, 2, -1, 2, bounds);
    placeBlock(world, Blocks.MYCELIUM.getDefaultState(), spawnpoint, 0, -1, 0, bounds);
    placeBlock(
      world, Blocks.CRIMSON_NYLIUM.getDefaultState(), spawnpoint, -1, -1, 1, bounds);
    placeBlock(
      world, Blocks.WARPED_NYLIUM.getDefaultState(), spawnpoint, 1, -1, 1, bounds);
    placeBlock(world, Blocks.DIRT.getDefaultState(), spawnpoint, 0, -1, -5, bounds);
    fillBlocks(world, leavesBlockState, spawnpoint, -2, 3, -7, 2, 4, -3, bounds);
    fillBlocks(world, leavesBlockState, spawnpoint, -1, 5, -5, 1, 6, -5, bounds);
    fillBlocks(world, leavesBlockState, spawnpoint, 0, 5, -6, 0, 6, -4, bounds);
    placeBlock(world, leavesBlockState, spawnpoint, -1, 5, -4, bounds);
    placeBlock(world, leavesBlockState, spawnpoint, -1, 5, -6, bounds);
    placeBlock(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 4, -3, bounds);
    placeBlock(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 3, -7, bounds);
    fillBlocks(
      world, Blocks.OAK_LOG.getDefaultState(), spawnpoint, 0, 0, -5, 0, 5, -5, bounds);
  }
}
