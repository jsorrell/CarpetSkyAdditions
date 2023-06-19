package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    private void compactToDiamonds() {
        int numCoalBlocks = getItem().getCount();
        int numDiamonds = numCoalBlocks / 64;
        int remainingCoalBlocks = numCoalBlocks % 64;
        ItemEntity diamondEntity =
                new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack(Items.DIAMOND, numDiamonds));
        diamondEntity.setDefaultPickUpDelay();
        level().addFreshEntity(diamondEntity);

        getItem().setCount(remainingCoalBlocks);
    }

    @Inject(method = "hurt", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true, at = @At(value = "HEAD"))
    private void compactCoalToDiamonds(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (SkyAdditionsSettings.renewableDiamonds) {
            if (source.is(DamageTypes.FALLING_ANVIL)) {
                ItemStack stack = getItem();
                if (Items.COAL_BLOCK.equals(stack.getItem()) && 64 <= stack.getCount()) {
                    compactToDiamonds();
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
