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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
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
import java.util.function.Supplier;

// Normally, this will always fail validation and give warning on load.
// This warning is disabled through a mixin.
// See validation method DimensionOptions.method_29567
public class SkyblockChunkGenerator extends ChunkGenerator {
    private final long seed;
    private final Supplier<ChunkGeneratorSettings> settings;
//    private final ChunkGeneratorType chunkGeneratorType;

    // We reference this because SurfaceChunkGenerator final and so can't subclass it
    private final NoiseChunkGenerator reference;

    public static final Codec<SkyblockChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("seed").stable().forGetter(SkyblockChunkGenerator::getSeed),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(SkyblockChunkGenerator::getBiomeSource),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(SkyblockChunkGenerator::getSettings)
    ).apply(instance, instance.stable(SkyblockChunkGenerator::new)));

    public SkyblockChunkGenerator(long seed, BiomeSource biomeSource, Supplier<ChunkGeneratorSettings> settings) {
        super(biomeSource, settings.get().getStructuresConfig());
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
                generateSpawnPlatform(region, spawn);
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
        BlockPos pos = chunkPos.getCenterBlockPos();

        accessor.getStructuresWithChildren(ChunkSectionPos.from(pos), Registry.STRUCTURE_FEATURE.get(new Identifier("minecraft:stronghold"))).forEach((structureStart) -> {
            for (StructurePiece piece : structureStart.getChildren()) {
                if (piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM) {
                    BlockPos portalPos = new BlockPos(piece.getBoundingBox().minX, piece.getBoundingBox().minY, piece.getBoundingBox().minZ);
                    generateStrongholdPortal(region, portalPos, this.seed);
                }
            }
        });
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    protected static void placeRelativeBlock(WorldAccess world, BlockState block, BlockPos referencePos, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(referencePos.getX() + x, referencePos.getY() + y, referencePos.getZ() + z);
        world.setBlockState(blockPos, block, 2);
    }

    protected static void fillRelativeBlock(WorldAccess world, BlockState block, BlockPos referencePos, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    placeRelativeBlock(world, block, referencePos, x, y, z);
                }
            }
        }
    }

    private static void generateStrongholdPortal(ServerWorldAccess world, BlockPos pos, long seed) {
        boolean completePortal = true;
        boolean[] eyes = new boolean[12];

        ChunkRandom random = new ChunkRandom(seed);

        for (int i = 0; i < eyes.length; ++i) {
            eyes[i] = random.nextFloat() > 0.9F;
            completePortal &= eyes[i];
        }

        BlockState northPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.NORTH);
        BlockState southPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.SOUTH);
        BlockState eastPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.EAST);
        BlockState westPortalFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.WEST);

        placeRelativeBlock(world, southPortalFrame.with(EndPortalFrameBlock.EYE, eyes[0]), pos, 4, 3, 8);
        placeRelativeBlock(world, southPortalFrame.with(EndPortalFrameBlock.EYE, eyes[1]), pos, 5, 3, 8);
        placeRelativeBlock(world, southPortalFrame.with(EndPortalFrameBlock.EYE, eyes[2]), pos, 6, 3, 8);
        placeRelativeBlock(world, northPortalFrame.with(EndPortalFrameBlock.EYE, eyes[3]), pos, 4, 3, 12);
        placeRelativeBlock(world, northPortalFrame.with(EndPortalFrameBlock.EYE, eyes[4]), pos, 5, 3, 12);
        placeRelativeBlock(world, northPortalFrame.with(EndPortalFrameBlock.EYE, eyes[6]), pos, 6, 3, 12);
        placeRelativeBlock(world, eastPortalFrame.with(EndPortalFrameBlock.EYE, eyes[7]), pos, 3, 3, 9);
        placeRelativeBlock(world, eastPortalFrame.with(EndPortalFrameBlock.EYE, eyes[8]), pos, 3, 3, 10);
        placeRelativeBlock(world, eastPortalFrame.with(EndPortalFrameBlock.EYE, eyes[9]), pos, 3, 3, 11);
        placeRelativeBlock(world, westPortalFrame.with(EndPortalFrameBlock.EYE, eyes[10]), pos, 7, 3, 9);
        placeRelativeBlock(world, westPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 7, 3, 10);
        placeRelativeBlock(world, westPortalFrame.with(EndPortalFrameBlock.EYE, eyes[11]), pos, 7, 3, 11);
        if (completePortal) {
            BlockState endPortal = Blocks.END_PORTAL.getDefaultState();
            fillRelativeBlock(world, endPortal, pos, 4, 3, 9, 6, 3, 11);
        }

        int spawnerPositionOption = random.nextInt() % 4;
        int x, y = 4, z;
        switch (spawnerPositionOption) {
            case 0:
                x = 5;
                z = 6;
                break;
            case 1:
                x = 1;
                z = 10;
                break;
            case 2:
                x = 5;
                z = 14;
                break;
            default:
                x = 9;
                z = 10;
        }

        BlockPos spawnerPos = pos.add(x, y, z);
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity spawnerEntity = world.getBlockEntity(spawnerPos);
        if (spawnerEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity) spawnerEntity).getLogic().setEntityId(EntityType.SILVERFISH);
        }
    }

    protected static void generateSpawnPlatform(ServerWorldAccess world, BlockPos spawnpoint) {
        fillRelativeBlock(world, Blocks.GRASS_BLOCK.getDefaultState(), spawnpoint, -2, -1, -7, 2, -1, 2);
        placeRelativeBlock(world, Blocks.MYCELIUM.getDefaultState(), spawnpoint, 0, -1, 0);
        placeRelativeBlock(world, Blocks.CRIMSON_NYLIUM.getDefaultState(), spawnpoint, -1, -1, 1);
        placeRelativeBlock(world, Blocks.WARPED_NYLIUM.getDefaultState(), spawnpoint, 1, -1, 1);
        placeRelativeBlock(world, Blocks.DIRT.getDefaultState(), spawnpoint, 0, -1, -5);
        fillRelativeBlock(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -2, 3, -7, 2, 4, -3);
        fillRelativeBlock(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -5, 1, 6, -5);
        fillRelativeBlock(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, 0, 5, -6, 0, 6, -4);
        placeRelativeBlock(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -4);
        placeRelativeBlock(world, Blocks.OAK_LEAVES.getDefaultState(), spawnpoint, -1, 5, -6);
        placeRelativeBlock(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 4, -3);
        placeRelativeBlock(world, Blocks.AIR.getDefaultState(), spawnpoint, -2, 3, -7);
        fillRelativeBlock(world, Blocks.OAK_LOG.getDefaultState(), spawnpoint, 0, 0, -5, 0, 5, -5);
    }
}
