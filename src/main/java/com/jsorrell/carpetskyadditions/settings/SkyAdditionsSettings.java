package com.jsorrell.carpetskyadditions.settings;

import static carpet.api.settings.RuleCategory.COMMAND;
import static carpet.api.settings.RuleCategory.FEATURE;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyAdditionsSettings {
    public static final Logger LOG = LoggerFactory.getLogger(SkyAdditionsExtension.MOD_NAME);
    public static final String GENERATION = "generation";
    public static final String WANDERING_TRADER = "wandering_trader";

    @Rule(categories = COMMAND)
    public static String commandSkyIsland = "ops";

    /* Generation -- Only obeyed with SkyBlock world generation */
    @Rule(categories = GENERATION)
    public static boolean generateEndPortals = true;

    @Rule(categories = GENERATION)
    public static boolean generateSilverfishSpawners = true;

    @Rule(categories = GENERATION)
    public static boolean generateAncientCityPortals = true;

    @Rule(categories = GENERATION)
    public static boolean generateMagmaCubeSpawners = false;

    @Rule(categories = GENERATION)
    public static boolean generateRandomEndGateways = false;

    /* Features -- can be used in any generation, SkyBlock or not.
     *  These all default to false so this mod is impotent by default like Carpet.
     *  When a SkyBlock world is created, some are enabled by default using the SkyBlockSetting annotation. */

    /* Wandering Trader */
    private static class TallFlowersTradesNameFix extends SettingFixer {
        @Override
        public String[] names() {
            return new String[] {"wanderingTraderSkyBlockTrades", "tallFlowersFromWanderingTrader"};
        }

        @Override
        public Optional<FieldPair> fix(FieldPair fieldPair) {
            fieldPair.setName("tallFlowersFromWanderingTrader");
            return Optional.of(fieldPair);
        }
    }

    @Rule(categories = {FEATURE, WANDERING_TRADER})
    @SkyAdditionsSetting(value = "true", fixer = TallFlowersTradesNameFix.class)
    public static boolean tallFlowersFromWanderingTrader = false;

    /* Wandering Trader Lava */
    @Rule(categories = {FEATURE, WANDERING_TRADER})
    public static boolean lavaFromWanderingTrader = false;

    /* Lightning Electrifies Vines */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean lightningElectrifiesVines = false;

    /* Renewable Budding Amethysts */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableBuddingAmethysts = false;

    /* Chorus Plant Generation */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean gatewaysSpawnChorus = false;

    /* Dolphins Find Hearts of the Sea */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableHeartsOfTheSea = false;

    /* Ender Dragons Can Drop Heads */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableDragonHeads = false;

    /* Shulker Spawning */
    private static class ShulkerSpawningNameFix extends SettingFixer {
        @Override
        public String[] names() {
            return new String[] {"shulkerSpawning", "shulkerSpawnsOnDragonKill"};
        }

        @Override
        public Optional<FieldPair> fix(FieldPair fieldPair) {
            fieldPair.setName("shulkerSpawnsOnDragonKill");
            return Optional.of(fieldPair);
        }
    }

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting(value = "true", fixer = ShulkerSpawningNameFix.class)
    public static boolean shulkerSpawnsOnDragonKill = false;

    /* Anvils Compact Coal into Diamonds */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableDiamonds = false;

    /* Goats Ramming Break Nether Wart Blocks */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean rammingWart = false;

    /* Foxes Spawn With Berries */
    private static class SweetBerriesFixer extends SettingFixer {
        @Override
        public String[] names() {
            return new String[] {"foxesSpawnWithBerries", "foxesSpawnWithSweetBerriesChance"};
        }

        @Override
        public Optional<FieldPair> fix(FieldPair fieldPair) {
            if (fieldPair.getName().equals("foxesSpawnWithBerries")) {
                fieldPair.setName("foxesSpawnWithSweetBerriesChance");

                if ("true".equalsIgnoreCase(fieldPair.getValue())) {
                    fieldPair.setValue("0.2");
                } else if ("false".equalsIgnoreCase(fieldPair.getValue())) {
                    fieldPair.setValue("0");
                }
            }

            return Optional.of(fieldPair);
        }
    }

    @Rule(
            categories = FEATURE,
            options = {"0", "0.2", "1"},
            strict = false,
            validators = Validators.Probablity.class)
    @SkyAdditionsSetting(value = "0.2", fixer = SweetBerriesFixer.class)
    public static double foxesSpawnWithSweetBerriesChance = 0d;

    /* Poisonous Potatoes Convert Spiders into Cave Spiders */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean poisonousPotatoesConvertSpiders = false;

    /* Saplings Placed on Sand Turn into Dead Bushes */
    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean saplingsDieOnSand = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableEchoShards = false;

    private static class AllayableVexesFixer extends SettingFixer {
        @Override
        public String[] names() {
            return new String[] {"renewableAllays", "allayableVexes"};
        }

        @Override
        public Optional<FieldPair> fix(FieldPair fieldPair) {
            fieldPair.setName("allayableVexes");
            return Optional.of(fieldPair);
        }
    }

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting(value = "true", fixer = AllayableVexesFixer.class)
    public static boolean allayableVexes = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean coralErosion = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean hugeMushroomsSpreadMycelium = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableNetherrack = false;

    private static class RenewableDeepslateSetting extends Validator<String> {
        @Override
        public String validate(
                CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            SkyAdditionsSettings.doRenewableDeepslate = !"false".equalsIgnoreCase(newValue);
            SkyAdditionsSettings.renewableDeepslateFromSplash = "true".equalsIgnoreCase(newValue);

            return newValue;
        }
    }

    @Rule(
            categories = FEATURE,
            options = {"true", "false", "no_splash"},
            validators = RenewableDeepslateSetting.class)
    @SkyAdditionsSetting("true")
    @SuppressWarnings("unused")
    public static String renewableDeepslate = "false";

    public static boolean doRenewableDeepslate = false;
    public static boolean renewableDeepslateFromSplash = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean renewableSwiftSneak = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean traderCamels = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean sniffersFromDrowneds = false;

    private static class SuspiciousSniffersSetting extends Validator<String> {
        @Override
        public String validate(
                CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            SkyAdditionsSettings.doSuspiciousSniffers = !"false".equalsIgnoreCase(newValue);
            SkyAdditionsSettings.ironFromSniffers = "true".equalsIgnoreCase(newValue);

            return newValue;
        }
    }

    @Rule(
            categories = FEATURE,
            options = {"true", "false", "no_iron"},
            validators = SuspiciousSniffersSetting.class)
    @SkyAdditionsSetting("true")
    @SuppressWarnings("unused")
    public static String suspiciousSniffers = "false";

    public static boolean doSuspiciousSniffers = false;
    public static boolean ironFromSniffers = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean spreadingSmallDripleaves = false;

    @Rule(categories = FEATURE)
    @SkyAdditionsSetting("true")
    public static boolean spreadingCoral = false;

    /* Wandering Trader Spawn Chance */
    public static class WanderingTraderSpawnChanceValidator extends Validator<Double> {
        @Override
        public Double validate(
                CommandSourceStack source, CarpetRule<Double> currentRule, Double newValue, String string) {
            return (0.025 <= newValue && newValue <= 1) ? newValue : null;
        }

        @Override
        public String description() {
            return "Must be between 0.025 and 1";
        }
    }

    @Rule(
            categories = WANDERING_TRADER,
            options = {"0.075", "0.2", "1"},
            strict = false,
            validators = WanderingTraderSpawnChanceValidator.class)
    public static double maxWanderingTraderSpawnChance = 0.075;

    /* Wandering Trader Spawn Rate */
    public static class POSITIVE_NUMBER<T extends Number> extends Validator<T> {
        @Override
        public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
            return 0 < newValue.doubleValue() ? newValue : null;
        }

        @Override
        public String description() {
            return "Must be a positive number";
        }
    }

    @Rule(
            categories = WANDERING_TRADER,
            options = {"6000", "24000", "72000"},
            strict = false,
            validators = POSITIVE_NUMBER.class)
    public static int wanderingTraderSpawnRate = 24000;
}
