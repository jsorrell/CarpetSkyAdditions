# SkyBlock

SkyBlock is a module for [fabric-carpet](https://github.com/gnembon/fabric-carpet)

It is a fork of [skyrising/skyblock](https://github.com/skyrising/skyblock)

## Features

### SkyBlock Generation

- Set world type to SkyBlock to generate an empty world (in all dimensions) with a single spawn platform.
![small spawn platform with grass, mycelium, nylium, and a tree](screenshots/spawn_platform.png?raw=true "Spawn Platform")

- Keeps biomes, structure bounding boxes, end portal frames, and silverfish spawners.
![end portal frame remains](screenshots/end_portal.png?raw=true "End Portal Frame")

### Additional Wandering Trader Trades
Enable with ```/skyblock wanderingTraderSkyBlockTrades true```
#### Tier 1
| Item          | Price | Trades until disabled |
| ------------- | ----- | --------------------- |
| Chorus Flower | 5     | 6                     |
| Nether Wart   | 1     | 12                    |
| Sweet Berries | 1     | 16                    |
| Lilac         | 5     | 10                    |
| Rose Bush     | 5     | 10                    |
| Peony         | 5     | 10                    |

#### Tier 2
| Item               | Price | Trades until disabled |
| ------------------ | ----- | --------------------- |
| Lava Bucket        | 16    | 11                    |
| Jukebox            | 64    | 6                     |
| Enchanting Table   | 64    | 6                     |
| Heart of the Sea   | 64    | 6                     |
| Wet Sponge         | 16    | 6                     |
| Pigstep Music Disc | 16    | 6                     |
| Cobweb             | 3     | 9                     |

#### Changes from [skyrising/skyblock](https://github.com/skyrising/skyblock)
- Removed soul sand: Use piglin bartering
- Removed cocoa beans: Go fishing in a jungle
- Added enchating table
- Added cobweb
- Added wet sponge
- Added pigstep music disc which is new in 1.16


### Useful Composters
Enable with ```/skyblock usefulComposters true``` or ```/skyblock usefulComposters redstone```
- Provides a nicer way to get sand/red sand than the 64/24 you can get at a time from a wandering trader.
- Makes dirt generation nicer too.
- Compost result depends on the biome.
Get sand in deserts, beaches, warm oceans etc, red sand from badlands, and dirt elsewhere.
  
- When set to ```redstone```, the composter outputs bonemeal without a redstone signal.

## Installation
- Install [Fabric](https://fabricmc.net/use)
- Download [fabric-carpet](https://github.com/gnembon/fabric-carpet/releases)
- Optionally download [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
- Download [SkyBlock](https://github.com/jsorrell/skyblock/releases)
- Place fabric-carpet, fabric-api, and SkyBlock into `<minecraft-directory>/mods/`

## Usage (World Generation)
### Singleplayer
- `Create New World`
- Optionally download [skyblock-datapack](https://github.com/jsorrell/skyblock-datapack) and select this as a datapack
- `More World Options...`
- Choose `World Type: SkyBlock` (or `World Type: generator.skyblock.skyblock` if you didn't download `fabric-api`)

### Multiplayer
- Open `server.properties`
- Change `level-type=default` to `level-type=skyblock`
- Make sure to delete or move the world folder in order to create a new world