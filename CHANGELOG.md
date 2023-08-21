# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

## [4.4.1] - 2023-08-21

### Fixed
* #136 where any potion bottle could convert Stone to Deepslate on click
* #139 where placing any block could trigger "Coral Utopia"

## [4.4.0] - 2023-08-03

### Added
* 11 challenge advancements, allowing previously unobtainable smithing templates to be obtained upon completion.
* Vanilla War Pigs advancement is now disabled.
* New `traderCamels` setting
  * Allows Camels to be obtained.
  * When enabled, Wandering Traders that spawn in Deserts and Badlands will spawn riding Camels.
  * Enabled by default on new SkyBlock worlds.
    * To enable on old worlds, run `/carpetskyadditions setDefault traderCamels true`.
* New advancement "Hump Day Trader" that activates when a camel is ridden.
* New `sniffersFromDrowneds` setting
  * Allows Sniffer Eggs to be obtained.
  * When enabled, Drowned may spawn holding Sniffer Eggs that they place when they stomp Turtle Eggs.
  * Enabled by default on new SkyBlock worlds.
    * To enable on old worlds, run `/carpetskyadditions setDefault sniffersFromDrowneds true`.
* New advancement "Prehistoric Foster Parent" that activates when a Sniffer Egg is obtained.
* New `suspiciousSniffers` setting
  * Allows Suspicious Sand and Suspicious Gravel to be obtained.
  * When enabled, Sniffers may convert Sand and Gravel into their suspicious version
    when digging them inside a structure that generates it.
  * Enabled by default on new SkyBlock worlds.
    * To enable on old worlds, run `/carpetskyadditions setDefault suspiciousSniffers true`.
* New advancement "Whiff of Treasure" that activates when Suspicious Sand or Gravel is brushed.
* New advancement "Master Archaeologist" that activates when all exclusive Archaeology loot is obtained.
* New `spreadingSmallDripleaves` setting
  * Allows additionals Small Dripleaves to be obtained.
  * When enabled, half-submerged Dripleaves on Clay with a light level of exactly 5 on top can spread.
  * Enabled by default on new SkyBlock worlds.
    * To enable on old worlds, run `/carpetskyadditions setDefault spreadingSmallDripleaves true`.
* New advancement "Perfect Growing Conditions" that actives when placing a Small Dripleaf where it can spread.
* New `spreadingCoral` setting
  * Allows additionals Coral Blocks to be obtained.
  * When enabled, Calcite can convert to a Coral Block when there are nearby Coral Blocks.
  * Enabled by default on new SkyBlock worlds.
    * To enable on old worlds, run `/carpetskyadditions setDefault spreadingCoral true`.
* New advancement "Reef Builder" that activates when placing Calcite where Coral can spread to it.
* New advancement "Coral Utopia" that activates when placing Calcite at high Coral suitability.

### Changed
* Beetroot seeds removed from Day Trader advancement because they are also obtainable through archaeology.
* Changed icon of "Day Trader" advancement to Pumpkin.
* Changed icon of "Cooking Stone in a Stone Oven" to Tuff.

### Fixed
* #125 where trying to generate island number 44 with the /skyisland would cause a crash.

## [4.3.2] - 2023-06-19

### Added
* Support for 1.20 and 1.20.1

### Changed
* Arborist and Day Trader advancements now require cherry saplings

## [4.3.1] - 2023-04-08

### Changed
* Datapack settings changes now update without a client restart.

### Fixed
* Ferns added to the Day Trader advancement for Acacia Tree starts (#101).
* Removed injection into Fabric API internals which fixes Quilt support (#94).
* Fixed crash when converting a Vex into an Allay with Note Blocks.

## [4.3.0] - 2023-03-20

### Added
* Support for Minecraft 1.19.4
* The spawn platform now uses a configured feature and its location (absolute or relative to spawn) can be set using a datapack.
* Added `skyisland` command simplifying the generation of per-user or per-team spawn islands.

### Changed
* Reworked renewable deepslate so it no longer converts all blocks hit by the splash potion into deepslate.
  There is probability depending on how much a potion effect would be applied.
  With over half the potion length applied, conversion is guaranteed.
* Gateways generated when `gatewaysSpawnChorus` is `true` should now generate 10 blocks above the End Stone, not 10 blocks above the highest chorus flower.
* Removed the sign from the default spawn platform.
* Embedded datapack translations into the mod. The translation pack is now only required for non-English server clients without the mod.
* Translations now fall back to the server language when the client lacks the translation in their own language (1.19.4 only).

### Removed
* Support for Minecraft 1.19.2.

### Fixed
* Ferns added to the Day Trader advancement for Oak Tree starts (#101).
* Conversion should no longer depend on location or the velocity of the potion entity (#104).
* Chorus will no longer generate on every End Island when `gatewaysSpawnChorus` is `true`, only those created by End Gateways.
* Get Down advancement no longer triggers when falling into the void.
* The config setting `defaultToSkyBlockWorld` now changes the default server `level-type` to `carpetskyadditions:skyblock`.
* The config setting `initialTreeType` now accepts any capitalization of oak or acacia.

## [4.2.0] - 2023-01-11

### Added
* Spanish, partial Finnish, and partial Danish translations.
* Add a configuration file that allows SkyBlock worlds to be set as default.
  Configuration works with `Mod Menu`.

### Changed
* Ores now have a crafting recipe instead of a Smithing Table recipe, so they can be better balanced.
* Ore drops are once again vanilla.
* `Cloth Config API` is now a dependency.

### Fixed
* Raw Gold, Iron, and Copper are once again obtainable (#81).
* Anvils converting Coal into Diamonds now works better with larger-than-vanilla max stack sizes.
* Nether portals once again only generate Netherrack/Nylium when forced.

[Unreleased]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.4.1...HEAD
[4.4.1]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.4.0...v4.4.1
[4.4.0]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.3.2...v4.4.0
[4.3.2]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.3.1...v4.3.2
[4.3.1]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.3.0...v4.3.1
[4.3.0]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.2.0...v4.3.0
[4.2.0]: https://github.com/jsorrell/carpetskyadditions/releases/tag/v4.2.0
