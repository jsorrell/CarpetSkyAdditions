package com.jsorrell.skyblock.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

public class SkyBlockStructures {

  protected static abstract class SkyBlockStructure {
    protected Direction facing;
    protected BlockBox boundingBox;
    protected BlockRotation rotation;
    protected BlockMirror mirror;

    public SkyBlockStructure(Direction orientation, BlockBox boundingBox) {
      this.facing = orientation;
      this.boundingBox = boundingBox;
      if (orientation == null) {
        this.rotation = BlockRotation.NONE;
        this.mirror = BlockMirror.NONE;
      } else {
        switch (orientation) {
          case SOUTH -> {
            this.mirror = BlockMirror.LEFT_RIGHT;
            this.rotation = BlockRotation.NONE;
          }
          case WEST -> {
            this.mirror = BlockMirror.LEFT_RIGHT;
            this.rotation = BlockRotation.CLOCKWISE_90;
          }
          case EAST -> {
            this.mirror = BlockMirror.NONE;
            this.rotation = BlockRotation.CLOCKWISE_90;
          }
          default -> {
            this.mirror = BlockMirror.NONE;
            this.rotation = BlockRotation.NONE;
          }
        }
      }
    }

    public BlockBox getBoundingBox() {
      return boundingBox;
    }

    public Direction getFacing() {
      return facing;
    }

    protected int applyXTransform(int x, int z) {
      if (this.facing == null) {
        return x;
      }
      switch (this.facing) {
        case NORTH, SOUTH -> {
          return this.boundingBox.getMinX() + x;
        }
        case WEST -> {
          return this.boundingBox.getMaxX() - z;
        }
        case EAST -> {
          return this.boundingBox.getMinX() + z;
        }
      }
      return x;
    }

    protected int applyYTransform(int y) {
      if (this.facing == null) {
        return y;
      }
      return y + this.boundingBox.getMinY();
    }

    protected int applyZTransform(int x, int z) {
      if (this.facing == null) {
        return z;
      }
      switch (this.facing) {
        case NORTH -> {
          return this.boundingBox.getMaxZ() - z;
        }
        case SOUTH -> {
          return this.boundingBox.getMinZ() + z;
        }
        case WEST, EAST -> {
          return this.boundingBox.getMinZ() + x;
        }
      }
      return z;
    }

    protected BlockPos.Mutable offsetPos(int x, int y, int z) {
      return new BlockPos.Mutable(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
    }

    protected void addBlock(ServerWorldAccess world, BlockState block, int x, int y, int z, BlockBox bounds) {
      BlockPos.Mutable blockPos = this.offsetPos(x, y, z);
      if (!bounds.contains(blockPos)) {
        return;
      }
      if (this.mirror != BlockMirror.NONE) {
        block = block.mirror(this.mirror);
      }
      if (this.rotation != BlockRotation.NONE) {
        block = block.rotate(this.rotation);
      }
      world.setBlockState(blockPos, block, Block.NOTIFY_LISTENERS);
    }

    protected void fillBlocks(
      ServerWorldAccess world,
      BlockState block,
      int minX,
      int minY,
      int minZ,
      int maxX,
      int maxY,
      int maxZ,
      BlockBox bounds) {
      for (int x = minX; x <= maxX; ++x) {
        for (int y = minY; y <= maxY; ++y) {
          for (int z = minZ; z <= maxZ; ++z) {
            this.addBlock(world, block, x, y, z, bounds);
          }
        }
      }
    }

    public abstract void generate(ServerWorldAccess world, BlockBox bounds, Random random);
  }

  public static class EndPortalStructure extends SkyBlockStructure {
    public EndPortalStructure(Direction facing, BlockBox boundingBox) {
      super(facing, boundingBox);
    }

    public EndPortalStructure(StructurePiece piece) {
      this(piece.getFacing(), piece.getBoundingBox());
    }

