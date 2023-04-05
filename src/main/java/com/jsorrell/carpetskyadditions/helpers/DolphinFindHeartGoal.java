package com.jsorrell.carpetskyadditions.helpers;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;

public class DolphinFindHeartGoal extends Goal {
    private static final float CHANCE_TO_FIND_HEART_OF_THE_SEA = 0.05f;
    private static final float NUM_DIGS = 10;
    private static final Set<Block> VALID_OCEAN_FLOORS = Set.of(Blocks.SAND, Blocks.GRAVEL);
    private final Dolphin dolphin;
    private int digCounter = 0;
    private boolean diggingPhase = false;

    public DolphinFindHeartGoal(Dolphin dolphin) {
        this.dolphin = dolphin;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected Optional<BlockPos> determineTreasureLocation() {
        // Set Y -64 to make it swim to ocean floor
        BlockPos potentialTarget = new BlockPos(
                this.dolphin.getBlockX() + this.dolphin.level.random.nextInt(16) - 8,
                -64,
                this.dolphin.getBlockZ() + this.dolphin.level.random.nextInt(16) - 8);
        if (this.dolphin
                .level
                .getBiome(potentialTarget.atY(this.dolphin.getBlockY()))
                .is(BiomeTags.IS_OCEAN)) {
            return Optional.of(potentialTarget);
        }

        return Optional.empty();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        return this.dolphin.gotFish() && 100 <= this.dolphin.getAirSupply();
    }

    @Override
    public void start() {
        if (!(this.dolphin.level instanceof ServerLevel world)) {
            return;
        }
        Optional<BlockPos> treasurePosOpt = this.determineTreasureLocation();
        if (treasurePosOpt.isEmpty()) {
            this.dolphin.setGotFish(false);
            return;
        }
        BlockPos treasurePos = treasurePosOpt.get();
        this.dolphin.setTreasurePos(treasurePos);

        this.dolphin.getNavigation().moveTo(treasurePos.getX(), treasurePos.getY(), treasurePos.getZ(), 0.7);
        world.broadcastEntityEvent(this.dolphin, (byte) 38);
    }

    private static void displaySuccessParticles(ServerLevel world, Dolphin dolphin) {
        world.broadcastEntityEvent(dolphin, EntityEvent.DOLPHIN_LOOKING_FOR_TREASURE);
    }

    private static void displayFailureParticles(ServerLevel world, Dolphin dolphin) {
        world.sendParticles(
                ParticleTypes.WITCH,
                dolphin.getRandomX(1),
                dolphin.getRandomY() + 1.6,
                dolphin.getRandomZ(1),
                5,
                world.random.nextGaussian() * 0.02,
                world.random.nextGaussian() * 0.02,
                world.random.nextGaussian() * 0.02,
                0.2);
    }

    @Override
    public void tick() {
        if (!(this.dolphin.level instanceof ServerLevel world)) {
            return;
        }
        if (!this.diggingPhase && this.dolphin.getNavigation().isDone()) {
            BlockPos heartPos = new BlockPos(
                    this.dolphin.getTreasurePos().getX(),
                    this.dolphin.getBlockY() - 1,
                    this.dolphin.getTreasurePos().getZ());
            if (this.dolphin
                            .position()
                            .closerThan(Vec3.atBottomCenterOf(heartPos).add(0, 1, 0), 8)
                    && VALID_OCEAN_FLOORS.contains(world.getBlockState(heartPos).getBlock())) {
                this.diggingPhase = true;
                this.digCounter = 0;
            } else {
                displayFailureParticles(world, this.dolphin);
                this.dolphin.setGotFish(false);
            }
        } else if (this.diggingPhase) {
            if (this.digCounter < NUM_DIGS) {
                world.levelEvent(
                        LevelEvent.PARTICLES_DESTROY_BLOCK,
                        this.dolphin.blockPosition(),
                        Block.getId(this.dolphin.level.getBlockState(
                                this.dolphin.blockPosition().below())));
                this.digCounter++;
            } else {
                if (world.random.nextFloat() < CHANCE_TO_FIND_HEART_OF_THE_SEA) {
                    ItemStack heartOfTheSea = new ItemStack(Items.HEART_OF_THE_SEA);
                    if (this.dolphin.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()
                            && this.dolphin.canHoldItem(heartOfTheSea)) {
                        this.dolphin.setItemSlot(EquipmentSlot.MAINHAND, heartOfTheSea);
                    }
                    displaySuccessParticles(world, this.dolphin);
                } else {
                    displayFailureParticles(world, this.dolphin);
                }
                this.dolphin.setGotFish(false);
                this.diggingPhase = false;
            }
        }
    }
}
