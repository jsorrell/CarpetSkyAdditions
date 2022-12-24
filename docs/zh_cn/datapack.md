## 数据包特性

### 自定义

如需自定义数据包，请[于此](https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2Fjsorrell%2FCarpetSkyAdditions%2Ftree%2FHEAD%2Fdatapack)
下载，并根据个人喜好进行编辑。

禁用内置数据包后启用您编辑的数据包即可使用自定义数据包。

---

#### 村民赠予熔岩桶 ####

提供*熔岩*

通过在数据包中删除```data/minecraft/loot_tables/gameplay/hero_of_the_village```可移除该特性

熔岩桶将可以作为村庄英雄的礼物从盔甲匠、武器匠和工具匠那里获得。

---

#### 可通过末影螨获得鞘翅 ####

提供*鞘翅*

通过在数据包中删除```data/minecraft/loot_tables/entities/endermite.json```可移除该特性

当玩家杀死同时受缓降和悬浮影响的末影螨时有几率掉落鞘翅，抢夺附魔可增加掉率。

---

#### 猪灵蛮兵掉落远古残骸 ####

提供*远古残骸*

通过在数据包中删除```data/minecraft/loot_tables/entities/piglin_brute.json```可移除该特性

地毯模组通过配置可令猪灵蛮兵在堡垒遗迹中再生。因此通过击杀它们可以概率掉落远古残骸。

---

#### 可再生方解石与凝灰岩 ####

提供*方解石*和*凝灰岩*

通过在数据包中删除```data/skyblock/recipes/[tuff_from_blasting_andesite.json + calcite_from_blasting_diorite.json]```可移除该特性

将闪长岩放入高炉烧制获得方解石

将安山岩放入高炉烧制获得凝灰岩

---

#### 可合成发光浆果 ####

提供*发光浆果*

通过在数据包中删除```data/skyblock/recipes/glow_berries.json```可移除该特性

使用荧光墨囊和甜浆果合成发光浆果

---

#### 蜘蛛骑士掉落蜘蛛网 ####

提供*蜘蛛网*

通过在数据包中删除```data/minecraft/loot_tables/entities/[skeleton.json + spider.json]```可移除该特性

当玩家杀死一个蜘蛛骑士时，先被击杀的部分将会掉落蜘蛛网。

---

#### 可可豆可在丛林群系被钓出 ####

提供*可可豆*

通过在数据包中删除```data/minecraft/loot_tables/gameplay/fishing/junk.json```可移除该特性

与基岩版匹配，可可豆可以作为丛林特色物品在丛林中钓鱼被钓出。

---

#### 锻造台可用于合成矿石 ####

提供*矿石*

通过在数据包中删除```data/skyblock/recipes/*_ore_smithing.json```可移除该特性

所有矿石都可以在锻造台上通过一块基材和一个矿物块进行合成。

举个例子，下界金矿石可以通过一个下界岩和一个金块合成。

---

#### 马铠可合成 ####

提供*铁马铠*，*金马铠*和*钻石马铠*

通过在数据包中删除```data/skyblock/recipes/*_horse_armor.json```可移除该特性

使用对应材料在工作台上摆放为**H**型以合成对应马铠。

---

#### 猫的晨礼提供附魔金苹果 ####

提供*附魔金苹果*

通过在数据包中删除```data/minecraft/loot_tables/gameplay/cat_morning_gift.json```可移除该特性

猫有极低的概率将附魔金苹果作为早晨礼物提供给玩家。

---

#### 猪灵交易提供镶金黑石 ####

提供*镶金黑石*

通过在数据包中删除```data/minecraft/loot_tables/gameplay/piglin_bartering.json```可移除该特性

猪灵有极低的概率在交易时给出镶金黑石。

---

#### 苦力怕在特定结构中掉落对应唱片或唱片残片 ####

提供唱片*Pigstep*、*otherside*和唱片残片*5*

通过在数据包中删除```data/minecraft/loot_tables/entities/creeper.json```可移除该特性

当苦力怕在堡垒遗迹中被骷髅杀死时，有几率掉落唱片“Pigstep”。

当苦力怕在要塞中被骷髅杀死时，有几率掉落唱片“otherside”。

当苦力怕在远古城市被骷髅杀死时，有几率掉落唱片残片 5。

---

#### 僵尸疣猪兽掉落猪鼻旗帜图案 ####

提供*猪鼻旗帜图案*

通过在数据包中删除```data/minecraft/loot_tables/entities/zoglin.json```可移除该特性

标题说明一切。

---

#### 盛开的杜鹃花丛掉落孢子花 ####

提供*孢子花*

通过在数据包中删除```data/minecraft/loot_tables/blocks/flowering_azaliea_leaves.json```可移除该特性

盛开的杜鹃花丛被破坏时有概率掉落孢子花，该操作受时运附魔的影响

---

#### 末影人可以拾取高草丛和大型蕨 ####

提供*高草丛*和*大型蕨*

通过在数据包中删除```data/minecraft/tags/blocks/enderman_holdable.json```可移除该特性

杀死拾起高草丛和大型蕨的末影人可以将它们作为掉落物获取。

模组修复了末影人对两格高方块的处理，使其能正确的放置而不会被破坏。

但这一修复并非针对客户端，因此原版的渲染修复并未修复（[MC-193497](https://bugs.mojang.com/browse/MC-193497)）

---

#### 战猪进度调整 ####

在空岛世界，战猪进度是无法获得的，因为它需要打开战利品箱（空岛不会生成）。

现在你可以通过击杀猪灵蛮兵来获得这一进度。
