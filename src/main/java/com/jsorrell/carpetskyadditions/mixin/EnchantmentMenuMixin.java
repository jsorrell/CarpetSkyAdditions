package com.jsorrell.carpetskyadditions.mixin;

import static com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper.MAX_WARDEN_DISTANCE_FOR_SWIFT_SNEAK;

import com.jsorrell.carpetskyadditions.SkyAdditionsDataComponents;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.flag.FeatureFlagSet;
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

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Redirect(
            method = "getEnchantmentList",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;selectEnchantment(Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;IZ)Ljava/util/List;"))
    private List<EnchantmentInstance> addSwiftSneak(
            FeatureFlagSet featureFlagSet, RandomSource random, ItemStack stack, int level, boolean allowTreasure) {
        if (SkyAdditionsSettings.renewableSwiftSneak) {
            boolean hasWardenNearby = access.evaluate((world, blockPos) -> {
                        AABB box = new AABB(blockPos).inflate(MAX_WARDEN_DISTANCE_FOR_SWIFT_SNEAK);
                        // Center of table's hitbox
                        Vec3 tablePos = Vec3.atBottomCenterOf(blockPos).relative(Direction.UP, 0.375);
                        Predicate<Warden> rangePredicate =
                                e -> e.position().closerThan(tablePos, MAX_WARDEN_DISTANCE_FOR_SWIFT_SNEAK);
                        List<Warden> wardenEntities = world.getEntitiesOfClass(Warden.class, box, rangePredicate);
                        return !wardenEntities.isEmpty();
                    })
                    .orElseThrow();

            if (hasWardenNearby) {
                stack = stack.copy();
                stack.set(SkyAdditionsDataComponents.SWIFT_SNEAK_ENCHANTABLE_COMPONENT, true);
            }
        }
        return EnchantmentHelper.selectEnchantment(featureFlagSet, random, stack, level, allowTreasure);
    }
}
