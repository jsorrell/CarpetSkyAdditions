## Datapack Features

### Modifying

To customize the datapack,
download the
datapack [here](https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2Fjsorrell%2FCarpetSkyAdditions%2Ftree%2FHEAD%2Fdatapack)
and edit to your liking.

Disable the built-in datapack and enable your edited one instead.

---

#### Villagers Gift Lava Buckets ####

Provides *Lava*

Disable by deleting  ```data/minecraft/loot_tables/gameplay/hero_of_the_village```

Lava Buckets can be received as a Hero of the Village gift from Armorers, Weaponsmiths, and Toolsmiths.

---

#### Elytra Obtainable from Endermites ####

Provides *Elytra*

Disable by deleting  ```data/minecraft/loot_tables/entities/endermite.json```

Player killing an Endermite affected by both Slow Falling and Levitation
has a chance to drop an Elytra, increased by looting.

---

#### Piglin Brutes Drop Ancient Debris ####

Provides *Ancient Debris*

Disable by deleting  ```data/minecraft/loot_tables/entities/piglin_brute.json```

A Carpet setting enables Piglin Brutes to spawn in bastions. These Brutes have a chance to drop Ancient Debris.

---

#### Calcite and Tuff Obtainable ####

Provides *Calcite* and *Tuff*

Disable by deleting  ```data/skyblock/recipes/[tuff_from_blasting_andesite.json + calcite_from_blasting_diorite.json]```

Put Diorite in a Blast Furnace for Calcite.

Put Andesite in a Blast Furnace for Tuff.

---

#### Glow Berries Craftable ####

Provides *Glow Berries*

Disable by deleting  ```data/skyblock/recipes/glow_berries.json```

Sweet Berries crafted with Glow Ink Sacs give Glow Berries.

---

#### Spider Jockeys Drop Cobwebs ####

Provides *Cobwebs*

Disable by deleting  ```data/minecraft/loot_tables/entities/[skeleton.json + spider.json]```

When a player kills a Spider Jockey, the first half killed drops a cobweb.

---

#### Cocoa Beans Obtainable by Fishing in Jungles ####

Provides *Cocoa Beans*

Disable by deleting  ```data/minecraft/loot_tables/gameplay/fishing/junk.json```

Matching Bedrock, Cocoa Beans can be obtained as a junk item when fishing in a Jungle.

---

#### Ores are Craftable ####

Provides *ores*

Disable by deleting  ```data/skyblock/recipes/*_ore.json```

All ores can be crafted using a block of the base stone material and four of the ore's material.

The recipe requires ingots, Nether Quartz, Coal, Diamonds, or Emeralds.

*Copper, Redstone, and Lapis Lazuli require blocks.*

For example, Nether Gold Ore can be crafted with Netherrack in the center and Gold Ingots on the four sides.

![4 Gold Ingots around Netherrack gives Nether Gold Ore](../screenshots/ore_recipe.png?raw=true "Ore Recipe")

---

#### Horse Armor is Craftable ####

Provides *Iron Horse Armor*, *Golden Horse Armor*, and *Diamond Horse Armor*

Disable by deleting  ```data/skyblock/recipes/*_horse_armor.json```

Craft Horse Armors with their respective materials in a **H** shape.

---

#### Cats Gift Enchanted Golden Apples ####

Provides *Enchanted Golden Apples*

Disable by deleting  ```data/minecraft/loot_tables/gameplay/cat_morning_gift.json```

Cats will rarely bring the player an Enchanted Golden Apple as a morning gift.

---

#### Piglins Give Gilded Blackstone ####

Provides *Gilded Blackstone*

Disable by deleting  ```data/minecraft/loot_tables/gameplay/piglin_bartering.json```

Piglins will rarely give Gilded Blackstone when bartering.

---

#### Creepers Drop Structure Specific Music Discs or Fragments in those Structures ####

Provides the music discs *Pigstep*, *otherside*, and *5*

Disable by deleting  ```data/minecraft/loot_tables/entities/creeper.json```

When Creepers are killed by Skeletons in the Bastions, they can drop the Music Disc "Pigstep".

When Creepers are killed by Skeletons in the Strongholds, they can drop the Music Disc "otherside".

When Creepers are killed by Skeletons in the Ancient Cities, they can drop Disc Fragment 5.

---

#### Zoglins Drop Snout Banner Patterns ####

Provides *Snout Banner Pattern*

Disable by deleting  ```data/minecraft/loot_tables/entities/zoglin.json```

Title says it all.

---

#### Flowering Azalea Leaves Drop Spore Blossoms ####

Provides *Spore Blossoms*

Disable by deleting  ```data/minecraft/loot_tables/blocks/flowering_azaliea_leaves.json```

Flowering Azalea Leaves have a chance to drop Spore Blossoms, increased by Fortune.

---

#### Endermen Can Pick Up Tall Grass and Large Ferns ####

Provides *Tall Grass and Large Ferns*

Disable by deleting  ```data/minecraft/tags/blocks/enderman_holdable.json```

Endermen who have picked up Tall Grass and Large Ferns can be killed to retrieve those as an item,
mimicking pre-1.19.3 behavior.

The mod fixes Endermen's handling of double-tall blocks, allowing them to be correctly placed down without being
destroyed.

However, this is not a client side mod, so the vanilla rendering bug is not
fixed ([MC-193497](https://bugs.mojang.com/browse/MC-193497)).

---

#### War Pigs Advancement Changed ####

In a SkyBlock world, the War Pigs advancement is unobtainable because it requires generated chests.

The advancement is now obtainable by killing a Piglin Brute.
