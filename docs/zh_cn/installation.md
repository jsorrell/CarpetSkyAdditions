## 安装

### 单人游戏

- 安装[Fabric](https://fabricmc.net/use)
- 下载[fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files),
  [fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/),
  [cloth-config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files/all?filter-game-version=2020709689%3A7499),
  和[地毯端空岛拓展](https://github.com/jsorrell/CarpetSkyAdditions/releases)
- 将下载的模组放到`<minecraft文件目录>/mods/`文件夹内
- 启动Minecraft并选择`创建新的世界`
- 将**允许作弊**选项调整为开启，以确保你能在游戏内控制模组特性的开启与关闭（建议不开启避免误操作，如果需要调整可以在游戏内选择对局域网开放，并在那里勾选允许作弊进行临时的修改）
- 启用数据包`carpetskyadditions/skyblock`
- 可以选择启用数据包`carpetskyadditions/skyblock_acacia`，以获得金合欢树开局。
- 点击`更多世界选项`
- 选择`世界类型：SkyBlock`
- 创建世界
- **可选**  下载[成就汉化包](https://github.com/jsorrell/CarpetSkyAdditions/releases)，并将其放入`resourcepacks`文件夹中。

### 多人游戏

该模组仅需在服务器端进行配置。

- 创建一个[fabric服务端](https://fabricmc.net/use/server/)
- 下载[fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files),
  [fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/),
  [cloth-config](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files),
  和[地毯端空岛拓展](https://github.com/jsorrell/CarpetSkyAdditions/releases)
- 将下载的模组放到`<server-directory>/mods/`文件夹内
- 启动服务端以生成配置模板`server.properties`文件和`eula.txt`
- 同意EULA（最终用户许可协议）
- 关闭服务器
- 打开配置文件`server.properties`
- 将`level-type=minecraft\:normal`（世界生成类型：默认）修改为`level-type=carpetskyadditions\:skyblock`（世界生成类型：空岛）
- 将`carpetskyadditions\:skyblock`从`initial-disabled-packs`移动到`initial-enabled-packs`
- 可以选择将`carpetskyadditions\:skyblock_acacia`从`initial-disabled-packs`移动到`initial-enabled-packs`以获得金合欢树开局
- 启动服务器

### 配置

该MOD有一个配置文件：`carpetskyadditions.toml`

#### defaultToSkyBlockWorld

*默认值为false*

当设置为`true`时，创建新世界时会默认选择`SkyBlock`世界类型。

---

#### enableDatapackByDefault

*默认值为false*

当设置为`true`时，创建新世界时会默认启用`skyblock`数据包。

---

#### initialTreeType

*默认值为OAK*

当设置为`ACACIA`时，创建新世界时`skyblock_acacia`数据包也会默认启用。

仅在`enableDatapackByDefault`设置为`true`时生效。

---

#### autoEnableDefaultSettings

*默认值为true*

当设置为`true`时，首次启动使用`SkyBlock`生成的世界时，会启用默认的SkyBlock设置。

