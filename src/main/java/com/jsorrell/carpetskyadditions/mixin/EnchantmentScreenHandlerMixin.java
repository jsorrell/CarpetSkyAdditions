package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.AncientCityEnchantingTableHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    private static final double MAX_WARDEN_DISTANCE = 8.0;

    @Shadow
    @Final
    private ScreenHandlerContext context;

    @Redirect(
            method = "generateEnchantments",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;IZ)Ljava/util/List;"))
    private List<EnchantmentLevelEntry> addSwiftSneak(
            Random random, ItemStack stack, int level, boolean treasureAllowed) {
        if (SkyAdditionsSettings.renewableSwiftSneak) {
            boolean hasWardenNearby = this.context
                    .get((world, blockPos) -> {
                        Box box = new Box(blockPos).expand(MAX_WARDEN_DISTANCE);
                        // Center of table's hitbox
                        Vec3d tablePos = Vec3d.ofBottomCenter(blockPos).offset(Direction.UP, 0.375);
                        Predicate<WardenEntity> rangePredicate =
                                e -> e.getPos().isInRange(tablePos, MAX_WARDEN_DISTANCE);
                        List<WardenEntity> wardenEntities =
                                world.getEntitiesByClass(WardenEntity.class, box, rangePredicate);
                        return !wardenEntities.isEmpty();
                    })
                    .orElseThrow();

            if (hasWardenNearby) {
                return AncientCityEnchantingTableHelper.generateEnchantments(random, stack, level, treasureAllowed);
            }
        }
        return EnchantmentHelper.generateEnchantments(random, stack, level, treasureAllowed);
    }
}
