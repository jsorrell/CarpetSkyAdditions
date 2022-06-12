package com.jsorrell.carpetskyadditions.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import com.jsorrell.carpetskyadditions.Build;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static carpet.settings.RuleCategory.FEATURE;

public class SkyAdditionsSettings {
  public static final Logger LOG = LoggerFactory.getLogger(Build.NAME);
  public static final String GENERATION = "generation";
  public static final String WANDERING_TRADER = "wandering_trader";

  /* Generation -- Only obeyed with SkyBlock world generation */
  @Rule(
    desc = "Generates End Portals",
    category = {GENERATION})
  public static boolean generateEndPortals = true;

  @Rule(
    desc = "Generates Silverfish Spawners",
    category = {GENERATION})
  public static boolean generateSilverfishSpawners = true;

  @Rule(
    desc = "Generate Ancient City Portals",
    category = {GENERATION})
  public static boolean generateAncientCityPortals = true;

  @Rule(
    desc = "Generates Magma Cube Spawners",
    category = {GENERATION})
  public static boolean generateMagmaCubeSpawners = false;

  @Rule(
    desc = "Generate Random End Gateways",
    category = {GENERATION})
  public static boolean generateRandomEndGateways = false;

  /* Features -- can be used in any generation, SkyBlock or not.
   *  These all default to false so non-SkyBlock worlds don't accidentally use them.
   *  When a SkyBlock world is created, some are enabled by default using the SkyBlockSetting annotation. */

  /* Wandering Trader */
  private static class TallFlowersTradesNameFix extends SettingFixer {
    @Override
    public String[] alternateNames() {
      return new String[]{"wanderingTraderSkyBlockTrades"};
    }

    @Override
    public FieldPair fix(FieldPair fieldPair) {
      fieldPair.setName("tallFlowersFromWanderingTrader");
      return fieldPair;
    }
  }

  @Rule(
    desc = "Add Tall Flower trades to the Wandering Trader",
    category = {FEATURE, WANDERING_TRADER})
  @SkyAdditionsSetting(value = "true", fixer = TallFlowersTradesNameFix.class)
  public static boolean tallFlowersFromWanderingTrader = false;

  /* Wandering Trader Lava */
  @Rule(
    desc = "Lets the Wandering Trader sell Lava, the old method",
    category = {FEATURE, WANDERING_TRADER})
  public static boolean lavaFromWanderingTrader = false;

