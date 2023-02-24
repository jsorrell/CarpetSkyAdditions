# 地毯端空岛拓展

[![GitHub downloads](https://img.shields.io/github/downloads/jsorrell/CarpetSkyAdditions/total?label=Github%20downloads&logo=github)](https://github.com/jsorrell/CarpetSkyAdditions/releases)
[![CurseForge downloads](http://cf.way2muchnoise.eu/full_633402_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-sky-additions)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/3oX3JnAP?label=Modrinth%20Downloads)](https://modrinth.com/mod/carpet-sky-additions)

[![en](https://img.shields.io/badge/lang-en-red.svg)](/README.md)
[![zh_cn](https://img.shields.io/badge/lang-zh--cn-yellow.svg)](/docs/zh_cn/README.md)

SkyBlock应用是一个基于[skyrising/skyblock](https://github.com/skyrising/skyblock)
发展而来的[fabric-carpet](https://github.com/gnembon/fabric-carpet)拓展模组。

该模组致力于在原版基础上为玩家提供专业的的空岛游戏体验，这取决于玩家对于Minecraft游戏机制（以及特性）的了解。在某些时候诸如[Chunkbase](https://www.chunkbase.com/)
或[MiniHUD](https://www.curseforge.com/minecraft/mc-mods/minihud)等外部工具会很有用，建议搭配他们来获得更好的游戏体验。

尽管我已经尽力去将这方面的不良体验最小化，但某些时候游戏的进度依旧会比较折磨或者需要一定时间的挂机（AFK）。
除非你在世界生成时选择了**空岛**或专门启用了该模组相关的特性，否则该模组不会对游戏内容进行任何修改。
这意味着空岛世界和非空岛世界可以被随意切换而无需重启客户端。

## 安装

[![Vanilla Sky: Everything from Nothing](http://cf.way2muchnoise.eu/title/624853.svg)](https://www.curseforge.com/minecraft/modpacks/vanilla-sky)

这是一个Fabric模组并需要以其他模组类似的方式安装。

在使用时依赖于`fabric-carpet`和`fabric-api`两个模组的安装。

安装后，需要运行指令`datapack enable "carpetskyadditions/skyblock"`以启用数据包。

将翻译包放入资源包文件夹中可以实现成就的汉化。

[详细安装细节](installation.md)

## 特性

### 空岛生成
空岛世界的生成与常规世界完全一致，但所有方块均被移除，所有群系以及结构生成框架均得到保留。这意味着尸壳将正常于沙漠生成，烈焰人也会在下界堡垒中刷出，其余生物也如此。

即便是移除了几乎所有的方块，你依旧可以体验到原版游戏中的绝大多数内容。
[更多生成细节](generation.md)

### 玩法变更

然而空岛世界的生成依旧留下了一部分无法（以原版方式）获取的资源。
除了为其增添空岛特有的生成形式外，这个模组也尽量填补了其中设定上的空白，力求相对于原版体验做出更少的改变。

最大的进度障碍来自于熔岩，在默认状态下无法获得。
这会导致玩家无法前往下界或者获取圆石。
本模组通过提供一种熔岩的获取方式来解决这个问题。

在未进行调整的空岛世界中，沙子的常规获取途径也非常有限，因此这个模组允许玩家通过其他方式获得更多的沙子。

而这个模组提供的大多数其他资源都是装饰性的，不会对游戏进度造成重大影响，比如枯死的灌木和龙首。

如果条件允许，这些变更的内容都被添加到数据包中，而非以代码的形式写入模组中，这方便了玩家自定义相关内容。
当前的版本中，数据包已被内置入模组之中。

空岛进度也被添加到了进度引导中，以记录模组对原版游戏内容的修改。

*当玩家使用默认设置安装时，原版游戏中可获得的所有方块、物品、生物和进度都可在空岛世界中获得。

[模组特性列表](features.md)

[数据包特性列表](datapack.md)

### 游戏流程指南

如果你在游戏过程中对游戏流程产生了疑问，可以通过查看[游戏流程指南](progression.md)来了解下一步的游戏内容。

## 致谢
+ [@skyrising](https://github.com/skyrising/skyblock)提供该mod的最初灵感和一些源代码
+ [@DeadlyMC](https://github.com/DeadlyMC/Skyblock-datapack)提供数据包的设计灵感
+ [@gnembon](https://github.com/gnembon/fabric-carpet)为地毯模组的作者

## 许可
该项目根据MIT许可条款进行许可
