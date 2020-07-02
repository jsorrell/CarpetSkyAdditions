# SkyBlock

SkyBlock is a module for [fabric-carpet](https://github.com/gnembon/fabric-carpet)

It is a fork of [skyrising/skyblock](https://github.com/skyrising/skyblock)

## Features
- Generator for empty worlds, keeping biomes, structure bounding boxes and end portals
    - Generates a skyblock style spawn platform
- Additional wandering trader trades
- Dirt and sand generation from composters

## Installation
- Install [Fabric](https://fabricmc.net/use)
- Download [fabric-carpet](https://github.com/gnembon/fabric-carpet/releases)
- Download [SkyBlock](https://github.com/jsorrell/skyblock/releases)
- Place fabric-carpet and SkyBlock into `<minecraft-directory>/mods/`

## Usage (World Generation)
### Singleplayer
- `Create New World`
- `More World Options...`
- Choose `World Type: Skyblock`

### Multiplayer
- Open `server.properties`
- Change `level-type=default` to `level-type=skyblock`
- Make sure to delete or move the world folder in order to create a new world