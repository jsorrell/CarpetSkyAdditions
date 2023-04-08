package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.AncientCityEnchantingTableHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {
    private static final double MAX_WARDEN_DISTANCE = 8.0;

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Redirect(
            method = "getEnchantmentList",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;selectEnchantment(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;IZ)Ljava/util/List;"))
    private List<EnchantmentInstance> addSwiftSneak(
            RandomSource random, ItemStack stack, int level, boolean allowTreasure) {
        if (SkyAdditionsSettings.renewableSwiftSneak) {
            boolean hasWardenNearby = access.evaluate((world, blockPos) -> {
                        AABB box = new AABB(blockPos).inflate(MAX_WARDEN_DISTANCE);
                        // Center of table's hitbox
                        Vec3 tablePos = Vec3.atBottomCenterOf(blockPos).relative(Direction.UP, 0.375);
                        Predicate<Warden> rangePredicate = e -> e.position().closerThan(tablePos, MAX_WARDEN_DISTANCE);
                        List<Warden> wardenEntities = world.getEntitiesOfClass(Warden.class, box, rangePredicate);
                        return !wardenEntities.isEmpty();
                    })
                    .orElseThrow();

            if (hasWardenNearby) {
                return AncientCityEnchantingTableHelper.selectEnchantment(random, stack, level, allowTreasure);
            }
        }
        return EnchantmentHelper.selectEnchantment(random, stack, level, allowTreasure);
    }
}
