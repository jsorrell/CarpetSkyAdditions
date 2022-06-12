## 安装

### 单人游戏

+ 安装[Fabric](https://fabricmc.net/use)
+ 下载[fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/)
+ 下载[fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
+ 下载[SkyBlock模组](https://github.com/jsorrell/CarpetSkyAdditions/releases)
+ 将fabric-carpet，fabric-api和SkyBlock模组放到`<minecraft文件目录>/mods/`文件夹内
+ 启动Minecraft并选择`创建新的世界`
+ 将**允许作弊**选项调整为开启，以确保你能在游戏内控制模组特性的开启与关闭（建议不开启避免误操作，如果需要调整可以在游戏内选择对局域网开放，并在那里勾选允许作弊进行临时的修改）
+ 启用数据包`carpetskyadditions/skyblock`
+ 点击`更多世界选项`
+ 选择`世界类型：SkyBlock`
+ 创建世界
+ **可选**  下载[成就汉化包](https://github.com/jsorrell/CarpetSkyAdditions/releases)，并将其放入`resourcepacks`文件夹中。

### 多人游戏

该模组仅需在服务器端进行配置。

- 创建一个[fabric服务端](https://fabricmc.net/use/server/)
- 下载[fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
- 下载[fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/)
- 下载[SkyBlock模组](https://github.com/jsorrell/CarpetSkyAdditions/releases)
- 启动服务端以生成配置模板`server.properties`文件
- 关闭服务器
- 删除服务端生成的`world`文件夹（就是删除掉一开始生成的默认世界）
- 将fabric-carpet，fabric-api和SkyBlock模组放到`<服务端文件夹目录>/mods/`文件夹内
- 打开配置文件`server.properties`
- 将`level-type=minecraft\:normal`（世界生成类型：默认）修改为`level-type=carpetskyadditions\:skyblock`（世界生成类型：空岛）
- 如果有其余生成选项修改或指定世界种子也请在这一步进行完成
- 启动服务器
- 在服务端操作台或在游戏内以OP权限执行指令`datapack enable "carpetskyadditions/skyblock"`启用模组内置数据包
