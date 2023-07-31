## Datapacks

### Modifying

To customize the datapack,
download the datapack from [releases](https://github.com/jsorrell/CarpetSkyAdditions/releases)
and edit to your liking.

Disable the built-in datapack and enable your edited one instead.

#### Structures ####
Configured features can be overridden to replace the default world spawn platform and Sky Island platform.

The world spawn platform uses a configured feature defined in
`data/carpetskyadditions/worldgen/configured_feature/spawn_platform.json`.
By default, it loads the structure defined in `data/carpetskyadditions/structures/spawn_platform.nbt`
at an offset of (-4, -1) at y=63.

Sky Islands generated from the command use a configured feature defined in
`data/carpetskyadditions/worldgen/configured_feature/sky_island.json`.
They fall back to the world spawn platform if this isn't defined.

---

### Built-In "SkyBlock" Datapack Features ###
Note that this datapack needs to be explicitly enabled when starting a new world.

#### Villagers Gift Lava Buckets ####

Provides *Lava*

Disable by deleting ```data/minecraft/loot_tables/gameplay/hero_of_the_village```

Lava Buckets can be received as a Hero of the Village gift from Armorers, Weaponsmiths, and Toolsmiths.

---

#### Elytra Obtainable from Endermites ####

Provides *Elytra*

Disable by deleting ```data/minecraft/loot_tables/entities/endermite.json```

Player killing an Endermite affected by both Slow Falling and Levitation
has a chance to drop an Elytra, increased by looting.

---

#### Piglin Brutes Drop Ancient Debris ####

Provides *Ancient Debris*

Disable by deleting ```data/minecraft/loot_tables/entities/piglin_brute.json```

A Carpet setting enables Piglin Brutes to spawn in bastions. These Brutes have a chance to drop Ancient Debris.

---

#### Calcite and Tuff Obtainable ####

Provides *Calcite* and *Tuff*

Disable by deleting ```data/skyblock/recipes/[tuff_from_blasting_andesite.json + calcite_from_blasting_diorite.json]```

Put Diorite in a Blast Furnace for Calcite.

Put Andesite in a Blast Furnace for Tuff.

---

#### Glow Berries Craftable ####

Provides *Glow Berries*

Disable by deleting ```data/skyblock/recipes/glow_berries.json```

Sweet Berries crafted with Glow Ink Sacs give Glow Berries.

---

#### Spider Jockeys Drop Cobwebs ####

Provides *Cobwebs*

Disable by deleting ```data/minecraft/loot_tables/entities/[skeleton.json + spider.json]```

When a player kills a Spider Jockey, the first half killed drops a cobweb.

---

#### Cocoa Beans Obtainable by Fishing in Jungles ####

Provides *Cocoa Beans*

Disable by deleting ```data/minecraft/loot_tables/gameplay/fishing/junk.json```

Matching Bedrock, Cocoa Beans can be obtained as a junk item when fishing in a Jungle.

---

#### Ores are Craftable ####

Provides *ores*

Disable by deleting ```data/skyblock/recipes/*_ore.json```

All ores can be crafted using a block of the base stone material and four of the ore's material.

The recipe requires ingots, Nether Quartz, Coal, Diamonds, or Emeralds.

*Copper, Redstone, and Lapis Lazuli require blocks.*

For example, Nether Gold Ore can be crafted with Netherrack in the center and Gold Ingots on the four sides.

![4 Gold Ingots around Netherrack gives Nether Gold Ore](../screenshots/ore_recipe.png?raw=true "Ore Recipe")

---

#### Horse Armor is Craftable ####

Provides *Iron Horse Armor*, *Golden Horse Armor*, and *Diamond Horse Armor*

Disable by deleting ```data/skyblock/recipes/*_horse_armor.json```

Craft Horse Armors with their respective materials in a **H** shape.

---

#### Cats Gift Enchanted Golden Apples ####

Provides *Enchanted Golden Apples*

Disable by deleting ```data/minecraft/loot_tables/gameplay/cat_morning_gift.json```

Cats will rarely bring the player an Enchanted Golden Apple as a morning gift.

---

#### Piglins Give Gilded Blackstone ####

Provides *Gilded Blackstone*

Disable by deleting ```data/minecraft/loot_tables/gameplay/piglin_bartering.json```

Piglins will rarely give Gilded Blackstone when bartering.

---

#### Creepers Drop Structure Specific Music Discs or Fragments in those Structures ####

Provides the music discs *Pigstep*, *otherside*, and *5*

Disable by deleting ```data/minecraft/loot_tables/entities/creeper.json```

When Creepers are killed by Skeletons in the Bastions, they can drop the Music Disc "Pigstep".

When Creepers are killed by Skeletons in the Strongholds, they can drop the Music Disc "otherside".

When Creepers are killed by Skeletons in the Ancient Cities, they can drop Disc Fragment 5.

---

#### Zoglins Drop Snout Banner Patterns ####

Provides *Snout Banner Pattern*

Disable by deleting ```data/minecraft/loot_tables/entities/zoglin.json```

Zoglins will always drop a Snout Banner Pattern when killed by a player.

---

#### Flowering Azalea Leaves Drop Spore Blossoms ####

Provides *Spore Blossoms*

Disable by deleting ```data/minecraft/loot_tables/blocks/flowering_azaliea_leaves.json```

Flowering Azalea Leaves have a chance to drop Spore Blossoms, increased by Fortune.

---

#### Endermen Can Pick Up Tall Grass and Large Ferns ####

Provides *Tall Grass and Large Ferns*

Disable by deleting ```data/minecraft/tags/blocks/enderman_holdable.json```

Endermen who have picked up Tall Grass and Large Ferns can be killed to retrieve those as an item,
mimicking pre-1.19.3 behavior.

The mod fixes Endermen's handling of double-tall blocks, allowing them to be correctly placed down without being
destroyed.

---

#### Netherite Upgrade Smithing Template is Craftable ####

Provides *Netherite Upgrade Smithing Template*

Disable by deleting ```data/skyblock/recipes/netherite_upgrade_smithing_template_from_netherite.json```

A Netherite Upgrade Smithing Template is crafted from the duplication recipe
with the Smithing Template replaced with a Netherite Ingot.

![Netherite Upgrade Smithing Template Crafting Recipe](../screenshots/netherite_upgrade_smithing_template_recipe.png?raw=true "Template Recipe")

---

### Advancements ###

The datapack adds numerous advancements to guide progression.

It also adds multiple challenge advancements that provide Armor Trim Smithing Templates as rewards upon completion.

The challenge advancements are documented below.

#### Seeing Patterns Everywhere ####

Completing rewards *Sentry Armor Trim Smithing Template*

Granted upon obtaining all Banner Patterns.

---

#### Let There Be Light ####

Completing rewards *Eye Armor Trim Smithing Template*

Granted upon obtaining all Light Sources in the following list:
- Torch and Soul Torch
- Lantern and Soul Lantern
- Campfire and Soul Campfire
- Candle and all Colored Candles
- Glowstone
- Jack o'Lantern
- Shroomlight
- Sea Lantern
- All Froglights
- Lava Bucket
- Redstone Lamp
- Glow Berries
- Glow Lichen
- Sea Pickle
- End Rod
- Furnace, Blast Furnace, and Smoker
- Brewing Stand
- Crying Obsidian
- Respawn Anchor
- Redstone Ore and Deepslate Redstone Ore
- Enchanting Table
- Ender Chest
- Redstone Torch
- Sculk Sensor and Calibrated Sculk Sensor
- Sculk Catalyst
- Amethyst Cluster and all sized Buds
- Magma Block
- Brown Mushroom
- Beacon
- Conduit
- Dragon Egg

---

#### End City Builder ####

Completing rewards *Spire Armor Trim Smithing Template*

Granted upon crafting or stonecutting all Purpur Blocks, Pillars, Stairs, and Slabs while levitated.

---

#### Treasure Map to Nowhere ####

Completing rewards *Vex Armor Trim Smithing Template*

Granted upon entering a Woodland Mansion bounding box.

The spirit of this challenge is to follow a treasure map from a villager,
but looking up the coordinates would work too.

---

#### Wither Art Thou ####

Completing rewards *Rib Armor Trim Smithing Template*

Granted upon killing a Wither within the (small) bounding box of a Nether Fortress.

---

#### Air Tunes ####

Completing rewards *Silence Armor Trim Smithing Template*

Granted upon playing all music discs in a Jukebox.

Music discs must be clicked onto a Jukebox, not hoppered in.

---

#### Way of the Ancients ####

Completing rewards *Dune Armor Trim Smithing Template*

An exact replica Desert Pyramid must be built as described [here](https://minecraft.fandom.com/wiki/Desert_pyramid/Structure).
The structure can face any direction. Only layers from the Blue Terracotta layer to the top matter.

The advancement is granted when a Husk is sacrificed on the Blue Terracotta in the center of the pyramid.

---

#### Sky Pirate ###

Completing rewards *Coast Armor Trim Smithing Template*

Granted upon traveling 30km in a boat.

---

#### War Pigs ####

Completing rewards *Snout Armor Trim Smithing Template*

Granted upon killing a Piglin Brute while riding a Pig and wearing a Piglin Head.

The vanilla War Pigs advancement is impossible; this serves as its replacement.

---

#### Resistance Isn't Futile ####

Completing rewards *Ward Armor Trim Smithing Template*

Granted upon getting hit by a Warden while wearing full Protection 4 Netherite Armor
and affected by Resistance 4.

---

#### Spy in the Sky ####

Completing rewards *Wild Armor Trim Smithing Template*

Granted after looking through a Spyglass at all animals in the following list:
- Axolotl
- Bat
- Bee
- Camel
- Cat
- Spider and Cave Spider
- Chicken
- Cod, Salmon, Pufferfish, and Tropical Fish
- Cow
- Dolphin
- Horse, Donkey, and Mule
- Endermite
- Fox
- Frog and Tadpole
- Squid and Glow Squid
- Goat
- Hoglin
- Llama
- Mooshroom
- Ocelot
- Panda
- Parrot
- Pig
- Polar Bear
- Rabbit
- Sheep
- Silverfish
- Sniffer
- Strider
- Turtle
- Wolf
