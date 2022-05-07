package com.jsorrell.skyblock.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static carpet.settings.RuleCategory.FEATURE;

public class SkyBlockSettings {
  public static final Logger LOG = LoggerFactory.getLogger("skyblock");
  public static final String SKYBLOCK = "skyblock";
  public static final String GENERATION = "generation";

  /* Generation -- Only obeyed with SkyBlock world generation */
  @Rule(
    desc = "Generates End Portals",
    category = {SKYBLOCK, GENERATION})
  public static boolean generateEndPortals = true;

  @Rule(
    desc = "Generates Silverfish Spawners",
    category = {SKYBLOCK, GENERATION})
  public static boolean generateSilverfishSpawners = true;

  @Rule(
    desc = "Generate Ancient City Portals",
    category = {SKYBLOCK, GENERATION})
  public static boolean generateAncientCityPortals = true;

  @Rule(
    desc = "Generates Magma Cube Spawners",
    category = {SKYBLOCK, GENERATION})
  public static boolean generateMagmaCubeSpawners = false;

  @Rule(
    desc = "Generate Random End Gateways",
    category = {SKYBLOCK, GENERATION})
  public static boolean generateRandomEndGateways = false;

  /* Features -- can be use in any generation, SkyBlock or not.
   *  These all default to false so non-SkyBlock worlds don't accidentally use them.
   *  When a SkyBlock world is created, some are enabled by default using the SkyBlockSetting annotation. */

  /* Wandering Trader */
  @Rule(
    desc = "Add trades to the wandering trader for SkyBlock",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean wanderingTraderSkyBlockTrades = false;

  /* Wandering Trader Lava */
  @Rule(
    desc = "Lets the Wandering Trader sell Lava, the old method",
    category = {SKYBLOCK, FEATURE})
  public static boolean lavaFromWanderingTrader = false;

  /* Lightning Electrifies Vines */
  @Rule(
    desc = "Lightning striking glowstone with attached vines converts them to glow lichen",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean lightningElectrifiesVines = false;

  /* Renewable Budding Amethysts */
  @Rule(
    desc = "Surrounding lava by calcite and smooth basalt forms budding amethysts",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableBuddingAmethysts = false;

  /* Chorus Plant Generation */
  @Rule(
    desc = "Chorus Plants generate with End Gateways in void",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean gatewaysSpawnChorus = false;

  /* Dolphins Find Hearts of the Sea */
  @Rule(
    desc = "Dolphins can find a heart of the sea when given fish",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableHeartsOfTheSea = false;

  /* Ender Dragons Can Drop Heads */
  @Rule(
    desc = "Ender Dragons killed by Charged Creepers drop their heads",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableDragonHeads = false;

  /* Shulker Spawning */
  @Rule(
    desc = "Shulkers spawn on obsidian pillar when Ender Dragon is re-killed",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean shulkerSpawning = false;

  /* Anvils Compact Coal into Diamonds */
  @Rule(
    desc = "An Anvil falling on a full stack of Coal Blocks compacts it into a Diamond",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableDiamonds = false;

  /* Goats Ramming Break Nether Wart Blocks */
  @Rule(
    desc = "A Goat ramming a Nether Wart Block will break it apart",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
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
        fieldPair.setName("foxesSpawnWithBerriesChance");

        if ("true".equalsIgnoreCase(fieldPair.getValue())) {
          Field settingField;
          try {
            settingField = SkyBlockSettings.class.getDeclaredField("foxesSpawnWithBerriesChance");
          } catch (Exception e) {
            return null;
          }
          fieldPair.setValue(settingField.getAnnotation(SkyBlockSetting.class).value());
        } else if ("false".equalsIgnoreCase(fieldPair.getValue())) {
          fieldPair.setValue("0");
        }
      }

      return fieldPair;
    }
  }

  @Rule(
    desc = "A spawned Fox has a chance to hold Sweet Berries",
    category = {SKYBLOCK, FEATURE},
    options = {"0", "0.2", "1"},
    strict = false,
    validate = Validator.PROBABILITY.class
  )
  @SkyBlockSetting(value = "0.2", fixer = SweetBerriesFixer.class)
  public static double foxesSpawnWithBerriesChance = 0d;

  /* Poisonous Potatoes Convert Spiders into Cave Spiders */
  @Rule(
    desc = "Spiders convert into Cave Spiders when given Poisonous Potatoes",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean poisonousPotatoesConvertSpiders = false;

  /* Saplings Placed on Sand Turn into Dead Bushes */
  @Rule(
    desc = "Saplings on Sand eventually turn into Dead Bushes",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean saplingsDieOnSand = false;

  @Rule(
    desc = "Creatures with Echolocation Drop Echo Shardes when Killed with Sonic Booms",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableEchoShards = false;

  @Rule(
    desc = "Vexes can be converted into Allays with music",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableAllays = false;

  @Rule(
    desc = "Netherrack generates as part of Nether Portals spawned floating",
    category = {SKYBLOCK, FEATURE})
  @SkyBlockSetting("true")
  public static boolean renewableNetherrack = false;

  private static class RenewableDeepslateSetting extends Validator<String> {
    @Override
    public String validate(ServerCommandSource source, ParsedRule<String> currentRule, String newValue, String string) {
      SkyBlockSettings.doRenewableDeepslate = !"false".equalsIgnoreCase(newValue);
      SkyBlockSettings.renewableDeepslateFromSplash = "true".equalsIgnoreCase(newValue);

      return newValue;
    }
  }

  @Rule(
    desc = "Stone can be converted into deepslate with thick potions",
    extra = {
      "either by clicking/dispensing them or with splash potions",
      "With no_splash: splash potion conversion is disabled"
    },
    category = {SKYBLOCK, FEATURE},
    options = {"true", "false", "no_splash"},
    validate = RenewableDeepslateSetting.class
  )
  @SkyBlockSetting("true")
  public static String renewableDeepslate = "false";

  public static boolean doRenewableDeepslate = false;
  public static boolean renewableDeepslateFromSplash = false;
}
