package com.jsorrell.carpetskyadditions.gen;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class SkyBlockStructures {
    protected record StructureOrientation(Rotation rotation, Mirror mirror) {
        private int applyXTransform(int x, int z, BoundingBox boundingBox) {
            if ((rotation == Rotation.NONE && mirror != Mirror.FRONT_BACK)
                    || (rotation == Rotation.CLOCKWISE_180 && mirror == Mirror.FRONT_BACK)) {
                return boundingBox.minX() + x;
            } else if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
                return boundingBox.maxX() - x;
            } else if ((rotation == Rotation.COUNTERCLOCKWISE_90 && mirror != Mirror.LEFT_RIGHT)
                    || (rotation == Rotation.CLOCKWISE_90 && mirror == Mirror.LEFT_RIGHT)) {
                return boundingBox.minX() + z;
            } else {
                return boundingBox.maxX() - z;
            }
        }

        private int applyZTransform(int x, int z, BoundingBox boundingBox) {
            if ((rotation == Rotation.NONE && mirror != Mirror.LEFT_RIGHT)
                    || (rotation == Rotation.CLOCKWISE_180 && mirror == Mirror.LEFT_RIGHT)) {
                return boundingBox.minZ() + z;
            } else if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
                return boundingBox.maxZ() - z;
            } else if ((rotation == Rotation.CLOCKWISE_90 && mirror != Mirror.FRONT_BACK)
                    || (rotation == Rotation.COUNTERCLOCKWISE_90 && mirror == Mirror.LEFT_RIGHT)) {
                return boundingBox.minZ() + x;
            } else {
                return boundingBox.maxZ() - x;
            }
        }
    }

    protected abstract static class SkyBlockStructure {
        protected BoundingBox boundingBox;
        protected StructureOrientation orientation;
        protected Rotation rotation;
        protected Mirror mirror;

        public SkyBlockStructure(StructurePiece piece) {
            this.boundingBox = piece.getBoundingBox();
            this.rotation = Objects.requireNonNullElse(piece.getRotation(), Rotation.NONE);
            this.mirror = Objects.requireNonNullElse(piece.getMirror(), Mirror.NONE);
            this.orientation = new StructureOrientation(this.rotation, this.mirror);
        }

        protected int applyXTransform(int x, int z) {
            return this.orientation.applyXTransform(x, z, this.boundingBox);
        }

        protected int applyYTransform(int y) {
            return y + this.boundingBox.minY();
        }

        protected int applyZTransform(int x, int z) {
            return this.orientation.applyZTransform(x, z, this.boundingBox);
        }

        protected BlockPos.MutableBlockPos offsetPos(int x, int y, int z) {
            return new BlockPos.MutableBlockPos(
                    this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
        }

        protected void addBlock(ServerLevelAccessor world, BlockState block, int x, int y, int z, BoundingBox bounds) {
            BlockPos.MutableBlockPos blockPos = this.offsetPos(x, y, z);
            if (!bounds.isInside(blockPos)) {
                return;
            }
            if (this.mirror != Mirror.NONE) {
                block = block.mirror(this.mirror);
            }
            if (this.rotation != Rotation.NONE) {
                block = block.rotate(this.rotation);
            }
            world.setBlock(blockPos, block, Block.UPDATE_CLIENTS);
        }

        protected void fillBlocks(
                ServerLevelAccessor world,
                BlockState block,
                int minX,
                int minY,
                int minZ,
                int maxX,
                int maxY,
                int maxZ,
                BoundingBox bounds) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        this.addBlock(world, block, x, y, z, bounds);
                    }
                }
            }
        }

        public abstract void generate(ServerLevelAccessor world, BoundingBox bounds, RandomSource random);
    }

    public static class EndPortalStructure extends SkyBlockStructure {
        public EndPortalStructure(StructurePiece piece) {
            super(piece);
        }

        @Override
        public void generate(ServerLevelAccessor world, BoundingBox bounds, RandomSource random) {
            BlockState northFrame = Blocks.END_PORTAL_FRAME.defaultBlockState();
            BlockState southFrame = northFrame.setValue(EndPortalFrameBlock.FACING, Direction.SOUTH);
            BlockState eastFrame = northFrame.setValue(EndPortalFrameBlock.FACING, Direction.EAST);
            BlockState westFrame = northFrame.setValue(EndPortalFrameBlock.FACING, Direction.WEST);

            boolean complete = true;
            boolean[] hasEye = new boolean[12];
            for (int l = 0; l < hasEye.length; ++l) {
                hasEye[l] = random.nextFloat() > 0.9f;
                complete &= hasEye[l];
            }

            this.addBlock(world, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[0]), 4, 3, 3, bounds);
            this.addBlock(world, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[1]), 5, 3, 3, bounds);
            this.addBlock(world, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[2]), 6, 3, 3, bounds);
            this.addBlock(world, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[3]), 4, 3, 7, bounds);
            this.addBlock(world, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[4]), 5, 3, 7, bounds);
            this.addBlock(world, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[5]), 6, 3, 7, bounds);
            this.addBlock(world, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[6]), 3, 3, 4, bounds);
            this.addBlock(world, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[7]), 3, 3, 5, bounds);
            this.addBlock(world, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[8]), 3, 3, 6, bounds);
            this.addBlock(world, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[9]), 7, 3, 4, bounds);
            this.addBlock(world, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[10]), 7, 3, 5, bounds);
            this.addBlock(world, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, hasEye[11]), 7, 3, 6, bounds);

            if (complete) {
                this.fillBlocks(world, Blocks.END_PORTAL.defaultBlockState(), 4, 3, 4, 6, 3, 6, bounds);
            }
        }
    }

    public static class AncientCityPortalStructure extends SkyBlockStructure {
        public AncientCityPortalStructure(StructurePiece piece) {
            super(piece);
        }

        @Override
        public void generate(ServerLevelAccessor world, BoundingBox bounds, RandomSource random) {
            // Horizontal Sides
            this.fillBlocks(world, Blocks.REINFORCED_DEEPSLATE.defaultBlockState(), 13, 17, 10, 13, 17, 31, bounds);
            this.fillBlocks(world, Blocks.REINFORCED_DEEPSLATE.defaultBlockState(), 13, 24, 10, 13, 24, 31, bounds);

            // Vertical Sides
            this.fillBlocks(world, Blocks.REINFORCED_DEEPSLATE.defaultBlockState(), 13, 18, 10, 13, 23, 10, bounds);
            this.fillBlocks(world, Blocks.REINFORCED_DEEPSLATE.defaultBlockState(), 13, 18, 31, 13, 23, 31, bounds);

            // Sculk Shrieker
            this.addBlock(
                    world,
                    Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true),
                    9,
                    8,
                    20,
                    bounds);
        }
    }

    public static class SpawnerStructure extends SkyBlockStructure {
        private final BlockPos spawnerPos;
        private final EntityType<?> spawnerType;

        public SpawnerStructure(StructurePiece piece, BlockPos spawnerPos, EntityType<?> spawnerType) {
            super(piece);
            this.spawnerPos = spawnerPos;
            this.spawnerType = spawnerType;
        }

        @Override
        public void generate(ServerLevelAccessor world, BoundingBox bounds, RandomSource random) {
            BlockPos.MutableBlockPos spawnerAbsolutePos =
                    this.offsetPos(spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ());
            if (bounds.isInside(spawnerAbsolutePos)) {
                world.setBlock(spawnerAbsolutePos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_CLIENTS);
                BlockEntity blockEntity = world.getBlockEntity(spawnerAbsolutePos);
                if (blockEntity instanceof SpawnerBlockEntity spawnerEntity) {
                    spawnerEntity.setEntityId(spawnerType, random);
                }
            }
        }
    }

    public static class SilverfishSpawnerStructure extends SpawnerStructure {
        public SilverfishSpawnerStructure(StructurePiece piece) {
            super(piece, new BlockPos(5, 3, 9), EntityType.SILVERFISH);
        }
    }

    public static class MagmaCubeSpawner extends SpawnerStructure {
        public MagmaCubeSpawner(StructurePiece piece) {
            super(piece, new BlockPos(11, 7, 19), EntityType.MAGMA_CUBE);
        }
    }
}
