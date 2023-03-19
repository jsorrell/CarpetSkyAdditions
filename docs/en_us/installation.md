## Installation

### Singleplayer

- Install [Fabric](https://fabricmc.net/use/installer/)
- Download [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files),
  [fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files),
  [cloth-config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files/all?filter-game-version=2020709689%3A7499),
  and [Carpet Sky Additions](https://github.com/jsorrell/CarpetSkyAdditions/releases)
- Place downloaded mods into `<minecraft-directory>/mods/`
- Start Minecraft and `Create New World`
- Set Allow Cheats to `ON` so you will be able to enable/disable mod features
- Enable the Datapack `carpetskyadditions/skyblock`
- Optionally enable the Datapack `carpetskyadditions/skyblock_acacia` for an Acacia Tree start
- Click on `More World Options...`
- Choose `World Type: SkyBlock`
- Create the World

### Multiplayer

The mod is only required server-side.

- Create a [Fabric Server](https://fabricmc.net/use/server/)
- Download [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files),
  [fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files),
  [cloth-config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files),
  and [Carpet Sky Additions](https://github.com/jsorrell/CarpetSkyAdditions/releases)
- Place downloaded mods into `<server-directory>/mods/`
- Start the server to generate a template `server.properties` and `eula.txt` file
- Agree to the EULA
- Open `server.properties`
- Change `level-type=minecraft\:normal` to `level-type=carpetskyadditions\:skyblock`
- Move `carpetskyadditions\:skyblock` from `initial-disabled-packs` to `initial-enabled-packs`
- Optionally move `carpetskyadditions\:skyblock_acacia` from `initial-disabled-packs` to `initial-enabled-packs` for an
  Acacia Tree start
- Start the server

### Configuration

The mod has a config file: `carpetskyadditions.toml`

#### defaultToSkyBlockWorld

*Defaults to false*

When `true`, the `SkyBlock` world type is selected by default when creating a new world.

---

#### enableDatapackByDefault

*Defaults to false*

When `true`, the `skyblock` datapack is enabled by default when creating a new world.

---

#### initialTreeType

*Defaults to OAK*

When set to `ACACIA`, the `skyblock_acacia` datapack is also enabled by default when creating a new world.

Only takes effect when `enableDatapackByDefault` is `true`

---

#### autoEnableDefaultSettings

*Defaults to true*

When `true`, the default SkyBlock settings are enabled when first starting a world with `SkyBlock` generation.
