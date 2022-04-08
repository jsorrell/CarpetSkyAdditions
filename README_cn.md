# SkyBlock

[English](README.md)|**中文**

SkyBlock是一个由[skyrising/skyblock](https://github.com/skyrising/skyblock)修改而来的依赖[fabric-carpet](https://github.com/gnembon/fabric-carpet)的模组。

该模组致力于在原版基础上为玩家提供专业的的空岛游戏体验。在某些时候诸如[Chunkbase](https://www.chunkbase.com/)或[MiniHUD](https://www.curseforge.com/minecraft/mc-mods/minihud)这类工具会很有用，建议搭配使用。尽管我已经尽力去将这方面的不良体验最小化，但某些时候游戏的进度依旧会比较折磨或者需要挂机一定时间。
除非你使用指令开启其相关功能或者在世界生成时选择了**空岛**，否则该模组不会对客户端已经安装的其他存档造成任何影响。

## 安装
+ 安装[Fabric](https://fabricmc.net/use)
+ 下载[fabric-carpet](https://www.curseforge.com/minecraft/mc-mods/carpet/files/)
+ 下载[fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
+ 下载[SkyBlock模组及其数据包](https://github.com/jsorrell/skyblock/releases)
+ 将fabric-carpet，fabric-api和SkyBlock模组放进到`<minecraft文件目录>/mods/`文件夹内

## 使用

### 单人游戏
+ `创建新的世界`
+ 将游戏难度设置为**困难**（以掉落更多的盔甲和武器）
+ 在**数据包**选项中拖入前面下载好的数据包
+ 将**允许作弊**选项调整为开启，以确保你能在游戏内启用模组提供的空岛特性（你也可以在进入游戏后选择对局域网开放，并在那里允许作弊，意面游戏过程中误使用指令）
+ 点击`更多世界选项`
+ 选择`世界类型：空岛`
+ 创建世界
+ 进入游戏后使用指令`/function skyblock:enable_features`以启用空岛特性

### 多人游戏
模组与数据包仅需要在服务端进行设置
+ 以文本编辑器打开`server.properties`（如果没有更好的文本编辑器就请使用记事本打开）
+ 将`level-type=default`修改为`level-type=skyblock`
+ 确保你已经删除或移除了旧有的世界文件夹以创建新的世界
+ 将前面下载好的数据包放进世界存档文件夹中的`datapack`文件夹中
+ 在控制台执行指令`function skyblock:enable_features`以启用空岛特性（或在客户端以OP权限执行`/function skyblock:enable_features`）

## 特性

### 空岛生成
空岛世界的生成与常规世界完全一致，但所有方块均被移除，所有群系以及结构生成框架均得到保留。这意味着尸壳将正常于沙漠生成，烈焰人也会在下界堡垒中刷出，其余生物也如此。

只有两样东西会在世界生成时得到保留：
+ 一个小的出生点平台：
![一个小出生点平台，包括草方块、菌丝、菌岩，以及一棵树](screenshots/spawn_platform.png?raw=true "Spawn Platform")

+ 所有传送门框架和蠹虫刷怪笼：
![末地传送门框架将得到保留](screenshots/end_portal.png?raw=true "End Portal Frame")

尽管几乎所有方块都被溢出了，但在仅使用*原版*特性下，几乎所有方块都可以被获取，几乎所有生物也可以生成。

### 模组追加特性 ###
尽管如此，空岛的生成依旧留下了一些原版可以正常获取但现在无法获得的资源。因此，除了生成世界外，这个模组在尽力确保原汁原味的原版生存基础上填补了这些空缺

---

#### 流浪商人交易追加 ####
提供*高花*与*熔岩桶*的交易

通过指令```/skyblock setDefault wanderingTraderSkyBlockTrades false```可禁用该特性

高花交易灵感来源于基岩版

##### 追加交易列表 #####

###### 级别1
| 物品   | 价格 | 失效前可交易次数 |
|--------|-----|-----------------|
| 丁香   | 1    | 12             |
| 玫瑰丛 | 1    | 12             |
| 牡丹   | 1    | 12             |
| 向日葵 | 1    | 12             |

###### 级别2
| 获得物品 | 消耗物品 | 价格 | 失效前可交易次数 |
|---------|---------|------|-----------------|
| 熔岩桶   | 桶      | 16   | 1               |

---

#### 雷击藤蔓使其通电 ####
提供*发光地衣*

通过指令```/skyblock setDefault lightningElectrifiesVines false```可禁用该特性

如果闪电击中附着于萤石上的藤蔓，则藤蔓将会转变为发光地衣，击中萤石上的避雷针时该特性依旧有效。

#### 紫水晶母岩可再生 ####
提供*紫水晶母岩*

通过指令```/skyblock setDefault renewableBuddingAmethysts false```可禁用该特性

当熔岩方块被方解石包围后，外围再被平滑玄武岩包围时，最终会变为一个紫水晶母岩方块

##### 如何搭建该结构 #####

![熔岩源](screenshots/amethyst_step_1_240.png?raw=true "Budding Amethyst Generation Step 1")
---->
![用方解石包围熔岩](screenshots/amethyst_step_2_240.png?raw=true "Budding Amethyst Generation Step 2")
---->
![用平滑玄武岩包围方解石](screenshots/amethyst_step_3_240.png?raw=true "Budding Amethyst Generation Step 3")

在一定时间后（平均约2小时），处于结构正中心的熔岩将会转变为紫水晶母岩

![熔岩变成了紫水晶母岩](screenshots/amethyst_result_240.png?raw=true "Budding Amethyst Generation Result")

---

#### 末地传送门生成紫颂植物 ####
提供*紫颂果*和*紫颂花*

通过指令```/skyblock setDefault gatewaysSpawnChorus false```可禁用该特性

当一个末地传送门在虚空上方生成时，其伴生的末地石小岛将会生成一颗紫颂树。

---

#### 海豚可以找到海洋之心 ####
提供*海洋之心*

通过指令```/skyblock setDefault renewableHeartsOfTheSea false```可禁用该特性

当海豚被喂食鱼后无法找到宝箱时，它会找到海洋之心。

---

#### 末影龙掉落龙首 ####
提供*龙首*

通过指令```/skyblock setDefault renewableDragonHeads false```可禁用该特性

当末影龙被**高压爬行者**击杀时，她会掉落她的头颅。

---

#### 潜影贝生成于击杀末影龙后 ####
提供*潜影贝*

通过指令```/skyblock setDefault shulkerSpawning false```可禁用该特性

当末影龙被再次击杀时，一只潜影贝会在基岩祭坛的顶部生成。

---

#### 铁砧压合煤炭块为钻石 ####
提供*钻石*

通过指令```/skyblock setDefault renewableDiamonds false```可禁用该特性

下落的铁砧可将整组煤炭块转化为钻石。

---

#### 山羊撞击分解地狱疣块 ####
提供*地狱疣*

通过指令```/skyblock setDefault rammingWart false```可禁用该特性

当山羊撞击地狱疣块时，它会分解为地狱疣。

---

#### 狐狸携带浆果生成 ####
提供*发光浆果*及*甜浆果*

通过指令```/skyblock setDefault foxesSpawnWithBerries false```可禁用该特性

当一只狐狸携带物品生成时，这个物品分别有15%的概率为发光浆果或甜浆果。
狐狸在生成之后很快就会吃掉它，所以请动作快点。

---

### 数据包特性追加 ###
数据包提供了额外的配方和进度。

在条件允许的前提下，我们会通过mod来修改数据包内容以方便用户。（因为生成时选择的数据包无法更改）

---

#### 腐肉合成下界岩 ####
提供*下界岩*

下界岩可以通过9个腐肉合成

---

#### 方解石和凝灰岩可获得 ####
提供*方解石*和*凝灰岩*

将闪长岩放入高炉烧制获得方解石

将安山岩放入高卢烧制获得凝灰岩

---

#### 尸壳掉落沙子 ####
提供额外的*沙子*

尸壳将会掉落沙子，因为从流浪商人交易获得大量沙子是相当乏味的。

---

#### 猪灵蛮兵掉落远古残骸 ####
提供*远古残骸*

地毯模组通过配置可令猪灵蛮兵在堡垒遗迹中再生。因此通过击杀它们可以概率掉落远古残骸。

---

#### 深板岩圆石可制作 ####
提供*深板岩圆石*

在工作台使用9块圆石以合成深板岩圆石

---

#### 末地钓鱼可获得鞘翅 ####
提供*鞘翅*

在末地钓鱼时，鞘翅低概率可以作为宝藏被钓出。

---

#### 红沙配方 ####
提供额外的*红沙*

沙子与红色染料1比1混合可以转化为红沙。

---

#### 蜘蛛骑士掉落蜘蛛网 ####
提供*蜘蛛网*

当玩家杀死一个蜘蛛骑士时，前半段击杀将会掉落蜘蛛网。

---

#### 可可豆可在丛林被钓出以再生 ####
提供*可可豆*

与基岩版匹配，可可豆可以作为丛林特色物品在丛林中钓鱼被钓出。

---

#### 矿石通过锻造台合成 ####
提供*矿石*

所有矿石都可以在锻造台上通过一块基材和一个矿物块进行合成。

举个例子，下界金矿石可以通过一个下界岩和一个金块合成。

---

#### 可合成马铠 ####
提供*铁马铠*，*金马铠*和*钻石马铠*

使用对应材料在工作台上摆放为**H**型以合成对应马铠。

---

#### 猫的礼物中包含附魔金苹果 ####
提供*附魔金苹果*

猫有极低的概率将附魔金苹果作为早晨礼物提供给玩家。

---

#### 猪灵交易包含镶金黑石 ####
提供*镶金黑石*

猪灵有极低的概率在交易时给出镶金黑石。

---

#### 爬行者在下界掉落全部种类的唱片 ####
提供音乐唱片*Pigstep*和*otherside*

当爬行者在下界被骷髅杀死时，它们将可以掉落所有种类的唱片，包括Pigstep和otherside。

---

#### 僵尸疣猪兽掉落猪鼻旗帜图案 ####
提供*猪鼻旗帜图案*

标题说明一切。

---

#### 原版岩浆膏配方移除 ####

原版岩浆膏配方太过简单。请自行建造岩浆怪农场获取。

---

#### 战猪进度调整 ####

在空岛世界，战猪进度是无法获得的，因为它需要打开战利品箱（空岛不会生成）。

现在你可以通过击杀猪灵蛮兵来获得这一进度。

---

### 大量额外的进度 ###

空岛进度将起到引导作用，并用于区分空岛模组和原版。

当启用所有特性时，所有原版进度都可被完成（尽管战猪进度被修改了）

## 地毯特性
安装后还将启用以下`fabric-carpet`的特性。

启用以下`fabric-carpet`选项后，所有原版生存的方块、物品和生物都可以在空岛中正常生成

+ [desertShrubs](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#desertshrubs)选项启用以获得枯萎的灌木
  + 运行指令`/carpet setDefault desertShrubs false`可禁用该特性
+ [renewableSponges](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#renewablesponges) for Sponges选项启用以再生海绵
  + 运行指令`/carpet setDefault renewableSponges false`可禁用该特性
+ [piglinsSpawningInBastions](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#piglinsSpawningInBastions)选项启用以在堡垒遗迹中再生猪灵
  + 运行指令`/carpet setDefault piglinsSpawningInBastions false`可禁用该特性

## 致谢
+ [@skyrising](https://github.com/skyrising/skyblock)提供该mod的灵感和一些源代码
+ [@DeadlyMC](https://github.com/DeadlyMC/Skyblock-datapack)提供数据包的设计灵感
+ [@gnembon](https://github.com/gnembon/fabric-carpet)为地毯模组的作者

## 许可
该项目根据MIT许可条款获得授权