  /* Lightning Electrifies Vines */
  @Rule(
    desc = "Lightning striking glowstone with attached vines converts them to glow lichen",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean lightningElectrifiesVines = false;

  /* Renewable Budding Amethysts */
  @Rule(
    desc = "Surrounding lava by calcite and smooth basalt forms budding amethysts",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableBuddingAmethysts = false;

  /* Chorus Plant Generation */
  @Rule(
    desc = "Chorus Plants generate with End Gateways in void",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean gatewaysSpawnChorus = false;

  /* Dolphins Find Hearts of the Sea */
  @Rule(
    desc = "Dolphins can find a heart of the sea when given fish",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableHeartsOfTheSea = false;

  /* Ender Dragons Can Drop Heads */
  @Rule(
    desc = "Ender Dragons killed by Charged Creepers drop their heads",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableDragonHeads = false;

  /* Shulker Spawning */
  private static class ShulkerSpawningNameFix extends SettingFixer {
    @Override
    public String[] alternateNames() {
      return new String[]{"shulkerSpawning"};
    }

    @Override
    public FieldPair fix(FieldPair fieldPair) {
      fieldPair.setName("shulkerSpawnsOnDragonKill");
      return fieldPair;
    }
  }

  @Rule(
    desc = "Shulkers spawn on obsidian pillar when Ender Dragon is re-killed",
    category = {FEATURE})
  @SkyAdditionsSetting(value = "true", fixer = ShulkerSpawningNameFix.class)
  public static boolean shulkerSpawnsOnDragonKill = false;

  /* Anvils Compact Coal into Diamonds */
  @Rule(
    desc = "An Anvil falling on a full stack of Coal Blocks compacts it into a Diamond",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableDiamonds = false;

  /* Goats Ramming Break Nether Wart Blocks */
  @Rule(
    desc = "A Goat ramming a Nether Wart Block will break it apart",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean rammingWart = false;

  /* Foxes Spawn With Berries */
  private static class SweetBerriesFixer extends SettingFixer {
    @Override
    public String[] alternateNames() {
      return new String[]{"foxesSpawnWithBerries"};
    }

    @Override
    public FieldPair fix(FieldPair fieldPair) {
      if (fieldPair.getName().equals("foxesSpawnWithBerries")) {
        fieldPair.setName("foxesSpawnWithSweetBerriesChance");

        if ("true".equalsIgnoreCase(fieldPair.getValue())) {
          Field settingField;
          try {
            settingField = SkyAdditionsSettings.class.getDeclaredField("foxesSpawnWithSweetBerriesChance");
          } catch (Exception e) {
            return null;
          }
          fieldPair.setValue(settingField.getAnnotation(SkyAdditionsSetting.class).value());
        } else if ("false".equalsIgnoreCase(fieldPair.getValue())) {
          fieldPair.setValue("0");
        }
      }

      return fieldPair;
    }
  }

  @Rule(
    desc = "A spawned Fox has a chance to hold Sweet Berries",
    category = {FEATURE},
    options = {"0", "0.2", "1"},
    strict = false,
    validate = Validator.PROBABILITY.class
  )
  @SkyAdditionsSetting(value = "0.2", fixer = SweetBerriesFixer.class)
  public static double foxesSpawnWithSweetBerriesChance = 0d;

  /* Poisonous Potatoes Convert Spiders into Cave Spiders */
  @Rule(
    desc = "Spiders convert into Cave Spiders when given Poisonous Potatoes",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean poisonousPotatoesConvertSpiders = false;

  /* Saplings Placed on Sand Turn into Dead Bushes */
  @Rule(
    desc = "Saplings on Sand eventually turn into Dead Bushes",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean saplingsDieOnSand = false;

  @Rule(
    desc = "Creatures with Echolocation Drop Echo Shards when Killed with Sonic Booms",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableEchoShards = false;

  @Rule(
    desc = "Vexes can be converted into Allays with music",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableAllays = false;

  @Rule(
    desc = "Dead Coral with Water flowing out of it erodes into Sand",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean coralErosion = false;

  @Rule(
    desc = "Huge Mushrooms nearby convert soil to Mycelium",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean hugeMushroomsSpreadMycelium = false;

  @Rule(
    desc = "Netherrack generates as part of Nether Portals spawned floating",
    category = {FEATURE})
  @SkyAdditionsSetting("true")
  public static boolean renewableNetherrack = false;

  private static class RenewableDeepslateSetting extends Validator<String> {
    @Override
    public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string) {
      SkyAdditionsSettings.doRenewableDeepslate = !"false".equalsIgnoreCase(newValue);
      SkyAdditionsSettings.renewableDeepslateFromSplash = "true".equalsIgnoreCase(newValue);

      return newValue;
    }
  }

  @Rule(
    desc = "Stone can be converted into deepslate with thick potions",
    extra = {
      "either by clicking/dispensing them or with splash potions",
      "With no_splash: splash potion conversion is disabled"
    },
    category = {FEATURE},
    options = {"true", "false", "no_splash"},
    validate = RenewableDeepslateSetting.class
  )
  @SkyAdditionsSetting("true")
  @SuppressWarnings("unused")
  public static String renewableDeepslate = "false";
  public static boolean doRenewableDeepslate = false;
  public static boolean renewableDeepslateFromSplash = false;

  @Rule(
    desc = "Enchanting Tables Near Wardens can Enchant Items with Swift Sneak",
    category = {FEATURE}
  )
  @SkyAdditionsSetting("true")
  public static boolean renewableSwiftSneak = false;

  /* Wandering Trader Spawn Chance */
  public static class WanderingTraderSpawnChanceValidator extends Validator<Double> {
    @Override
    public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string) {
      return (0.025 <= newValue && newValue <= 1) ? newValue : null;
    }

    @Override
    public String description() {
      return "Must be between 0.025 and 1";
    }
  }

  @Rule(
    desc = "Max wandering trader spawn chance",
    extra = {
      "This starts at 0.025 and increases by 0.025 each time a trader spawn fails",
      "until the max chance is reached."
    },
    category = {WANDERING_TRADER},
    options = {"0.075", "0.2", "1"},
    strict = false,
    validate = WanderingTraderSpawnChanceValidator.class
  )
  public static double maxWanderingTraderSpawnChance = 0.075;

  /* Wandering Trader Spawn Rate */
  public static class POSITIVE_NUMBER<T extends Number> extends Validator<T> {
    @Override
    public T validate(ServerCommandSource source, ParsedRule<T> currentRule, T newValue, String string) {
      return 0 < newValue.doubleValue() ? newValue : null;
    }

    @Override
    public String description() {
      return "Must be a positive number";
    }
  }

  @Rule(
    desc = "How often the wandering trader attempts to spawn in ticks",
    category = {WANDERING_TRADER},
    options = {"6000", "24000", "72000"},
    strict = false,
    validate = POSITIVE_NUMBER.class
  )
  public static int wanderingTraderSpawnRate = 24000;
}
