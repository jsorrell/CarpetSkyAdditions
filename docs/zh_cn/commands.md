## 命令

### SkyIsland ###

----
权限配置使用 ```/carpetskyadditions commandSkyIsland```

生成生成岛屿，简化了在同一服务器上具有不同起始岛屿的多个玩家。
Generates spawn islands, simplifying having multiple players on the same server with different starting islands.

**用法: `/skyisland new`**

创建一个新的起始平台。
Creates a new starting platform.

该平台将至少位于其他生成的岛屿几千个方块之外，
但不会远离外部要塞的环形边界。
The platform will be at least a few thousand blocks out from other generated islands,
yet not too far outside the outer stronghold ring.

它永远不会生成在已经生成的区块中。
It will never generate in already generated chunks.


**用法: `/skyisland join [num]`**

将调用的玩家传送到岛屿编号为 `num` 的位置，并设置他们的重生点。
Teleports the calling player to the island number `num` and sets their spawn.

**用法: `/skyisland join [num] [player]`**

将 `player` 传送到岛屿编号为 `num` 的位置，并设置他们的重生点。
Teleports the `player` to the island number `num` and sets their spawn.

**用法: `/skyisland locate [num]`**

打印岛屿编号为 `num` 的坐标。
Prints the coordinates of the island number `num`.
