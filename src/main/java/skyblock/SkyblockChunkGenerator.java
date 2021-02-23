package skyblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.fluid.FluidState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

// Normally, this will always fail validation and give warning on load.
// This warning is disabled through a mixin.
// See validation method DimensionOptions.method_29567
public class SkyblockChunkGenerator extends ChunkGenerator {
    private final long seed;
    private final Supplier<ChunkGeneratorSettings> settings;

    // We reference this because SurfaceChunkGenerator final and so can't subclass it
    private final NoiseChunkGenerator reference;

    public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("seed").stable().forGetter(SkyblockChunkGenerator::getSeed),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(SkyblockChunkGenerator::getBiomeSource),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(SkyblockChunkGenerator::getSettings)
    ).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

    public SkyblockChunkGenerator(long seed, BiomeSource biomeSource, Supplier<ChunkGeneratorSettings> settings) {
        super(biomeSource, biomeSource, settings.get().getStructuresConfig(), seed);
        this.seed = seed;
        this.settings = settings;
        this.reference = new NoiseChunkGenerator(biomeSource, seed, settings);
    }

    public long getSeed() {
        return this.seed;
    }

    public Supplier<ChunkGeneratorSettings> getSettings() {
        return this.settings;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new SkyblockChunkGenerator(seed, this.biomeSource.withSeed(seed), this.settings);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        Arrays.fill(chunk.getSectionArray(), WorldChunk.EMPTY_SECTION);

        if (region.getDimension().isNatural()) {
            BlockPos spawn = new BlockPos(region.getLevelProperties().getSpawnX(), region.getLevelProperties().getSpawnY(), region.getLevelProperties().getSpawnZ());
            if (chunk.getPos().getStartX() <= spawn.getX() && spawn.getX() <= chunk.getPos().getEndX()
                    && chunk.getPos().getStartZ() <= spawn.getZ() && spawn.getZ() <= chunk.getPos().getEndZ()) {
                generateSpawnPlatformInBox(region, spawn, new BlockBox(chunk.getPos().getStartX(), 0, chunk.getPos().getStartZ(), chunk.getPos().getStartX() + 15, region.getHeight(), chunk.getPos().getStartZ() + 15));
            }
        }
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return reference.getHeight(x, z, heightmapType);
    }

    @Override
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        return reference.getEntitySpawnList(biome, accessor, group, pos);
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        return new BlockView() {
            @Nullable
            @Override
            public BlockEntity getBlockEntity(BlockPos pos) {
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos) {
                return Blocks.AIR.getDefaultState();
            }

            @Override
            public FluidState getFluidState(BlockPos pos) {
                return null;
            }
        };
    }

    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        ChunkPos chunkPos = new ChunkPos(region.getCenterChunkX(), region.getCenterChunkZ());
        BlockPos pos = new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ());

        accessor.getStructuresWithChildren(ChunkSectionPos.from(pos), Registry.STRUCTURE_FEATURE.get(new Identifier("minecraft:stronghold"))).forEach((structureStart) -> {
            for (StructurePiece piece : structureStart.getChildren()) {
                if (piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                    BlockPos portalPos = new BlockPos(piece.getBoundingBox().minX, piece.getBoundingBox().minY, piece.getBoundingBox().minZ);
                    if (piece.intersectsChunk(chunkPos, 0)) {
                        BlockBox box = new BlockBox(chunkPos.getStartX(), 0, chunkPos.getStartZ(), chunkPos.getStartX() + 15, region.getHeight(), chunkPos.getStartZ() + 15);

                        ChunkRandom random = new ChunkRandom();
                        random.setCarverSeed(seed, region.getCenterChunkX(), region.getCenterChunkZ());
                        generateStrongholdPortalInBox(region, portalPos, random, piece.getFacing(), box);
                    }
                }
            }
        });
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    protected static void placeRelativeBlockInBox(WorldAccess world, BlockState block, BlockPos referencePos, int x, int y, int z, BlockBox box) {
        BlockPos blockPos = new BlockPos(referencePos.getX() + x, referencePos.getY() + y, referencePos.getZ() + z);
        if (box.contains(blockPos)) {
            world.setBlockState(blockPos, block, 2);
        }
    }

    protected static void fillRelativeBlockInBox(WorldAccess world, BlockState block, BlockPos referencePos, int startX, int startY, int startZ, int endX, int endY, int endZ, BlockBox box) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    placeRelativeBlockInBox(world, block, referencePos, x, y, z, box);
                }
            }
        }
    }

    private static void generateStrongholdPortalInBox(ServerWorldAccess world, BlockPos pos, Random random, Direction facing, BlockBox box) {
        boolean completePortal = true;
        boolean[] eyes = new boolean[12];

        for (int i = 0; i < eyes.length; ++i) {
            eyes[i] = random.nextFloat() > 0.9F;
            completePortal &= eyes[i];
        }

        BlockState nearPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, facing);
        BlockState farPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, facing.getOpposite());
        BlockState rightPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, facing.rotateYCounterclockwise());
        BlockState leftPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, facing.rotateYClockwise());

        BlockPos spawnerPos;

        switch (facing) {
            case EAST:
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[0]), pos, 8, 3, 6, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[1]), pos, 8, 3, 5, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[2]), pos, 8, 3, 4, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[3]), pos, 12, 3, 6, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[4]), pos, 12, 3, 5, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[5]), pos, 12, 3, 4, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[6]), pos, 9, 3, 7, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[7]), pos, 10, 3, 7, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[8]), pos, 11, 3, 7, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[9]), pos, 9, 3, 3, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[10]), pos, 10, 3, 3, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 11, 3, 3, box);
                if (completePortal) {
                    BlockState endPortal = Blocks.END_PORTAL.getDefaultState();
                    fillRelativeBlockInBox(world, endPortal, pos, 9, 4, 9, 11, 6, 11, box);
                }
                spawnerPos = pos.add(6, 3, 5);
                break;

            case NORTH:
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[0]), pos, 6, 3, 7, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[1]), pos, 5, 3, 7, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[2]), pos, 4, 3, 7, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[3]), pos, 6, 3, 3, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[4]), pos, 5, 3, 3, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[5]), pos, 4, 3, 3, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[6]), pos, 7, 3, 6, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[7]), pos, 7, 3, 5, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[8]), pos, 7, 3, 4, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[9]), pos, 3, 3, 6, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[10]), pos, 3, 3, 5, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 3, 3, 4, box);
                if (completePortal) {
                    BlockState endPortal = Blocks.END_PORTAL.getDefaultState();
                    fillRelativeBlockInBox(world, endPortal, pos, 4, 3, 4, 6, 3, 6, box);
                }
                spawnerPos = pos.add(5, 3, 9);
                break;

            case WEST:
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[0]), pos, 7, 3, 4, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[1]), pos, 7, 3, 5, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[2]), pos, 7, 3, 6, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[3]), pos, 3, 3, 4, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[4]), pos, 3, 3, 5, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[5]), pos, 3, 3, 6, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[6]), pos, 6, 3, 3, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[7]), pos, 5, 3, 3, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[8]), pos, 4, 3, 3, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[9]), pos, 6, 3, 7, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[10]), pos, 5, 3, 7, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 4, 3, 7, box);
                if (completePortal) {
                    BlockState endPortal = Blocks.END_PORTAL.getDefaultState();
                    fillRelativeBlockInBox(world, endPortal, pos, 4, 3, 4, 6, 3, 6, box);
                }
                spawnerPos = pos.add(9, 3, 5);
                break;

            default: // SOUTH
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[0]), pos, 4, 3, 8, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[1]), pos, 5, 3, 8, box);
                placeRelativeBlockInBox(world, nearPortalFrame.with(EndPortalFrameBlock.EYE, eyes[2]), pos, 6, 3, 8, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[3]), pos, 4, 3, 12, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[4]), pos, 5, 3, 12, box);
                placeRelativeBlockInBox(world, farPortalFrame.with(EndPortalFrameBlock.EYE, eyes[5]), pos, 6, 3, 12, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[6]), pos, 3, 3, 9, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[7]), pos, 3, 3, 10, box);
                placeRelativeBlockInBox(world, rightPortalFrame.with(EndPortalFrameBlock.EYE, eyes[8]), pos, 3, 3, 11, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[9]), pos, 7, 3, 9, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[10]), pos, 7, 3, 10, box);
                placeRelativeBlockInBox(world, leftPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 7, 3, 11, box);
                if (completePortal) {
                    BlockState endPortal = Blocks.END_PORTAL.getDefaultState();
                    fillRelativeBlockInBox(world, endPortal, pos, 4, 3, 9, 6, 3, 11, box);
                }
                spawnerPos = pos.add(5, 3, 6);
        }

        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity spawnerEntity = world.getBlockEntity(spawnerPos);
        if (spawnerEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity) spawnerEntity).getLogic().setEntityId(EntityType.SILVERFISH);
        }
    }

    protected static void generateSpawnPlatformInBox(ServerWorldAccess world, BlockPos spawnpoint, BlockBox box) {
        fillRelativeBlockInBox(world, Blocks.GRASS_BLOCK.getDefaultState(), spawnpoint, -2, -1, -7, 2, -1, 2, box);
        placeRelativeBlockInBox(world, Blocks.MYCELIUM.getDefaultState(), spawnpoint, 0, -1, 0, box);
        placeRelativeBlockInBox(world, Blocks.CRIMSON_NYLIUM.getDefaultState(), spawnpoint, -1, -1, 1, box);
        placeRelativeBlockInBox(world, Blocks.WARPED_NYLIUM.getDefaultState(), spawnpoint, 1, -1, 1, box);
        placeRelativeBlockInBox(world, Blocks.DIRT.getDefaultState(), spawnpoint, 0, -1, -5, box);
        fillRelativeBlockInBox(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -2, 3, -7, 2, 4, -3, box);
        fillRelativeBlockInBox(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -5, 1, 6, -5, box);
        fillRelativeBlockInBox(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, 0, 5, -6, 0, 6, -4, box);
        placeRelativeBlockInBox(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -4, box);
        placeRelativeBlockInBox(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -6, box);
        placeRelativeBlockInBox(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 4, -3, box);
        placeRelativeBlockInBox(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 3, -7, box);
        fillRelativeBlockInBox(world, Blocks.OAK_LOG.getDefaultState(), spawnpoint, 0, 0, -5, 0, 5, -5, box);
    }
}
