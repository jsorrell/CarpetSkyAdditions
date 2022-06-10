# Carpet SkyBlock Utils

[![GitHub downloads](https://img.shields.io/github/downloads/jsorrell/skyblock/total?label=Github%20downloads&logo=github)](https://github.com/jsorrell/skyblock/releases)

**English** | [中文](docs/zh_cn/README.md)

SkyBlock Utils is a module for [fabric-carpet](https://github.com/gnembon/fabric-carpet)
originally based on [skyrising/skyblock](https://github.com/skyrising/skyblock).

This mod aims to provide an expert-level SkyBlock gameplay that depends on players' knowledge of Minecraft mechanics.
In some cases, outside tools such as [Chunkbase](https://www.chunkbase.com/)
or [MiniHUD](https://www.curseforge.com/minecraft/mc-mods/minihud) will be helpful.
Usage of these is encouraged.
Sometimes extended grinding or AFKing will be required for progression.
Unless SkyBlock world generation is chosen or features are specifically enabled, the mod will do nothing.
This means SkyBlock and Non-SkyBlock worlds can be switched between easily without restarting the
client.

## Installation

This is a Fabric Mod and installed as such. It requires `fabric-carpet` and the `fabric-api` installed as well.

Once installed, run `datapack enable "skyblock/skyblock"` to enable the datapack

[Detailed Installation Instructions](docs/en_us/installation.md)

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

### Progression Walkthrough

If you get stuck, a general progression walkthrough is available [here](docs/en_us/progression.md).


## Acknowledgements

- [@skyrising](https://github.com/skyrising/skyblock) for the initial mod idea and some source code

- [@DeadlyMC](https://github.com/DeadlyMC/Skyblock-datapack) for the initial ideas for the datapack

- [@gnembon](https://github.com/gnembon/fabric-carpet) for `fabric-carpet`

## License

This project is licensed under the terms of the MIT license.