    @Override
    public void generate(ServerWorldAccess world, BlockBox bounds, Random random) {
      BlockState northFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.NORTH);
      BlockState southFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.SOUTH);
      BlockState eastFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.EAST);
      BlockState westFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.WEST);
      boolean complete = true;
      boolean[] hasEye = new boolean[12];
      for (int l = 0; l < hasEye.length; ++l) {
        hasEye[l] = random.nextFloat() > 0.9f;
        complete &= hasEye[l];
      }
      this.addBlock(world, northFrame.with(EndPortalFrameBlock.EYE, hasEye[0]), 4, 3, 8, bounds);
      this.addBlock(world, northFrame.with(EndPortalFrameBlock.EYE, hasEye[1]), 5, 3, 8, bounds);
      this.addBlock(world, northFrame.with(EndPortalFrameBlock.EYE, hasEye[2]), 6, 3, 8, bounds);
      this.addBlock(world, southFrame.with(EndPortalFrameBlock.EYE, hasEye[3]), 4, 3, 12, bounds);
      this.addBlock(world, southFrame.with(EndPortalFrameBlock.EYE, hasEye[4]), 5, 3, 12, bounds);
      this.addBlock(world, southFrame.with(EndPortalFrameBlock.EYE, hasEye[5]), 6, 3, 12, bounds);
      this.addBlock(world, eastFrame.with(EndPortalFrameBlock.EYE, hasEye[6]), 3, 3, 9, bounds);
      this.addBlock(world, eastFrame.with(EndPortalFrameBlock.EYE, hasEye[7]), 3, 3, 10, bounds);
      this.addBlock(world, eastFrame.with(EndPortalFrameBlock.EYE, hasEye[8]), 3, 3, 11, bounds);
      this.addBlock(world, westFrame.with(EndPortalFrameBlock.EYE, hasEye[9]), 7, 3, 9, bounds);
      this.addBlock(world, westFrame.with(EndPortalFrameBlock.EYE, hasEye[10]), 7, 3, 10, bounds);
      this.addBlock(world, westFrame.with(EndPortalFrameBlock.EYE, hasEye[11]), 7, 3, 11, bounds);

      if (complete) {
        this.fillBlocks(world, Blocks.END_PORTAL.getDefaultState(), 4, 3, 9, 6, 3, 11, bounds);
      }
    }
  }

  public static class SpawnerStructure extends SkyBlockStructure {
    private final BlockPos spawnerPos;
    private final EntityType<?> spawnerType;

    public SpawnerStructure(Direction orientation, BlockBox boundingBox, BlockPos spawnerPos, EntityType<?> spawnerType) {
      super(orientation, boundingBox);
      this.spawnerPos = spawnerPos;
      this.spawnerType = spawnerType;
    }

    public SpawnerStructure(StructurePiece piece, BlockPos spawnerPos, EntityType<?> spawnerType) {
      this(piece.getFacing(), piece.getBoundingBox(), spawnerPos, spawnerType);
    }

    @Override
    public void generate(ServerWorldAccess world, BlockBox bounds, Random random) {
      BlockPos.Mutable spawnerAbsolutePos = this.offsetPos(spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ());
      if (bounds.contains(spawnerAbsolutePos)) {
        world.setBlockState(spawnerAbsolutePos, Blocks.SPAWNER.getDefaultState(), Block.NOTIFY_LISTENERS);
        BlockEntity blockEntity = world.getBlockEntity(spawnerAbsolutePos);
        if (blockEntity instanceof MobSpawnerBlockEntity spawnerEntity) {
          spawnerEntity.getLogic().setEntityId(spawnerType);
        }
      }
    }
  }

  public static class SilverfishSpawnerStructure extends SpawnerStructure {
    public SilverfishSpawnerStructure(StructurePiece piece) {
      super(piece, new BlockPos(5, 3, 5), EntityType.SILVERFISH);
    }
  }

  public static class MagmaCubeSpawner extends SpawnerStructure {
    public MagmaCubeSpawner(StructurePiece piece) {
      super(piece, new BlockPos(11, 7, 19), EntityType.MAGMA_CUBE);
    }
  }
}
