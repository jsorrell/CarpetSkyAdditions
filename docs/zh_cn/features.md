## 模组特性

#### 失活的珊瑚和失活的珊瑚扇可以冲蚀成沙

提供额外的*沙子*与*红沙*

通过指令`/carpetskyadditions removeDefault coralErosion`可禁用该特性。

失活的珊瑚和失活的珊瑚扇在有水流出的时候每隔几秒就会生成沙子。 火珊瑚会生成红沙。

珊瑚在每次生成沙子之后将会有3%的概率被破坏。

无限自动化农场是可行的，并不复杂。

添加此特性是因为：

- 仅从流浪商人处获取沙子远远无法满足需求。
- 重力方块复制和重复交易bug是没有意义的，随时可能被官方修复。
- 尸壳掉落沙子的旧途径很无聊，因为它只是另一种形式的刷怪塔。

---

#### Shulkers Spawn On Dragon Kill

提供*潜影贝*

通过指令`/carpetskyadditions removeDefault shulkerSpawning`可禁用该特性

当末影龙被再次击杀时，一只潜影贝会在基岩祭坛的顶部生成。

---

#### 山羊撞击分解地狱疣块

提供*地狱疣*

通过指令`/carpetskyadditions removeDefault rammingWart`可禁用该特性

When a Goat rams a Nether Wart Block, it will break into Nether Wart.

---

#### 浓稠的药水可将石头转化为深板岩

提供*深板岩*

通过指令`/carpetskyadditions removeDefault renewableDeepslate`可禁用该特性

通过指令`/carpetskyadditions setDefault renewableDeepslate no_splash`仅禁用溅射药水生效

右键点击或通过发射器使用的浓稠药水可以将对应的石头转化成为深板岩

溅射的浓稠药水也会将所有覆盖的石头方块转化成深板岩。

---

#### Netherrack and Nylium Generates with Nether Portal Structures

提供*下界岩*

通过指令`/carpetskyadditions removeDefault renewableNetherrack`可禁用该特性

When a Nether Portal generates in the void, it generates a few blocks of Netherrack or Nylium around it.

Which block is generated depends on the Biome -- Crimson Nylium in Crimson Forests, Warped Nylium in Warped Forests, Netherrack elsewhere.

---

#### 流浪商人销售高花

提供*高花*

通过指令`/carpetskyadditions removeDefault tallFlowersFromWanderingTrader`可禁用该特性

Tall Flowers trades mimic Bedrock.

##### 追加的一级交易内容

| 物品    | 价格 | 失效前可交易次数 |
| ----- | -- | -------- |
| Lilac | 1  | 12       |
| 玫瑰丛   | 1  | 12       |
| 牡丹    | 1  | 12       |
| 向日葵   | 1  | 12       |

---

#### Vexes Can Be Allayed

提供*熔岩*

通过指令`/carpetskyadditions removeDefault allayableVexes`可禁用该特性

Play Vexes the right sequence of 5 Note Block notes to convert them to Allays.

恼鬼会检测周围16格范围内的音符盒，并根据是否演奏了正确的音符而发出粒子效果。 乐器种类和八度音阶都将被忽略，这意味着F#<sub>3</sub>和F#<sub>5</sub>将被视作同一个音符。

当恼鬼处于矿车中时，可以使用比较器和探测铁轨来确定序列中的下一个音符。 比较器会输出一个从0（对应 F#）到 11（对应 F）的值。

---

#### 狐狸携带甜浆果生成

提供*甜浆果*

通过指令`/carpetskyadditions setDefault foxesSpawnWithSweetBerriesChance <chance>`可调整生成甜浆果的概率

通过指令`/carpetskyadditions removeDefault foxesSpawnWithSweetBerriesChance`可禁用该特性

当一只狐狸携带物品生成时，有20%的概率为甜浆果。 狐狸在生成之后很快就会吃掉它，所以请动作快点。

---

#### 铁砧压合煤炭块为钻石

提供*钻石*

通过指令`/carpetskyadditions removeDefault renewableDiamonds`可禁用该特性

A Falling Anvil compresses a stack of Coal Blocks into a Diamond.

---

#### 雷击藤蔓使其通电

提供*发光地衣*

通过指令`/carpetskyadditions removeDefault lightningElectrifiesVines`可禁用该特性

If lightning strikes Glowstone with vines attached, the vines will turn into Glow Lichen. It can also strike a Lightning Rod on the Glowstone.

