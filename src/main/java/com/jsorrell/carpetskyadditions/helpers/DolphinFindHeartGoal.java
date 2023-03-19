package com.jsorrell.carpetskyadditions.helpers;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

public class DolphinFindHeartGoal extends Goal {
    private static final float CHANCE_TO_FIND_HEART_OF_THE_SEA = 0.05f;
    private static final float NUM_DIGS = 10;
    private static final Set<Block> VALID_OCEAN_FLOORS = Set.of(Blocks.SAND, Blocks.GRAVEL);
    private final DolphinEntity dolphin;
    private int digCounter = 0;
    private boolean diggingPhase = false;

    public DolphinFindHeartGoal(DolphinEntity dolphin) {
        this.dolphin = dolphin;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    protected Optional<BlockPos> determineTreasureLocation() {
        // Set Y -64 to make it swim to ocean floor
        BlockPos potentialTarget = new BlockPos(
                this.dolphin.getBlockX() + this.dolphin.world.random.nextInt(16) - 8,
                -64,
                this.dolphin.getBlockZ() + this.dolphin.world.random.nextInt(16) - 8);
        if (this.dolphin
                .world
                .getBiome(potentialTarget.withY(this.dolphin.getBlockY()))
                .isIn(BiomeTags.IS_OCEAN)) {
            return Optional.of(potentialTarget);
        }

        return Optional.empty();
    }

    @Override
    public boolean canStop() {
        return false;
    }

    @Override
    public boolean canStart() {
        return this.dolphin.hasFish() && 100 <= this.dolphin.getAir();
    }

    @Override
    public void start() {
        if (!(this.dolphin.world instanceof ServerWorld world)) {
            return;
        }
        Optional<BlockPos> treasurePosOpt = this.determineTreasureLocation();
        if (treasurePosOpt.isEmpty()) {
            this.dolphin.setHasFish(false);
            return;
        }
        BlockPos treasurePos = treasurePosOpt.get();
        this.dolphin.setTreasurePos(treasurePos);

        this.dolphin.getNavigation().startMovingTo(treasurePos.getX(), treasurePos.getY(), treasurePos.getZ(), 0.7);
        world.sendEntityStatus(this.dolphin, (byte) 38);
    }

    private static void displaySuccessParticles(ServerWorld world, DolphinEntity dolphin) {
        world.sendEntityStatus(dolphin, EntityStatuses.ADD_DOLPHIN_HAPPY_VILLAGER_PARTICLES);
    }

    private static void displayFailureParticles(ServerWorld world, DolphinEntity dolphin) {
        world.spawnParticles(
                ParticleTypes.WITCH,
                dolphin.getParticleX(1),
                dolphin.getRandomBodyY() + 1.6,
                dolphin.getParticleZ(1),
                5,
                world.random.nextGaussian() * 0.02,
                world.random.nextGaussian() * 0.02,
                world.random.nextGaussian() * 0.02,
                0.2);
    }

    @Override
    public void tick() {
        if (!(this.dolphin.world instanceof ServerWorld world)) {
            return;
        }
        if (!this.diggingPhase && this.dolphin.getNavigation().isIdle()) {
            BlockPos heartPos = new BlockPos(
                    this.dolphin.getTreasurePos().getX(),
                    this.dolphin.getBlockY() - 1,
                    this.dolphin.getTreasurePos().getZ());
            if (this.dolphin.getPos().isInRange(Vec3d.ofBottomCenter(heartPos).add(0, 1, 0), 8)
                    && VALID_OCEAN_FLOORS.contains(world.getBlockState(heartPos).getBlock())) {
                this.diggingPhase = true;
                this.digCounter = 0;
            } else {
                displayFailureParticles(world, this.dolphin);
                this.dolphin.setHasFish(false);
            }
        } else if (this.diggingPhase) {
            if (this.digCounter < NUM_DIGS) {
                world.syncWorldEvent(
                        WorldEvents.BLOCK_BROKEN,
                        this.dolphin.getBlockPos(),
                        Block.getRawIdFromState(this.dolphin.world.getBlockState(
                                this.dolphin.getBlockPos().down())));
                this.digCounter++;
            } else {
                if (world.random.nextFloat() < CHANCE_TO_FIND_HEART_OF_THE_SEA) {
                    ItemStack heartOfTheSea = new ItemStack(Items.HEART_OF_THE_SEA);
                    if (this.dolphin.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()
                            && this.dolphin.canPickupItem(heartOfTheSea)) {
                        this.dolphin.equipStack(EquipmentSlot.MAINHAND, heartOfTheSea);
                    }
                    displaySuccessParticles(world, this.dolphin);
                } else {
                    displayFailureParticles(world, this.dolphin);
                }
                this.dolphin.setHasFish(false);
                this.diggingPhase = false;
            }
        }
    }
}
