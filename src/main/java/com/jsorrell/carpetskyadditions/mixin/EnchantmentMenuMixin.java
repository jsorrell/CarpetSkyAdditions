package com.jsorrell.carpetskyadditions.mixin;

import static com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper.MAX_WARDEN_DISTANCE_FOR_SWIFT_SNEAK;
import static com.jsorrell.carpetskyadditions.helpers.SkyAdditionsEnchantmentHelper.SWIFT_SNEAK_ENCHANTABLE_TAG;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteTag;
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
                stack = new ItemStack(stack.getItem(), stack.getCount());
                stack.addTagElement(SWIFT_SNEAK_ENCHANTABLE_TAG, ByteTag.ONE);
            }
        }
        return EnchantmentHelper.selectEnchantment(random, stack, level, allowTreasure);
    }
}
