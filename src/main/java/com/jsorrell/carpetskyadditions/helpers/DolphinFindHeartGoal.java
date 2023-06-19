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
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected Optional<BlockPos> determineTreasureLocation() {
        // Set Y to -64 to make it swim as low as possible
        BlockPos potentialTarget = new BlockPos(
                dolphin.getBlockX() + dolphin.level().random.nextInt(16) - 8,
                -64,
                dolphin.getBlockZ() + dolphin.level().random.nextInt(16) - 8);
        if (dolphin.level().getBiome(potentialTarget.atY(dolphin.getBlockY())).is(BiomeTags.IS_OCEAN)) {
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
        return dolphin.gotFish() && 100 <= dolphin.getAirSupply();
    }

    @Override
    public void start() {
        if (!(dolphin.level() instanceof ServerLevel level)) {
            return;
        }
        Optional<BlockPos> treasurePosOpt = determineTreasureLocation();
        if (treasurePosOpt.isEmpty()) {
            dolphin.setGotFish(false);
            displayFailureParticles(level, dolphin);
            return;
        }
        BlockPos treasurePos = treasurePosOpt.get();
        dolphin.setTreasurePos(treasurePos);

        dolphin.getNavigation().moveTo(treasurePos.getX(), treasurePos.getY(), treasurePos.getZ(), 0.7);
        displaySuccessParticles(level, dolphin);
    }

    private static void displaySuccessParticles(ServerLevel level, Dolphin dolphin) {
        level.broadcastEntityEvent(dolphin, EntityEvent.DOLPHIN_LOOKING_FOR_TREASURE);
    }

    private static void displayFailureParticles(ServerLevel level, Dolphin dolphin) {
        level.sendParticles(
                ParticleTypes.WITCH,
                dolphin.getRandomX(1),
                dolphin.getRandomY() + 1.6,
                dolphin.getRandomZ(1),
                5,
                level.random.nextGaussian() * 0.02,
                level.random.nextGaussian() * 0.02,
                level.random.nextGaussian() * 0.02,
                0.2);
    }

    @Override
    public void tick() {
        if (!(dolphin.level() instanceof ServerLevel level)) {
            return;
        }
        if (!diggingPhase && dolphin.getNavigation().isDone()) {
            BlockPos heartPos = new BlockPos(
                    dolphin.getTreasurePos().getX(),
                    dolphin.getBlockY() - 1,
                    dolphin.getTreasurePos().getZ());
            if (dolphin.position().closerThan(Vec3.atBottomCenterOf(heartPos).add(0, 1, 0), 8)
                    && VALID_OCEAN_FLOORS.contains(level.getBlockState(heartPos).getBlock())) {
                diggingPhase = true;
                digCounter = 0;
            } else {
                displayFailureParticles(level, dolphin);
                dolphin.setGotFish(false);
            }
        } else if (diggingPhase) {
            if (digCounter < NUM_DIGS) {
                level.levelEvent(
                        LevelEvent.PARTICLES_DESTROY_BLOCK,
                        dolphin.blockPosition(),
                        Block.getId(dolphin.level()
                                .getBlockState(dolphin.blockPosition().below())));
                digCounter++;
            } else {
                if (level.random.nextFloat() < CHANCE_TO_FIND_HEART_OF_THE_SEA) {
                    ItemStack heartOfTheSea = new ItemStack(Items.HEART_OF_THE_SEA);
                    if (dolphin.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && dolphin.canHoldItem(heartOfTheSea)) {
                        dolphin.setItemSlot(EquipmentSlot.MAINHAND, heartOfTheSea);
                    }
                    displaySuccessParticles(level, dolphin);
                } else {
                    displayFailureParticles(level, dolphin);
                }
                dolphin.setGotFish(false);
                diggingPhase = false;
            }
        }
    }
}
