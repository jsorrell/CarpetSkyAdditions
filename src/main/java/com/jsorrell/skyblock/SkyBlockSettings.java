package com.jsorrell.skyblock;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.FEATURE;

public class SkyBlockSettings {
  public static final String SKYBLOCK = "skyblock";

  /* Wandering Trader */
  @Rule(
      desc = "Add trades to the wandering trader for SkyBlock",
      category = {SKYBLOCK, FEATURE})
  public static boolean wanderingTraderSkyBlockTrades = false;

  /* Lightning Electrifies Vines */
  @Rule(
      desc = "Lightning striking glowstone with attached vines converts them to glow lichen",
      category = {SKYBLOCK, FEATURE})
  public static boolean lightningElectrifiesVines = false;

  /* Renewable Budding Amethysts */
  @Rule(
      desc = "Surrounding lava by calcite and smooth basalt forms budding amethysts",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableBuddingAmethysts = false;

  /* Chorus Plant Generation */
  @Rule(
      desc = "Chorus Plants generate with End Gateways in void",
      category = {SKYBLOCK, FEATURE})
  public static boolean gatewaysSpawnChorus = false;

  /* Dolphins Find Hearts of the Sea */
  @Rule(
      desc = "Dolphins can find a heart of the sea when given fish",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableHeartsOfTheSea = false;

  /* Ender Dragons Can Drop Heads */
  @Rule(
      desc = "Ender Dragons killed by Charged Creepers drop their heads",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableDragonHeads = false;

  /* Shulker Spawning */
  @Rule(
      desc = "Shulkers spawn on obsidian pillar when Ender Dragon is re-killed",
      category = {SKYBLOCK, FEATURE})
  public static boolean shulkerSpawning = false;

  /* Anvils Compact Coal into Diamonds */
  @Rule(
      desc = "An Anvil falling on a full stack of Coal Blocks compacts it into a Diamond",
      category = {SKYBLOCK, FEATURE})
  public static boolean renewableDiamonds = false;

  /* Goats Ramming Break Nether Wart Blocks */
  @Rule(
      desc = "A Goat ramming a Nether Wart Block will break it apart",
      category = {SKYBLOCK, FEATURE})
  public static boolean rammingWart = false;

  /* Foxes Spawn With Berries */
  @Rule(
      desc = "A spawned Fox has a chance to hold Sweet Berries",
      category = {SKYBLOCK, FEATURE})
  public static boolean foxesSpawnWithBerries = false;

  /* Poisonous Potatoes Convert Spiders into Cave Spiders */
  @Rule(
      desc = "Spiders convert into Cave Spiders when given Poisonous Potatoes",
      category = {SKYBLOCK, FEATURE})
  public static boolean poisonousPotatoesConvertSpiders = false;

  /* Saplings Placed on Sand Turn into Dead Bushes */
  @Rule(
      desc = "Saplings on Sand eventually turn into Dead Bushes",
      category = {SKYBLOCK, FEATURE})
  public static boolean saplingsDieOnSand = false;
}
