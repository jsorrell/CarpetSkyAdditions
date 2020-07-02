package skyblock.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    private final static List<Block> possibleSurfaceDrops = new ArrayList<>(Arrays.asList(Blocks.SAND, Blocks.RED_SAND, Blocks.DIRT));

    @Inject(method = "emptyFullComposter", locals = LocalCapture.CAPTURE_FAILSOFT, at=@At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V",
            shift = At.Shift.BEFORE
    ))
    private static void setComposterOutput(BlockState arg0, World world, BlockPos pos, CallbackInfoReturnable<BlockState> cir, float f, double d, double e, double g, ItemEntity dropItemEntity) {
        if (SkyBlockSettings.usefulComposters) {
            Block dropBlock;

            Block surfaceBlock = world.getBiome(pos).getSurfaceConfig().getTopMaterial().getBlock();
            if (possibleSurfaceDrops.contains(surfaceBlock)) {
                dropBlock = surfaceBlock;
            } else {
                dropBlock = Blocks.DIRT;
            }

            dropItemEntity.setStack(new ItemStack(dropBlock));
        }
    }
}
