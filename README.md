# Carpet Sky Additions

[![GitHub downloads](https://img.shields.io/github/downloads/jsorrell/CarpetSkyAdditions/total?label=Github%20downloads&logo=github)](https://github.com/jsorrell/CarpetSkyAdditions/releases)
[![CurseForge downloads](http://cf.way2muchnoise.eu/full_633402_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-sky-additions)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/3oX3JnAP?label=Modrinth%20Downloads)](https://modrinth.com/mod/carpet-sky-additions)

[![en](https://img.shields.io/badge/lang-en-red.svg)](/README.md)
[![zh_cn](https://img.shields.io/badge/lang-zh--cn-yellow.svg)](/docs/zh_cn/README.md)

Carpet Sky Additions is a module for [fabric-carpet](https://github.com/gnembon/fabric-carpet)
originally based on [skyrising/skyblock](https://github.com/skyrising/skyblock).

This mod aims to provide an expert-level SkyBlock style gameplay that depends on players' knowledge of Minecraft
mechanics.
In some cases, outside tools such as [Chunkbase](https://www.chunkbase.com/)
or [MiniHUD](https://www.curseforge.com/minecraft/mc-mods/minihud) will be helpful.
Usage of these is encouraged.
Sometimes extended grinding or AFKing will be required for progression.
Unless SkyBlock world generation is chosen or features are specifically enabled, the mod will do nothing.
This means SkyBlock and Non-SkyBlock worlds can be switched between easily without restarting the
client.

## Installation

[![Vanilla Sky: Everything from Nothing](http://cf.way2muchnoise.eu/title/624853.svg)](https://www.curseforge.com/minecraft/modpacks/vanilla-sky)

The easiest way to use this mod is to install the modpack called ***Vanilla Sky: Everything from Nothing*** which is
available on CurseForge.

To create a new SkyBlock world, choose `World Type: SkyBlock` and enable the datapack `"carpetskyadditions/skyblock"`.

If you want a harder challenge, ***also*** enable the datapack `"carpetskyadditions/skyblock_acacia"` to start with an
Acacia
tree instead of an Oak tree.

For custom or server installations, follow the [Detailed Installation Instructions](docs/en_us/installation.md)

## Features

### SkyBlock Generation

A SkyBlock world generates exactly like a Default generation world, but with every block removed. Biomes and Structure
Bounding Boxes are kept in place. This means Husks will still spawn in Deserts and Blazes will spawn in Nether
Fortresses, for example. Even with almost every block removed, you have access to most things in the game.

[More Generation Details](docs/en_us/generation.md)

### Gameplay Changes ###

SkyBlock generation does, however, leave a few unobtainable resources.
In addition to adding SkyBlock generation, this mod also fills in
the gaps, making as minimal and Minecrafty changes as possible.

The biggest progress-blocker is Lava, which is unobtainable by default.
This prevents going to the Nether or End or getting Cobblestone.
This mod fixes that problem by providing a way to get Lava.

Sand is also very limited in a default SkyBlock world, but the mod allows for more to be created.

Most other resources provided by this mod are cosmetic and don't make major changes to progression, such as Dead Bushes
and Ender Dragon Heads.

When possible, changes were added to a datapack instead of being programmed into the mod for ease of user customization.
The datapack is built into the mod.

SkyBlock advancements were also added to guide progression and document the mod's changes to vanilla.

*When installed with default settings, all blocks, items, mobs, and advancements obtainable in Default generation are
obtainable in SkyBlock generation.*

[List of Mod Features](docs/en_us/features.md)

[List of Datapack Features](docs/en_us/datapack.md)

### Commands
This mod provides a command for generating islands,
which simplifies having multiple players on the same server with different starting islands.

[List of Mod Commands](docs/en_us/commands.md)

### Progression Walkthrough

If you get stuck, a general progression walkthrough is available [here](docs/en_us/progression.md).

## Translations

The mod and the datapack are available for translation using [CrowdIn](https://crowdin.com/project/carpetskyadditions).
If you are able to add translations, your help would be much appreciated.

## Acknowledgements

- [@skyrising](https://github.com/skyrising/skyblock) for the initial mod idea

- [@DeadlyMC](https://github.com/DeadlyMC/Skyblock-datapack) for the initial ideas for the datapack

- [@gnembon](https://github.com/gnembon/fabric-carpet) for `fabric-carpet`

- All the translators on CrowdIn

## License

This project is licensed under the terms of the MIT license.