---

#### 紫颂植物在末地小岛上生成

提供*紫颂果*和*紫颂花*

通过指令`/carpetskyadditions removeDefault gatewaysSpawnChorus`可禁用该特性

When an End Gateway is taken to a position over the void, the Endstone island generated spawns with a Chorus Tree on it.

---

#### 海豚可以找到海洋之心

提供*海洋之心*

通过指令`/carpetskyadditions removeDefault renewableHeartsOfTheSea`可禁用该特性

当海豚被喂食鱼后，它会在海底的沙子或砂砾中找到一个海洋之心。

Make sure to give the Dolphin enough space to search.

必须在海洋群系——这玩意儿是海洋之心，而不是丛林之心

---

#### Budding Amethysts Can Be Generated

提供*紫水晶母岩*

通过指令`/carpetskyadditions removeDefault renewableBuddingAmethysts`可禁用该特性

A lava block surrounded by Calcite which is then surrounded by Smooth Basalt will eventually turn into a Budding Amethyst.

##### 如何搭建该结构

![熔岩源](../screenshots/amethyst_step_1_240.png?raw=true "紫水晶母岩再生步骤1") ----> ![用方解石包围熔岩](../screenshots/amethyst_step_2_240.png?raw=true "紫水晶母岩再生步骤2") ----> ![用平滑玄武岩包围方解石](../screenshots/amethyst_step_3_240.png?raw=true "紫水晶母岩再生步骤3")

在一定时间后（平均约2小时），处于结构正中心的熔岩将会转变为紫水晶母岩

![熔岩变成了紫水晶母岩](../screenshots/amethyst_result_240.png?raw=true "紫水晶母岩再生结果")

---

#### Saplings Die on Sand

提供*枯萎的灌木*

通过指令`/carpetskyadditions removeDefault saplingsDieOnSand`可禁用该特性

树苗可以放在沙子和红沙上。

一段时间后它们会枯萎并转化为枯死的灌木

---

#### Ender Dragons Can Drop their Head

提供*龙首*

通过指令`/carpetskyadditions removeDefault renewableDragonHeads`可禁用该特性

当末影龙被**闪电苦力怕**击杀时，她会掉落她的头颅。

---

#### 巨型蘑菇生成菌丝

提供*菌丝*

通过指令`/carpetskyadditions removeDefault hugeMushroomsSpreadMycelium`可禁用该特性

当巨型蘑菇长成时，它会往附近的方块上扩散菌丝，该机制类似于巨型云杉扩散灰化土。

---

#### 具有音波定位能力的生物在被音爆杀死时掉落回响碎片

提供*回响碎片*

通过指令`/carpetskyadditions removeDefault renewableEchoShards`可禁用该特性

蝙蝠和海豚在被监守者释放的音爆杀死时会掉落回响碎片

---

#### 监守者附近的附魔台可以提供迅捷潜行的附魔

提供*迅捷潜行*

通过指令`/carpetskyadditions removeDefault renewableSwiftSneak`可禁用该特性

处于监守者周围8格范围内的附魔台可以为其上的物品提供迅捷潜行的附魔。

---

#### 毒马铃薯转化蜘蛛

提供*洞穴蜘蛛*

通过指令`/carpetskyadditions removeDefault poisonousPotatoesConvertSpiders`可禁用该特性

对蜘蛛使用毒马铃薯可将其转化为洞穴蜘蛛

---

#### 流浪商人出售熔岩

提供*熔岩*

通过指令`/carpetskyadditions setDefault lavaFromWanderingTrader true`启用该特性

***默认禁用 - 取而代之的是通过村庄英雄的礼物来获取熔岩***

##### 追加的二级交易内容

| 物品  | 价格 | Input Item | 失效前可交易次数 |
| --- | -- | ---------- | -------- |
| 熔岩桶 | 16 | 桶          | 1        |

### Carpet Features

默认状态下安装也会启用以下`fabric-carpet`的特性：

- 启用[renewableSponges](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#renewablesponges)选项以再生海绵
  - 运行指令`/carpet removeDefault renewableSponges`可禁用该特性
- 启用[piglinsSpawningInBastions](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#piglinsSpawningInBastions)选项以在堡垒遗迹中再生猪灵
  - 运行指令`/carpet removeDefault piglinsSpawningInBastions`可禁用该特性
