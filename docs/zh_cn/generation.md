## 空岛世界生成

空岛世界的生成与常规世界完全一致，但所有方块均被移除。
所有群系以及结构生成框架均得到保留。
这意味着尸壳将正常于沙漠生成，烈焰人也会在下界堡垒中刷出，其余生物也如此。

唯一的区别在于末地传送门框架上的末影之眼是随机的——这可能会在未来被调整。

只有少量事物会在伴随世界生成：
+ 一个小的出生点平台：
  ![一个小出生点平台，包括草方块，以及一棵树](../screenshots/spawn_platform.png?raw=true "出生点平台")

+ 末地传送门框架（除非属性`generateEndPortals`被设置为`false`）：
  ![末地传送门框架将得到保留](../screenshots/end_portal.png?raw=true "末地传送门框架")

+ 带有幽匿尖啸体的远古城市传送门框架（除非属性`generateAncientCityPortals`被设置为`false`）：
  ![远古城市传送门框架将得到保留](../screenshots/ancient_city_portal.png?raw=true "远古城市传送门框架")

+ 蠹虫刷怪笼（除非属性`generateSilverfishSpawners`被设置为`false`）

+ 堡垒遗迹的宝藏室中的岩浆刷怪笼（仅当属性`generateMagmaCubeSpawners`设置为`true`时生效）

+ 末地中的所有随机返回折跃门（仅当属性`generateRandomEndGateways`设置为`true`时生效）
