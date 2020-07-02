package skyblock;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.FEATURE;

public class SkyBlockSettings
{
    public static final String SKYBLOCK = "skyblock";
    
    @Rule(desc = "Add trades to the wandering trader for Skyblock", category = {SKYBLOCK, FEATURE}, validate = WanderingTraderSkyblockTradesChange.class)
    public static boolean wanderingTraderSkyblockTrades = false;
    
    public static class WanderingTraderSkyblockTradesChange extends Validator<Boolean> {
        @Override
        public Boolean validate(ServerCommandSource source, ParsedRule<Boolean> currentRule, Boolean newValue, String string) {
//            if (newValue) {
//                Trades.mergeWanderingTraderOffers(Trades.getSkyblockWanderingTraderOffers());
//            } else {
//                Trades.mergeWanderingTraderOffers(new Int2ObjectOpenHashMap<>());
//            }
//            return newValue;
            return newValue;
        }
    }

    @Rule(desc = "Composters create sand, red sand, and dirt depending on biome", category = {SKYBLOCK, FEATURE})
    public static boolean usefulComposters = false;
}
