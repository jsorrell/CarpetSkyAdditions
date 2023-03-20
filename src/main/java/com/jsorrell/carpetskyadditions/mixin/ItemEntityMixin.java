package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow(prefix = "shadow$")
    public abstract ItemStack shadow$getStack();

    private void compactToDiamonds() {
        int numCoalBlocks = this.shadow$getStack().getCount();
        int numDiamonds = numCoalBlocks / 64;
        int remainingCoalBlocks = numCoalBlocks % 64;
        ItemEntity diamondEntity = new ItemEntity(
                this.world, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.DIAMOND, numDiamonds));
        diamondEntity.setToDefaultPickupDelay();
        this.world.spawnEntity(diamondEntity);

        this.shadow$getStack().setCount(remainingCoalBlocks);
    }

    @Inject(method = "damage", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true, at = @At(value = "HEAD"))
    private void compactCoalToDiamonds(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (SkyAdditionsSettings.renewableDiamonds) {
            if (source.isFallingBlock() && source.name.equals("anvil")) {
                ItemStack stack = this.shadow$getStack();
                if (Items.COAL_BLOCK.equals(stack.getItem()) && 64 <= stack.getCount()) {
                    this.compactToDiamonds();
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
