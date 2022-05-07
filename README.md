# SkyBlock

**English**|[中文](README_cn.md)

SkyBlock is a module for [fabric-carpet](https://github.com/gnembon/fabric-carpet)
originally based on [skyrising/skyblock](https://github.com/skyrising/skyblock).

This mod aims to provide an expert level SkyBlock gameplay that depends on players' knowledge of Minecraft mechanics.
In some cases, outside tools such as [Chunkbase](https://www.chunkbase.com/)
or [MiniHUD](https://www.curseforge.com/minecraft/mc-mods/minihud) will be helpful.
Usage of these is encouraged.
Sometimes extended grinding or AFK sessions will be required for progression.
Unless SkyBlock world generation is chosen or features are specifically enabled, the mod will do nothing.
This means in Single Player, SkyBlock and Non-SkyBlock worlds can be switch between easily without restarting the
client.

## Installation

- Install [Fabric](https://fabricmc.net/use)
- Download [fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/)
- Download [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
- Download [SkyBlock and the Datapack](https://github.com/jsorrell/skyblock/releases)
- Place fabric-carpet, fabric-api, and SkyBlock into `<minecraft-directory>/mods/`

## Usage

### Singleplayer

- `Create New World`
- Select the downloaded Datapack
- Set Allow Cheats to `ON` so you will be able to enable/disable mod features
- Click on `More World Options...`
- Choose `World Type: SkyBlock`
- Create the world

### Multiplayer

The mod and datapack are only required on the server.

- Start the server to generate a template `server.properties` file
- Shut down the server
- Delete `world` folder the server created
- Open `server.properties`
- Change `level-type=minecraft\:normal` to `level-type=skyblock\:skyblock`
- Start the server to generate the `world` folder
- Shut down the server again
- Move the downloaded datapack to the `world -> datapacks` folder
- The SkyBlock server should now be ready

## Features

### SkyBlock Generation

A SkyBlock world generates exactly like a Default generation world, but with every block removed. Biomes and Structure
Bounding Boxes are kept in place. This means Husks will still spawn in Deserts and Blazes will spawn in Nether
Fortresses, for example.

Only a few things are generated:

- A small starting island where you spawn:
  ![small spawn platform with grass, mycelium, nylium, and a tree](screenshots/spawn_platform.png?raw=true "Spawn Platform")

- End Portal Frames (unless `generateEndPortals` is set to `false`):
  ![end portal frame remains](screenshots/end_portal.png?raw=true "End Portal Frame")

- Ancient City Portal Frames with a Sculk Shrieker (unless `generateAncientCityPortals` is set to `false`):
  ![ancient city portal frame remains](screenshots/ancient_city_portal.png?raw=true "Ancient City Portal Frame")

- Silverfish spawners (unless `generateSilverfishSpawners` is set to `false`)

- Magma Cube spawners in Treasure Room Bastion Remnants (only if `generateMagmaCubeSpawners` is set to `true`)

- Random End Gateways throughout the End (only if `generateRandomEndGateways` is set to `true`)

Even with every block removed, almost every block is obtainable and almost every mob is spawnable, using only *Vanilla*
features.

### Additional Mod Features ###

SkyBlock generation does, however, leave a few unobtainable resources. In addition to generation, this mod also fills in
the gaps, making as minimal and Minecrafty changes as possible.

---

#### Additional Wandering Trader Trades ####

Provides *tall flowers*

Disable with ```/skyblock setDefault wanderingTraderSkyBlockTrades false```

Tall Flowers trades mimic Bedrock.

##### Additional Trades: #####

###### Tier 1

| Item      | Price | Trades until disabled |
|-----------|-------|-----------------------|
| Lilac     | 1     | 12                    |
| Rose Bush | 1     | 12                    |
| Peony     | 1     | 12                    |
| Sunflower | 1     | 12                    |

###### Tier 2

| Item          | Price | Input Item | Trades until disabled |
|---------------|-------|------------|-----------------------|
| Lava Bucket\* | 16    | Bucket     | 1                     |

\* The Lava Bucket trade needs `lavaFromWanderingTrader` enabled, which is disabled by default.

---

#### Lightning Electrifies Vines ####

Provides *Glow Lichen*

Disable with ```/skyblock setDefault lightningElectrifiesVines false```

If lightning strikes Glowstone with vines attached, the vines will turn into Glow Lichen. It can also strike a Lightning
Rod on the Glowstone.

---

#### Renewable Budding Amethysts ####

Provides *Budding Amethysts*

Disable with ```/skyblock setDefault renewableBuddingAmethysts false```

A lava block surrounded by Calcite which is then surrounded by Smooth Basalt will eventually turn into a Budding
Amethyst.

##### How to build structure: #####

![lava source](screenshots/amethyst_step_1_240.png?raw=true "Budding Amethyst Generation Step 1")
---->
![Lava surrounded by calcite](screenshots/amethyst_step_2_240.png?raw=true "Budding Amethyst Generation Step 2")
---->
![Calcite surrounded by smooth basalt](screenshots/amethyst_step_3_240.png?raw=true "Budding Amethyst Generation Step 3")

After some time (2 hours on average), the Lava in the center will turn into a Budding Amethyst.

![Lava has become budding amethyst](screenshots/amethyst_result_240.png?raw=true "Budding Amethyst Generation Result")


---

#### End Gateways Spawn Chorus ####

Provides *Chorus Fruit* and *Chorus Flowers*

Disable with ```/skyblock setDefault gatewaysSpawnChorus false```

When an End Gateway is taken to a position over the void, the Endstone island generated spawns with a Chorus Tree on it.

---

#### Dolphins Find Hearts of the Sea ####

Provides *Hearts of the Sea*

Disable with ```/skyblock setDefault renewableHeartsOfTheSea false```

When a Dolphin is fed a fish, they may dig a Heart of the Sea out of Sand or Gravel on the sea floor.

Must be in an Ocean biome -- they're Hearts of the Sea, not Hearts of the Jungle.

---

#### Ender Dragons Can Drop Heads ####

Provides *Dragon Heads*

Disable with ```/skyblock setDefault renewableDragonHeads false```

When an Ender Dragon is killed by a Charged Creeper, she will drop her head.

---

#### Shulkers Spawn On Dragon Kill ####

Provides *Shulkers*

Disable with ```/skyblock setDefault shulkerSpawning false```

When an Ender Dragon is re-killed, a Shulker spawns on top of the Bedrock pillar.

---

#### Anvils Compact Coal into Diamonds ####

Provides *Diamonds*

Disable with ```/skyblock setDefault renewableDiamonds false```

A Falling Anvil crushes a stack of Coal Blocks into a Diamond.

---

#### Goats Ram Open Nether Wart Blocks ####

Provides *Nether Wart*

Disable with ```/skyblock setDefault rammingWart false```

When a Goat rams a Nether Wart Block, it will break into Nether Wart.

---

#### Foxes Spawn With Sweet Berries ####

Provides *Sweet Berries*

Set the chance with ```/skyblock setDefault foxesSpawnWithSweetBerriesChance <chance>```.

When a Fox spawns with an item, there is a 20% chance the item is Sweet Berries. The Fox will eat them soon after
spawning, so be quick.

---

#### Saplings Die on Sand ####

Provides *Dead Bushes*

Disable with ```/skyblock setDefault saplingsDieOnSand false```

Saplings can be placed on Sand and Red Sand.

After a time, the saplings will die and turn into Dead Bushes.

---

#### Thick Potions convert Stone to Deepslate ####

Provides *Deepslate*

Disable with ```/skyblock setDefault renewableDeepslate false```

Disable only splash with ```/skyblock setDefault renewableDeepslate no_splash```

Right clicking or dispensing a Thick Potion on Stone converts it to Deepslate.

A Thick Splash Potion will convert all Stone blocks hit by the splash into Deepslate.

---

#### Netherrack Generates with Nether Portal Structures ####

Provides *Netherrack*

Disable with ```/skyblock setDefault renewableNetherrack false```

When a Nether Portal generates in the void, in generates Netherrack around it, matching the behavior of Bedrock.

---

#### Creatures with Echolocation Drop Echo Shards when Killed with Sonic Booms ####

Provides *Echo Shards*

Disable with ```/skyblock setDefault renewableEchoShards false```

Bats and Dolphins drop an Echo Shard when killed by a Warden's Sonic Boom attack.

---

#### Poisonous Potatoes Convert Spiders ####

Provides *Cave Spiders*

Disable with ```/skyblock setDefault poisonousPotatoesConvertSpiders false```

Use a Poisonous Potato on a Spider to convert it to a Cave Spider.

---

#### Vexes Can Be Allayed ####

Provides *Allays*

Disable with ```/skyblock setDefault renewableAllays false```

Play Vexes the right sequence of 5 Note Block notes to convert them to Allays.

Vexes listen to Note Blocks within a 16 block range and emit particles based on whether the correct note is played. The
instrument is ignored and the octave is ignored, meaning F#<sub>3</sub> is treated the same as F#<sub>5</sub>.

When a Vex is in a Minecart, a Comparator can be used with a Detector Rail to determine the next note in the sequence.
The Comparator outputs a value from 0 (corresponding to F#) to 11 (corresponding to F)

---

### Additional Datapack Features ###

The included datapack provides additional recipes, and advancements.

When possible, changes were added to the datapack over the mod for ease of user customization.

---

#### Lava from Hero of the Village Gift ####

Provides *Lava*

Lava Buckets can be received as a Hero of the Village gift from Armorers, Weaponsmiths, and Toolsmiths.

---

#### Calcite and Tuff Obtainable ####

Provides *Calcite* and *Tuff*

Put Diorite in a Blast Furnace for Calcite.

Put Andesite in a Blast Furnace for Tuff.

---

#### Piglin Brutes Drop Ancient Debris ####

Provides *Ancient Debris*

A Carpet setting enables Piglin Brutes to spawn in bastions. These Brutes have a chance to drop Ancient Debris.

---

#### Elytra Obtainable from Endermites ####

Provides *Elytra*

Player killing an Endermite affected by both Slow Falling and Levitation
has a chance to drop an Elytra, increased by looting.

---

#### Glow Berries Recipe ####

Provides *Glow Berries*

Sweet Berries crafted with Glow Ink Sacs give Glow Berries.

---

#### Spider Jockeys Drop Cobwebs ####

Provides *Cobwebs*

When a player kills a Spider Jockey, the first half killed drops a cobweb.

---

#### Cocoa Beans Obtainable by Fishing in Jungles ####

Provides *Cocoa Beans*

Matching Bedrock, Cocoa Beans can be obtained as a junk item when fishing in a Jungle.

---

#### Ores Obtainable in Smithing Table ####

Provides *ores*

All ores can be crafted in a smithing table using a block of the base stone material and a block of the ore's material.

For example, Nether Gold Ore can be crafted with Netherrack and a Block of Gold.

---

#### Horse Armors are Craftable ####

Provides *Iron Horse Armor*, *Golden Horse Armor*, and *Diamond Horse Armor*

Craft Horse Armors with their respective materials in a **H** shape.

---

#### Cats Gift Enchanted Golden Apples ####

Provides *Enchanted Golden Apples*

Cats will rarely bring the player an Enchanted Golden Apple as a morning gift.

---

#### Piglins Give Gilded Blackstone ####

Provides *Gilded Blackstone*

Piglins will rarely give Gilded Blackstone when bartering.

---

#### Creepers Drop Structure Specific Music Discs or Fragments in those Structures ####

Provides the music discs *Pigstep*, *otherside*, and *5*

When Creepers are killed by Skeletons in the Bastions, they can drop the Music Disc "Pigstep".

When Creepers are killed by Skeletons in the Strongholds, they can drop the Music Disc "otherside".

When Creepers are killed by Skeletons in the Ancient Cities, they can drop Disc Fragment 5.

---

#### Zoglins Drop Snout Banner Patterns ####

Provides *Snout Banner Pattern*

Title says it all.

---

#### Flowering Azalea Leaves Drop Spore Blossoms ####

Provides *Spore Blossoms*

Flowering Azalea Leaves have a chance to drop Spore Blossoms, increased by Fortune.

---

#### Endermen Can Pick Up Tall Grass and Large Ferns ####

Provides *Tall Grass and Large Ferns*

Endermen who have picked up Tall Grass and Large Ferns can be killed to retrieve those as an item.

The mod fixes Endermen's handling of double-tall blocks, allowing them to be correctly placed down without being
destroyed.

However, it is not a client side mod, so the vanilla rendering bug is not
fixed ([MC-193497](https://bugs.mojang.com/browse/MC-193497)).

---

#### Magma Cream Recipe Disabled ####

The vanilla Magma Cream recipe is too easy. Make a Magma Cube farm instead.

---

#### War Pigs Advancement Changed ####

In a SkyBlock world, the War Pigs advancement is unobtainable because it requires generated chests.

The advancement is now obtainable by killing a Piglin Brute.

----

### Numerous Added Advancements ###

SkyBlock advancements help guide progression, as well as serve to document the mod's changes to vanilla.

With all features enabled, all vanilla advancements can be completed (although War Pigs is changed).

## Carpet Features

Installation will also enable these `fabric-carpet` features.

With the `fabric-carpet` options below enabled, all blocks, items, and mobs obtainable in vanilla survival are
obtainable in SkyBlock.

- [renewableSponges](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#renewablesponges) for
  Sponges
  - run `/carpet setDefault renewableSponges false` to disable
- [piglinsSpawningInBastions](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#piglinsSpawningInBastions)
  for Ancient Debris
  - run `/carpet setDefault piglinsSpawningInBastions false` to disable

## Acknowledgements

- [@skyrising](https://github.com/skyrising/skyblock) for the initial mod idea and some source code

- [@DeadlyMC](https://github.com/DeadlyMC/Skyblock-datapack) for the initial ideas for the datapack

- [@gnembon](https://github.com/gnembon/fabric-carpet) for `fabric-carpet`

## License

This project is licensed under the terms of the MIT license.
