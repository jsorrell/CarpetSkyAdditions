# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed
* Ferns added to the Day Trader advancement for Acacia Tree starts (#101).

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

[Unreleased]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.3.0...HEAD
[4.3.0]: https://github.com/jsorrell/CarpetSkyAdditions/compare/v4.2.0...v4.3.0
[4.2.0]: https://github.com/jsorrell/carpetskyadditions/releases/tag/v4.2.0
