## 模组特性

#### 失活的珊瑚和失活的珊瑚扇可以冲蚀成沙 ####

提供额外的*沙子*与*红沙*

通过指令```/carpetskyadditions removeDefault coralErosion```可禁用该特性。

失活的珊瑚和失活的珊瑚扇在有水流出的时候每隔几秒就会生成沙子。
火珊瑚会生成红沙。

珊瑚在每次生成沙子之后将会有3%的概率被破坏。

无限自动化农场是可行的，并不复杂。

添加此特性是因为：

- 仅从流浪商人处获取沙子远远无法满足需求。
- 重力方块复制和重复交易bug是没有意义的，随时可能被官方修复。
- 尸壳掉落沙子的旧途径很无聊，因为它只是另一种形式的刷怪塔。

---

#### 潜影贝生成于击杀末影龙后 ####

提供*潜影贝*

通过指令```/carpetskyadditions removeDefault shulkerSpawning```可禁用该特性

当末影龙被再次击杀时，一只潜影贝会在基岩祭坛的顶部生成。

---

#### 山羊撞击分解地狱疣块 ####

提供*地狱疣*

通过指令```/carpetskyadditions removeDefault rammingWart```可禁用该特性

当山羊撞击地狱疣块时，它会分解为地狱疣。

---

#### 浓稠的药水可将石头转化为深板岩 ####

提供*深板岩*

通过指令```/carpetskyadditions removeDefault renewableDeepslate```可禁用该特性

通过指令```/carpetskyadditions setDefault renewableDeepslate no_splash```仅禁用溅射药水生效

右键点击或通过发射器使用的浓稠药水可以将对应的石头转化成为深板岩

溅射的浓稠药水也会将所有覆盖的石头方块转化成深板岩。

---

#### 下界岩通过下界传送门结构生成 ####

提供*下界岩*

通过指令```/carpetskyadditions removeDefault renewableNetherrack```可禁用该特性

当在虚空生成下界传送门时，其周围会生成下界岩。
这与基岩版的设计保持一致。

---

#### 流浪商人销售高花 ####

提供*高花*

通过指令```/carpetskyadditions removeDefault tallFlowersFromWanderingTrader```可禁用该特性

高花交易模仿自基岩版

##### 追加的一级交易内容 #####

| 物品   | 价格 | 失效前可交易次数 |
|--------|-----|-----------------|
| 丁香   | 1    | 12             |
| 玫瑰丛 | 1    | 12             |
| 牡丹   | 1    | 12             |
| 向日葵 | 1    | 12             |

---

#### 恼鬼可以被平复 ####

提供*悦灵*

通过指令```/carpetskyadditions removeDefault allayableVexes```可禁用该特性

使用5个音符盒对恼鬼弹奏正确的旋律可以将他们转化成悦灵。

恼鬼会检测周围16格范围内的音符盒，并根据是否演奏了正确的音符而发出粒子效果。乐器种类和八度音阶都将被忽略，这意味着F#<sub>3</sub>和F#<sub>5</sub>将被视作同一个音符。

当恼鬼处于矿车中时，可以使用比较器和探测铁轨来确定序列中的下一个音符。比较器会输出一个从0（对应 F#）到 11（对应 F）的值。

---

#### 狐狸携带甜浆果生成 ####

提供*甜浆果*

通过指令```/carpetskyadditions removeDefault foxesSpawnWithSweetBerriesChance```可禁用该特性

通过指令```/carpetskyadditions setDefault foxesSpawnWithSweetBerriesChance <chance>```可调整生成甜浆果的概率

当一只狐狸携带物品生成时，有20%的概率为甜浆果。
狐狸在生成之后很快就会吃掉它，所以请动作快点。

---

#### 铁砧压合煤炭块为钻石 ####

提供*钻石*

通过指令```/carpetskyadditions removeDefault renewableDiamonds```可禁用该特性

下落的铁砧可将整组煤炭块转化为钻石。

