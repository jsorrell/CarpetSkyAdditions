package com.jsorrell.skyblock;

import static carpet.settings.RuleCategory.FEATURE;

import java.util.Locale;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;

import net.minecraft.server.command.ServerCommandSource;

public class SkyBlockSettings {
  public static final String SKYBLOCK = "skyblock";

  @Rule(name = "enable", desc = "Master switch for SkyBlock features", category = SKYBLOCK)
  public static boolean enableSkyBlockFeatures = false;

  /* Wandering Trader */
  @Rule(
      desc = "Add trades to the wandering trader for SkyBlock",
      category = {SKYBLOCK, FEATURE})
  public static boolean wanderingTraderSkyBlockTrades = true;

  /* Lightning Electrifies Vines */
  @Rule(
      desc = "Lightning striking glowstone with attached vines converts them to glow lichen",
      category = {SKYBLOCK, FEATURE})
  public static boolean lightningElectrifiesVines = true;

  /* Renewable Budding Amethysts */
  @Rule(
      desc = "Surrounding lava by calcite and smooth basalt forms budding amethysts",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableBuddingAmethysts = true;

  /* Chorus Plant Generation */
  @Rule(
      desc = "Chorus Plants generate with End Gateways in void",
      category = {SKYBLOCK, FEATURE})
  public static boolean gatewaysSpawnChorus = true;

  /* Dolphins Find Hearts of the Sea */
  @Rule(
      desc = "Dolphins can find a heart of the sea when given fish",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableHeartsOfTheSea = true;

  /* Ender Dragons Can Drop Heads */
  @Rule(
      desc = "Ender Dragons killed by Charged Creepers drop their heads",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableDragonHeads = true;

  /* Shulker Spawning */
  @Rule(
      desc = "Shulkers spawn on obsidian pillar when Ender Dragon is re-killed",
      category = {SKYBLOCK, FEATURE})
  public static boolean shulkerSpawning = true;

  /* Anvils Compact Coal into Diamonds */
  @Rule(
      desc = "An Anvil falling on a full stack of Coal Blocks compacts it into a Diamond",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableDiamonds = true;

  /* Goats Ramming Break Nether Wart Blocks */
  @Rule(
      desc = "A Goat ramming a Nether Wart Block will break it apart",
      category = {SKYBLOCK, FEATURE})
  public static boolean rammingWart = true;

  /* Useful Composters */
  public static boolean doUsefulComposters = false;
  public static boolean usefulCompostersNeedRedstone = false;

  private static class UsefulCompostersSetting extends Validator<String> {
    @Override
    public String validate(
        ServerCommandSource source,
        ParsedRule<String> currentRule,
        String newValue,
        String string) {
      doUsefulComposters = !newValue.toLowerCase(Locale.ROOT).equals("false");
      usefulCompostersNeedRedstone = newValue.toLowerCase(Locale.ROOT).equals("redstone");

      return newValue;
    }
  }

  @Rule(
      desc = "Composters create sand, red sand, and dirt depending on biome",
      category = {SKYBLOCK, FEATURE},
      options = {"true", "false", "redstone"},
      validate = UsefulCompostersSetting.class)
  @SuppressWarnings("unused")
  public static String usefulComposters = "false";
}
