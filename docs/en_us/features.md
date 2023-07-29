## Mod Features

#### Dead Coral and Coral Fans Erode into Sand ####

Provides additional *Sand* and *Red Sand*

Disable with ```/carpetskyadditions removeDefault coralErosion```

Dead Coral and Dead Coral Fans with water flowing out of them will spawn a Sand item every 16-32 seconds.
Fire versions spawn Red Sand instead.

The Coral has a 3% chance to break after spawning Sand.

Infinitely automatic farms are possible, but not trivial.

This method was added because:

- Getting Sand purely from the Wandering Trader is not sufficient.
- Gravity block duping and Trader multi-use bugs are unintended and could be fixed at any time.
- The old method where Husks dropped Sand is boring as it's just another standard mob farm.

---

#### Shulkers Spawn On Dragon Kill ####

Provides *Shulkers*

Disable with ```/carpetskyadditions removeDefault shulkerSpawning```

When an Ender Dragon is re-killed, a Shulker spawns on top of the Bedrock pillar.

---

#### Goats Break Apart Nether Wart Blocks By Ramming Them ####

Provides *Nether Wart*

Disable with ```/carpetskyadditions removeDefault rammingWart```

When a Goat rams a Nether Wart Block, it will break into Nether Wart.

---

#### Thick Potions Convert Stone into Deepslate ####

Provides *Deepslate*

Disable with ```/carpetskyadditions removeDefault renewableDeepslate```

Disable only splash/lingering conversion with ```/carpetskyadditions setDefault renewableDeepslate no_splash```

Right-clicking or dispensing a Thick Potion on Stone converts it to Deepslate.

A Thick Splash Potion will convert Stone blocks hit by the splash into Deepslate.

The conversion chance equals twice the percentage of the potion duration an entity would get.

A Thick Lingering Potion will continuously convert all Stone blocks in its cloud into Deepslate.

---

#### Netherrack and Nylium Generates with Nether Portal Structures ####

Provides *Netherrack and Nylium*

Disable with ```/carpetskyadditions removeDefault renewableNetherrack```

When a Nether Portal generates in the void, it generates a few blocks of Netherrack or Nylium around it.

Which block is generated depends on the Biome -- Crimson Nylium in Crimson Forests, Warped Nylium in Warped Forests,
Netherrack elsewhere.

---

#### Wandering Traders Sell Tall Flowers ####

Provides *tall flowers*

Disable with ```/carpetskyadditions removeDefault tallFlowersFromWanderingTrader```

Tall Flowers trades mimic Bedrock.

##### Additional Tier 1 Trades: #####

| Item      | Price | Trades until disabled |
|-----------|-------|-----------------------|
| Lilac     | 1     | 12                    |
| Rose Bush | 1     | 12                    |
| Peony     | 1     | 12                    |
| Sunflower | 1     | 12                    |

---

#### Vexes Can Be Allayed ####

Provides *Allays*

Disable with ```/carpetskyadditions removeDefault allayableVexes```

Play Vexes the right sequence of 5 Note Block notes to convert them to Allays.

Vexes listen to Note Blocks within a 16 block range and emit particles based on whether the correct note is played. The
instrument is ignored and the octave is ignored, meaning F#<sub>3</sub> is treated the same as F#<sub>5</sub>.

