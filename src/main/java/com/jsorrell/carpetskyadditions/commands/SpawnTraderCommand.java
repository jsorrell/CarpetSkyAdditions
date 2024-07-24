package com.jsorrell.carpetskyadditions.commands;

import com.jsorrell.carpetskyadditions.helpers.TraderSpawnerHelper;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class SpawnTraderCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("summontrader")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .executes(commandContext -> spawnTrader(commandContext.getSource())));
    }

    private static int spawnTrader(CommandSourceStack source) {
        Entity entity = TraderSpawnerHelper.spawnTrader(
                source.getLevel(), source.getLevel().getRandom(), BlockPos.containing(source.getPosition()));
        if (entity == null) {
            source.sendFailure(Component.translatable("commands.summon.failed"));
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("commands.summon.success", entity.getDisplayName()), true);
        return 1;
    }
}
