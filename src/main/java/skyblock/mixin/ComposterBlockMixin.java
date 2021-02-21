package skyblock.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import skyblock.SkyBlockSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    // Maybe this should have Gravel, Mycelium, Podzol etc
    private static final List<Block> possibleSurfaceDrops = new ArrayList<>(Arrays.asList(Blocks.SAND, Blocks.RED_SAND, Blocks.DIRT));

    private static Item getComposterDrop(WorldAccess world, BlockPos pos) {
        Block dropBlock = Blocks.DIRT;

        Block surfaceBlock = world.getBiome(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial().getBlock();
        if (possibleSurfaceDrops.contains(surfaceBlock)) {
            dropBlock = surfaceBlock;
        }

        return dropBlock.asItem();
    }

    // Provides a simple, single-item inventory for the full composter. Used in place of the private subclass
    private static class FullComposterInventory extends SimpleInventory implements SidedInventory {
        private final BlockState state;
        private final WorldAccess world;
        private final BlockPos pos;
        private boolean dirty;

        public FullComposterInventory(BlockState state, WorldAccess world, BlockPos pos) {
            super(new ItemStack(getComposterDrop(world, pos)));
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        public int getMaxCountPerStack() {
            return 1;
        }

        public int[] getAvailableSlots(Direction side) {
            return side == Direction.DOWN ? new int[]{0} : new int[0];
        }

        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return !this.dirty && dir == Direction.DOWN && stack.getItem() == getComposterDrop(world, pos);
        }

        public void markDirty() {
            emptyComposter(this.state, this.world, this.pos);
            this.dirty = true;
        }
    }

    private static void emptyComposter(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState blockState = state.with(ComposterBlock.LEVEL, 0);
        world.setBlockState(pos, blockState, 3);
    }


    @Inject(method = "emptyFullComposter", locals = LocalCapture.CAPTURE_FAILSOFT, at=@At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V",
        shift = At.Shift.BEFORE
    ))
    @SuppressWarnings("unused")
    private static void setComposterOutput(BlockState arg0, World world, BlockPos pos, CallbackInfoReturnable<BlockState> cir, float f, double d, double e, double g, ItemEntity dropItemEntity) {
        if (SkyBlockSettings.doUsefulComposters) {
            if (!SkyBlockSettings.usefulCompostersNeedRedstone || world.isReceivingRedstonePower(pos)) {
                dropItemEntity.setStack(new ItemStack(getComposterDrop(world, pos)));
            }
        }
    }

    @Inject(method = "getInventory", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true, at=@At(
            value = "RETURN",
            ordinal = 0
    ))
    @SuppressWarnings("unused")
    private void setInventory(BlockState state, WorldAccess world, BlockPos pos, CallbackInfoReturnable<SidedInventory> cir) {
        if (SkyBlockSettings.doUsefulComposters) {
            if (!SkyBlockSettings.usefulCompostersNeedRedstone || ((World)world).isReceivingRedstonePower(pos)) {
                cir.setReturnValue(new FullComposterInventory(state, world, pos));
            }
        }
    }
}