When a Vex is in a Minecart, a Comparator can be used with a Detector Rail to determine the next note in the sequence.
The Comparator outputs a value from 0 (corresponding to F#) to 11 (corresponding to F)

---

#### Foxes Spawn With Sweet Berries ####

Provides *Sweet Berries*

Disable with ```/carpetskyadditions removeDefault foxesSpawnWithSweetBerriesChance```

Set the chance with ```/carpetskyadditions setDefault foxesSpawnWithSweetBerriesChance <chance>```.

When a Fox spawns with an item, there is a 20% chance the item is Sweet Berries. The Fox will eat them soon after
spawning, so be quick.

---

#### Anvils Compact Coal into Diamonds ####

Provides *Diamonds*

Disable with ```/carpetskyadditions removeDefault renewableDiamonds```

A Falling Anvil compresses a stack of Coal Blocks into a Diamond.

---

#### Lightning Electrifies Vines ####

Provides *Glow Lichen*

Disable with ```/carpetskyadditions removeDefault lightningElectrifiesVines```

If lightning strikes Glowstone with vines attached, the vines will turn into Glow Lichen. It can also strike a Lightning
Rod on the Glowstone.

---

#### Chorus Trees Generate on End Islands ####

Provides *Chorus Fruit* and *Chorus Flowers*

Disable with ```/carpetskyadditions removeDefault gatewaysSpawnChorus```

When an End Gateway is taken to a position over the void, the Endstone island generated spawns with a Chorus Tree on it.

---

#### Dolphins Find Hearts of the Sea ####

Provides *Hearts of the Sea*

Disable with ```/carpetskyadditions removeDefault renewableHeartsOfTheSea```

When a Dolphin is fed a fish, they may dig a Heart of the Sea out of Sand or Gravel on the sea floor.

Make sure to give the Dolphin enough space to search.

Must be in an Ocean type biome -- they're Hearts of the Sea, not Hearts of the Jungle.

---

#### Budding Amethysts Can Be Generated ####

Provides *Budding Amethysts*

Disable with ```/carpetskyadditions removeDefault renewableBuddingAmethysts```

A lava block surrounded by Calcite which is then surrounded by Smooth Basalt will eventually turn into a Budding
Amethyst.

##### How to build structure: #####

![lava source](../screenshots/amethyst_step_1_240.png?raw=true "Budding Amethyst Generation Step 1")
---->
![Lava surrounded by calcite](../screenshots/amethyst_step_2_240.png?raw=true "Budding Amethyst Generation Step 2")
---->
![Calcite surrounded by smooth basalt](../screenshots/amethyst_step_3_240.png?raw=true "Budding Amethyst Generation Step 3")

After some time (1/100 chance on a random tick — ~2 hours on average), the Lava in the center will turn into a Budding
Amethyst.

![Lava has become budding amethyst](../screenshots/amethyst_result_240.png?raw=true "Budding Amethyst Generation Result")

---

#### Saplings Die on Sand ####

Provides *Dead Bushes*

Disable with ```/carpetskyadditions removeDefault saplingsDieOnSand```

Saplings can be placed on Sand and Red Sand.

After a time, the saplings will die and turn into Dead Bushes.

---

#### Ender Dragons Can Drop their Head ####

Provides *Dragon Heads*

Disable with ```/carpetskyadditions removeDefault renewableDragonHeads```

When an Ender Dragon is killed by a Charged Creeper, she will drop her head.

---

#### Huge Mushrooms Spread Mycelium ####

Provides *Mycelium*

Disable with ```/carpetskyadditions removeDefault hugeMushroomsSpreadMycelium```

When a Huge Mushroom grows, it spreads Mycelium nearby, similar to how Mega Spruces Trees spread Podzol.

---

#### Creatures with Echolocation Drop Echo Shards when Killed with Sonic Booms ####

Provides *Echo Shards*

Disable with ```/carpetskyadditions removeDefault renewableEchoShards```

Bats and Dolphins drop an Echo Shard when killed by a Warden's Sonic Boom attack.

---

#### Enchanting Tables Near Wardens can Enchant Items with Swift Sneak ####

Provides *Swift Sneak*

Disable with ```/carpetskyadditions removeDefault renewableSwiftSneak```

An Enchanting Table placed within 8 blocks of a Warden can enchant items with Swift Sneak.

---

#### Poisonous Potatoes Convert Spiders ####

Provides *Cave Spiders*

Disable with ```/carpetskyadditions removeDefault poisonousPotatoesConvertSpiders```

Use a Poisonous Potato on a Spider to convert it to a Cave Spider.

---

#### Wandering Traders Can Spawn Riding Camels ####

Provides *Camels*

Disable with ```/carpetskyadditions removeDefault traderCamels```

When a Wandering Trader spawns in a Desert or Badlands biome (tag `carpetskyadditions:wandering_trader_spawns_on_camel`),
it will not have Trader Llamas, but will ride a Camel.

The Camel will despawn when the Wandering Trader does if the Trader is still riding it.

While the Wandering Trader is riding, the Camel can't be ridden, fed, or leashed.

---

#### Wandering Traders Sell Lava  ####

Provides *Lava*

Enable with ```/carpetskyadditions setDefault lavaFromWanderingTrader true```

*** Not automatically enabled in SkyBlock — Get Lava from a Hero of the Village Gift Instead***

##### Additional Tier 2 Trade: #####

| Item        | Price | Input Item | Trades until disabled |
|-------------|-------|------------|-----------------------|
| Lava Bucket | 16    | Bucket     | 1                     |

### Carpet Features

Default installation will also enable these `fabric-carpet` features.

- [renewableSponges](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#renewablesponges) for
  Sponges
  - run `/carpet removeDefault renewableSponges` to disable
- [piglinsSpawningInBastions](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#piglinsSpawningInBastions)
  for Piglin Brutes and Ancient Debris
  - run `/carpet removeDefault piglinsSpawningInBastions` to disable