---

#### 雷击藤蔓使其通电 ####

提供*发光地衣*

通过指令```/carpetskyadditions removeDefault lightningElectrifiesVines```可禁用该特性

如果闪电击中附着于萤石上的藤蔓，则藤蔓将会转变为发光地衣，击中萤石上的避雷针时该特性依旧有效。

---

#### 紫颂植物在末地小岛上生成 ####

提供*紫颂果*和*紫颂花*

通过指令```/carpetskyadditions removeDefault gatewaysSpawnChorus```可禁用该特性

当一个末地折跃门在虚空上方生成时，其伴生的末地石小岛将会生成一株紫颂植物。

---

#### 海豚可以找到海洋之心 ####

提供*海洋之心*

通过指令```/carpetskyadditions removeDefault renewableHeartsOfTheSea```可禁用该特性

当海豚被喂食鱼后，它会在海底的沙子或砂砾中找到一个海洋之心。

必须在海洋群系——这玩意儿是海洋之心，而不是丛林之心

---

#### 紫水晶母岩可再生 ####

提供*紫水晶母岩*

通过指令```/carpetskyadditions removeDefault renewableBuddingAmethysts```可禁用该特性

当熔岩方块被方解石包围后，外围再被平滑玄武岩包围时，最终会变为一个紫水晶母岩方块

##### 如何搭建该结构 #####

![熔岩源](../screenshots/amethyst_step_1_240.png?raw=true "紫水晶母岩再生步骤1")
---->
![用方解石包围熔岩](../screenshots/amethyst_step_2_240.png?raw=true "紫水晶母岩再生步骤2")
---->
![用平滑玄武岩包围方解石](../screenshots/amethyst_step_3_240.png?raw=true "紫水晶母岩再生步骤3")

在一定时间后（平均约2小时），处于结构正中心的熔岩将会转变为紫水晶母岩

![熔岩变成了紫水晶母岩](../screenshots/amethyst_result_240.png?raw=true "紫水晶母岩再生结果")

---

#### 树苗会在沙子上枯死 ####

提供*枯萎的灌木*

通过指令```/carpetskyadditions removeDefault saplingsDieOnSand```可禁用该特性

树苗可以放在沙子和红沙上。

一段时间后它们会枯萎并转化为枯死的灌木

---

#### 末影龙掉落龙首 ####

提供*龙首*

通过指令```/carpetskyadditions removeDefault renewableDragonHeads```可禁用该特性

当末影龙被**闪电苦力怕**击杀时，她会掉落她的头颅。

---

#### 巨型蘑菇生成菌丝 ####

提供*菌丝*

通过指令```/carpetskyadditions removeDefault hugeMushroomsSpreadMycelium```可禁用该特性

当巨型蘑菇长成时，它会往附近的方块上扩散菌丝，该机制类似于巨型云杉扩散灰化土。

---

#### 具有音波定位能力的生物在被音爆杀死时掉落回响碎片 ####

提供*回响碎片*

通过指令```/carpetskyadditions removeDefault renewableEchoShards```可禁用该特性

蝙蝠和海豚在被监守者释放的音爆杀死时会掉落回响碎片

---

#### 监守者附近的附魔台可以提供迅捷潜行的附魔 ####

提供*迅捷潜行*

通过指令```/carpetskyadditions removeDefault renewableSwiftSneak```可禁用该特性

处于监守者周围8格范围内的附魔台可以为其上的物品提供迅捷潜行的附魔。

---

#### 毒马铃薯转化蜘蛛 ####

提供*洞穴蜘蛛*

通过指令```/carpetskyadditions removeDefault poisonousPotatoesConvertSpiders```可禁用该特性

对蜘蛛使用毒马铃薯可将其转化为洞穴蜘蛛

---

#### 流浪商人可以骑骆驼生成 ####

提供*骆驼*

通过指令```/carpetskyadditions removeDefault traderCamels```可禁用该特性

