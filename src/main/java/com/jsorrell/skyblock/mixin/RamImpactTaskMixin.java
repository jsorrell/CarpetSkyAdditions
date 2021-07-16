package com.jsorrell.skyblock.mixin;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.event.GameEvent;

import com.jsorrell.skyblock.SkyBlockSettings;

@Mixin(RamImpactTask.class)
public abstract class RamImpactTaskMixin<E extends PathAwareEntity> extends Task<E> {

  public RamImpactTaskMixin(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
    super(requiredMemoryState);
  }

  @Inject(
      method = "keepRunning",
      locals = LocalCapture.CAPTURE_FAILSOFT,
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/ai/brain/task/RamImpactTask;finishRam(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;)V",
              ordinal = 1))
  private void breakOpenNetherWart(ServerWorld world, E rammer, long time, CallbackInfo ci) {
    if (SkyBlockSettings.enableSkyBlockFeatures && SkyBlockSettings.rammingWart) {
      Brain<?> ramBrain = rammer.getBrain();
      if (!ramBrain.getOptionalMemory(MemoryModuleType.WALK_TARGET).isPresent()
          && ramBrain.getOptionalMemory(MemoryModuleType.RAM_TARGET).isPresent()
          && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
        Box box = rammer.getBoundingBox().expand(0.05D);

        Iterator<BlockPos> blocksHit =
            BlockPos.iterate(
                    MathHelper.floor(box.minX),
                    MathHelper.floor(box.minY),
                    MathHelper.floor(box.minZ),
                    MathHelper.floor(box.maxX),
                    MathHelper.floor(box.maxY),
                    MathHelper.floor(box.maxZ))
                .iterator();

        Random random = new Random();

        blocksHit.forEachRemaining(
            blockPos -> {
              BlockState state = world.getBlockState(blockPos);

              if (state.getBlock() == Blocks.NETHER_WART_BLOCK) {
                Block.dropStack(
                    world, blockPos, new ItemStack(Items.NETHER_WART, random.nextInt(2) + 1));

                boolean blockRemoved = world.removeBlock(blockPos, false);
                if (blockRemoved) {
                  world.emitGameEvent(rammer, GameEvent.BLOCK_DESTROY, blockPos);
                }
              }
            });
      }
    }
  }
}
