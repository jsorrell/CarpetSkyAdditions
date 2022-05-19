## Mod Features

#### Dead Coral and Coral Fans Erode into Sand ####

Provides additional *Sand* and *Red Sand*

Disable with ```/skyblock removeDefault coralErosion```

Dead Coral and Dead Coral Fans with water flowing out of them will spawn Sand every few seconds.
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

Disable with ```/skyblock removeDefault shulkerSpawning```

When an Ender Dragon is re-killed, a Shulker spawns on top of the Bedrock pillar.

---

#### Goats Break Apart Nether Wart Blocks By Ramming Them ####

Provides *Nether Wart*

Disable with ```/skyblock removeDefault rammingWart```

When a Goat rams a Nether Wart Block, it will break into Nether Wart.

---

#### Thick Potions Convert Stone into Deepslate ####

Provides *Deepslate*

Disable with ```/skyblock removeDefault renewableDeepslate```

Disable only splash with ```/skyblock setDefault renewableDeepslate no_splash```

Right clicking or dispensing a Thick Potion on Stone converts it to Deepslate.

A Thick Splash Potion will convert all Stone blocks hit by the splash into Deepslate.

---

#### Netherrack Generates with Nether Portal Structures ####

Provides *Netherrack*

Disable with ```/skyblock removeDefault renewableNetherrack```

When a Nether Portal generates in the void, in generates Netherrack around it, matching the behavior of Bedrock.

---

#### Wandering Traders Sell Tall Flowers ####

Provides *tall flowers*

Disable with ```/skyblock removeDefault tallFlowersFromWanderingTrader```

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

Disable with ```/skyblock removeDefault renewableAllays```

Play Vexes the right sequence of 5 Note Block notes to convert them to Allays.

Vexes listen to Note Blocks within a 16 block range and emit particles based on whether the correct note is played. The
instrument is ignored and the octave is ignored, meaning F#<sub>3</sub> is treated the same as F#<sub>5</sub>.

When a Vex is in a Minecart, a Comparator can be used with a Detector Rail to determine the next note in the sequence.
The Comparator outputs a value from 0 (corresponding to F#) to 11 (corresponding to F)

---

#### Foxes Spawn With Sweet Berries ####

Provides *Sweet Berries*

Disable with ```/skyblock removeDefault foxesSpawnWithSweetBerriesChance```

Set the chance with ```/skyblock setDefault foxesSpawnWithSweetBerriesChance <chance>```.

When a Fox spawns with an item, there is a 20% chance the item is Sweet Berries. The Fox will eat them soon after
spawning, so be quick.

---

#### Anvils Compact Coal into Diamonds ####

Provides *Diamonds*

Disable with ```/skyblock removeDefault renewableDiamonds```

A Falling Anvil crushes a stack of Coal Blocks into a Diamond.

---

#### Lightning Electrifies Vines ####

Provides *Glow Lichen*

Disable with ```/skyblock removeDefault lightningElectrifiesVines```

If lightning strikes Glowstone with vines attached, the vines will turn into Glow Lichen. It can also strike a Lightning
Rod on the Glowstone.

---

#### Chorus Trees Generate on End Islands ####

Provides *Chorus Fruit* and *Chorus Flowers*

Disable with ```/skyblock removeDefault gatewaysSpawnChorus```

When an End Gateway is taken to a position over the void, the Endstone island generated spawns with a Chorus Tree on it.

---

#### Dolphins Find Hearts of the Sea ####

Provides *Hearts of the Sea*

Disable with ```/skyblock removeDefault renewableHeartsOfTheSea```

When a Dolphin is fed a fish, they may dig a Heart of the Sea out of Sand or Gravel on the sea floor.

Must be in an Ocean biome -- they're Hearts of the Sea, not Hearts of the Jungle.

---

#### Budding Amethysts Can Be Generated ####

Provides *Budding Amethysts*

Disable with ```/skyblock removeDefault renewableBuddingAmethysts```

A lava block surrounded by Calcite which is then surrounded by Smooth Basalt will eventually turn into a Budding
Amethyst.

##### How to build structure: #####

![lava source](../screenshots/amethyst_step_1_240.png?raw=true "Budding Amethyst Generation Step 1")
---->
![Lava surrounded by calcite](../screenshots/amethyst_step_2_240.png?raw=true "Budding Amethyst Generation Step 2")
---->
![Calcite surrounded by smooth basalt](../screenshots/amethyst_step_3_240.png?raw=true "Budding Amethyst Generation Step 3")

After some time (2 hours on average), the Lava in the center will turn into a Budding Amethyst.

![Lava has become budding amethyst](../screenshots/amethyst_result_240.png?raw=true "Budding Amethyst Generation Result")

---

#### Saplings Die on Sand ####

Provides *Dead Bushes*

Disable with ```/skyblock removeDefault saplingsDieOnSand```

Saplings can be placed on Sand and Red Sand.

After a time, the saplings will die and turn into Dead Bushes.

---

#### Ender Dragons Can Drop their Head ####

Provides *Dragon Heads*

Disable with ```/skyblock removeDefault renewableDragonHeads```

When an Ender Dragon is killed by a Charged Creeper, she will drop her head.

---

#### Huge Mushrooms Spread Mycelium ####

Provides *Mycelium*

Disable with ```/skyblock removeDefault hugeMushroomsSpreadMycelium```

When a Huge Mushroom grows, it spreads Mycelium nearby, similar to how Mega Spruces Trees spread Podzol.

---

#### Creatures with Echolocation Drop Echo Shards when Killed with Sonic Booms ####

Provides *Echo Shards*

Disable with ```/skyblock removeDefault renewableEchoShards```

Bats and Dolphins drop an Echo Shard when killed by a Warden's Sonic Boom attack.

---

#### Enchanting Tables can Enchant Items with Swift Sneak in Ancient Cities ####

Provides *Swift Sneak*

Disable with ```/skyblock removeDefault swiftSneakFromEnchantingTable```

An Enchantment Table placed within an Ancient City can enchant items with Swift Sneak.

---

#### Poisonous Potatoes Convert Spiders ####

Provides *Cave Spiders*

Disable with ```/skyblock removeDefault poisonousPotatoesConvertSpiders```

Use a Poisonous Potato on a Spider to convert it to a Cave Spider.

---

#### Wandering Traders Sell Lava  ####

Provides *Lava*

Enable with ```/skyblock setDefault lavaFromWanderingTrader true```

***Disabled by Default - Get Lava from a Hero of the Village Gift Instead***

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