当流浪商人在沙漠或坏地群系（标签`carpetskyadditions:wandering_trader_spawns_on_camel`）生成时，
它不会有商人羊驼，而是会骑着骆驼。

如果流浪商人仍在骑骆驼，那么当商人消失时，骆驼也会一起消失。

当流浪商人骑着骆驼时，骆驼不能被骑乘、喂食或用牵引绳牵引。

注意：如果只安装在服务器端，商人会显示为站在骆驼上，而不是坐着。此外，喂食或牵引骑着的骆驼时，
会显示为使用仙人掌或牵引绳。这仅是客户端的问题，建议不要尝试与商人的骆驼互动。

---

#### 小型垂滴叶可以繁殖 ####

提供额外的*小型垂滴叶*

通过指令```/carpetskyadditions removeDefault spreadingSmallDripleaves```可禁用该特性

当小型垂滴叶种植在黏土上时，底部半截被水淹没，顶部没有，并且顶端的光照等级恰好为5时，它可以繁殖。繁殖机制类似于蘑菇。

它只能传播到符合相同条件的方块，且可以在水平5格内、垂直2格的范围内扩散。较近的方块更有可能传播。

它只能传播到一个位置，且周围5x2x5的范围内最多只能有15个小型垂滴叶方块（上下两部分都计入）。

在空岛世界中，小型垂滴叶通常非常有限，因为只能通过流浪商人一次获取10个。
本特性允许玩家对它们进行种植和更广泛的使用。

---

#### 珊瑚可以扩散到方解石上 ####

提供额外的 珊瑚方块

通过指令```/carpetskyadditions removeDefault spreadingCoral```可禁用该特性

当一个方解石方块周围的3x3范围内至少有8个相同类型的珊瑚方块时，它可以在随机刻上转变为该珊瑚方块（如果条件适宜）。

转变的概率取决于位置的适宜度。
适宜度基于[生成温度和大陆性参数](https://zh.minecraft.wiki/w/%E7%94%9F%E7%89%A9%E7%BE%A4%E7%B3%BB#%E4%B8%BB%E4%B8%96%E7%95%8C_3)。
理想位置定义为温度0.65和大陆性-0.3，意味着靠近海岸、温暖的地方。
这些数值在单人游戏的F3屏幕上可见。

算法如下：

```
如果维度不是主世界，则
    适宜度 = 0
否则如果是平坦世界，则
    适宜度 = 0.5
否则
    # 此数值在0到1之间取值
    适宜度 = 1 - ((温度 - 0.65)^2 + (大陆性 + 0.3)^2)
```

随机刻时转变的概率为`适宜度 * 0.49 + 0.01`。

在最适宜的条件下，转变平均约需2分钟。在最不适宜的条件下，平均需要近2小时。

在空岛世界中，珊瑚方块通常非常有限，因为只能通过流浪商人一次获取8个。
本特性允许玩家对它们进行种植和更广泛的使用。

---

#### 流浪商人出售熔岩  ####

提供*熔岩*

通过指令```/carpetskyadditions setDefault lavaFromWanderingTrader true```启用该特性

***默认禁用 - 取而代之的是通过村庄英雄的礼物来获取熔岩***



##### 追加的二级交易内容 #####

| 获得物品 | 消耗物品 | 价格 | 失效前可交易次数 |
|---------|---------|------|-----------------|
| 熔岩桶   | 桶      | 16   | 1               |

### 地毯模组特性

默认状态下安装也会启用以下`fabric-carpet`的特性：

+ 启用[renewableSponges](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#renewablesponges)选项以再生海绵
  + 运行指令`/carpet removeDefault renewableSponges`可禁用该特性
+ 启用[piglinsSpawningInBastions](https://github.com/gnembon/fabric-carpet/wiki/Current-Available-Settings#piglinsSpawningInBastions)选项以在堡垒遗迹中再生猪灵
  + 运行指令`/carpet removeDefault piglinsSpawningInBastions`可禁用该特性
