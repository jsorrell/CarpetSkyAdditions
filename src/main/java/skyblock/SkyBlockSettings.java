package skyblock;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Locale;

import static carpet.settings.RuleCategory.FEATURE;

public class SkyBlockSettings
{
    public static final String SKYBLOCK = "skyblock";
    
    @Rule(desc = "Add trades to the wandering trader for Skyblock", category = {SKYBLOCK, FEATURE})
    public static boolean wanderingTraderSkyblockTrades = false;


    public static boolean doUsefulComposters = false;
    public static boolean usefulCompostersNeedRedstone = false;

    private static class UsefulCompostersSetting extends Validator<String> {
        @Override public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string) {
            doUsefulComposters = !newValue.toLowerCase(Locale.ROOT).equals("false");
            usefulCompostersNeedRedstone = newValue.toLowerCase(Locale.ROOT).equals("redstone");

            return newValue;
        }
    }

    @Rule(desc = "Composters create sand, red sand, and dirt depending on biome",
            category = {SKYBLOCK, FEATURE},
            options = {"true", "false", "redstone"},
            validate = UsefulCompostersSetting.class
    )
    public static String usefulComposters = "false";
}
