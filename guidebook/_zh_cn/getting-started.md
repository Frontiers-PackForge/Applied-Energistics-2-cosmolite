---
navigation:
  title: 开始与入门（1.20+）
  position: 10
---

<div class="notification is-info">
  以下信息仅适用于 Minecraft 1.20 及更新版本中的 Applied Energistics 2。
</div>

# 开始与入门

## 获取初始材料

<GameScene zoom="4" background="transparent">
  <ImportStructure src="assets/assemblies/meteor_interior.snbt" />
</GameScene>

要上手 Applied Energistics 2，首先需要找到一处[陨石](ae2-mechanics/meteorites.md)。陨石相当常见，落地往往会在地形上留下大坑，因此你也许早就见过。

若还没遇到过，可以合成一枚 <ItemLink id="meteorite_compass" />，它会指向最近的 <ItemLink id="mysterious_cube" />。

找到陨石后，向中心挖掘。你会看到赛特斯石英簇、赛特斯石英芽、各种类型的[赛特斯石英母岩](items-blocks-machines/budding_certus.md)，以及位于中央的神秘方块。

请挖下赛特斯石英簇以及见到的赛特斯石英块。赛特斯石英母岩也可以整组带走，但若**没有**精准采集，母岩会**降一级**。

不要破坏任何无瑕赛特斯石英母岩：即便有精准采集，它们也会降为有瑕母岩，且无法再修回无瑕。

也请挖掉中央的神秘方块，以获得全部 4 枚压印模板。

## 培养赛特斯石英

<GameScene zoom="4" background="transparent">
<ImportStructure src="assets/assemblies/budding_certus_1.snbt" />
</GameScene>

赛特斯石英芽会从[赛特斯石英母岩](items-blocks-machines/budding_certus.md)上长出，机制类似紫水晶。若打掉**尚未**长成的芽，会掉落 1 个 <ItemLink id="certus_quartz_dust" />，**不受**时运影响。若打掉**完全**长成的簇，会掉落 4 个 <ItemLink id="certus_quartz_crystal" />，时运可增加掉落量。

赛特斯石英母岩共有 4 个等级：**无瑕、有瑕、开裂、破损**。

<GameScene zoom="4" background="transparent">
<ImportStructure src="assets/assemblies/budding_blocks.snbt" />
<IsometricCamera yaw="195" pitch="30" />
</GameScene>

每当芽再长一阶，母岩都有几率**降一级**，最终退化成普通赛特斯石英块。把母岩（或普通赛特斯石英块）与一枚或多枚 <ItemLink id="charged_certus_quartz_crystal" /> 一起丢进水里，即可**修复**并生成新的母岩。

<RecipeFor id="damaged_budding_quartz" />

无瑕母岩**不会**降级，可无限产出赛特斯石英；但它们**无法**合成，也无法用镐（含精准采集）完好挖走搬运。（不过*可以*用[空间存储](ae2-mechanics/spatial-io.md)整体搬迁。）

单靠自然生长，芽会长得非常慢。好在把 <ItemLink id="growth_accelerator" /> 贴在母岩旁边能**大幅**加速——建议尽早做几个。

<GameScene zoom="4" background="transparent">
<ImportStructure src="assets/assemblies/budding_certus_2.snbt" />
<IsometricCamera yaw="195" pitch="30" />
</GameScene>

若石英还不够做 <ItemLink id="energy_acceptor" /> 或 <ItemLink id="vibration_chamber" />，可以先做一个 <ItemLink id="crank" />，装在催生器末端手摇供能。

自动采收赛特斯石英的做法见[此处](example-setups/simple-certus-farm.md)。

## 福鲁伊克斯简述

你还会需要福鲁伊克斯——做催生器时你已经见过它了：把**充能赛特斯**、红石与下界石英丢进水里即可合成。若要全自动丢水合成，「留给读者作为习题」。

若还没做过，需要 <ItemLink id="charger" /> 才能生产 <ItemLink id="charged_certus_quartz_crystal" />。

## 压印一些处理器

搜刮陨石时，击破神秘方块会得到 4 枚「压印模板」，它们要在 <ItemLink id="inscriber" /> 里用来压制三种处理器。

<ItemGrid>
  <ItemIcon id="silicon_press" />

  <ItemIcon id="logic_processor_press" />

  <ItemIcon id="calculation_processor_press" />

  <ItemIcon id="engineering_processor_press" />
</ItemGrid>

压印器是**分面**机器，和原版熔炉类似：从**顶/底**送入物品会进上/下槽；从**侧面或背面**送入会进中间槽。产物可从**侧面或背面**抽出。

为方便用漏斗自动化（也顺便少点管线纠缠），可用 <ItemLink id="certus_quartz_wrench" /> **旋转**压印器朝向。

请预先做出若干枚各型处理器，为下一步——搭一套最基础的 ME 系统——做准备。处理器产线的自动化同样「留给读者作为习题」。

## 物质能量科技：ME 网络与存储

### ME 存储是什么？

读音是 Emm-Eee，代表 **Matter Energy（物质—能量）**。

物质能量是 Applied Energistics 2 的核心：它像科学家魔改版的多方块箱子，能彻底改变你的存储方式。ME 与常见存储模组差别很大，刚上手可能要一点「跳出框架」的思维方式；一旦习惯，极小空间内海量存储、多台终端同时访问，都只是冰山一角。

### 入门需要先搞懂什么？

首先，ME 把物品存在**另一种物品**里，这类物品叫做[存储元件](items-blocks-machines/storage_cells.md)，共分 5 级，容量逐级变大。要使用存储元件，必须把它放进 <ItemLink id="chest" /> 或 <ItemLink id="drive" />。

<ItemLink id="chest" /> 在放入元件后会立刻显示其中内容，并可像 <ItemLink id="minecraft:chest" /> 那样存取——区别在于物品实际存在**存储元件**里，而不是 <ItemLink id="chest" /> 本体。

<ItemLink id="chest" /> 很适合用来理解 ME 的概念；要真正吃透 AE2，还需要搭一套 [ME 网络](ae2-mechanics/me-network-connections.md)。

## 你的第一个 ME 系统

现在你已经凑齐了 AE2 的基础材料与机器，可以搭建第一个 **ME（物质能量）**系统。这套会非常基础：没有自动合成、没有物流，只有简单、可搜索的存储。

<GameScene zoom="6" interactive={true}>
<ImportStructure src="assets/assemblies/tiny_me_system.snbt" />

</GameScene>

* 材料清单：
    * 1× <ItemLink id="drive" />
    * 1× <ItemLink id="terminal" /> 或 <ItemLink id="crafting_terminal" />
    * 1× <ItemLink id="energy_acceptor" />
    * 若干[线缆](items-blocks-machines/cables.md)：玻璃、包层、智能均可，**不要**用致密线缆
    * 若干[存储元件](items-blocks-machines/storage_cells.md)：新手推荐 4k，在「种类数」与「容量」之间较均衡（更省心的做法是把 4k 与 1k 搭配并[分区](items-blocks-machines/cell_workbench.md)，但此处暂不展开）

---

1. 先放下驱动器。
2. <ItemLink id="energy_acceptor" />（以及其他多种 AE2 [设备](ae2-mechanics/devices.md)）有**方块**与**平板**两种形态，可在合成栏里互转。若你的接收器是方块形态，就紧挨着驱动器放置；若是平板形态，先在驱动器上接一根线缆，再把接收器贴在线缆上。
3. 用你最喜欢的产能模组里的线缆/管道/导管，把能量送进能源接收器。
4. 在驱动器**上方**（或大致与眼睛齐平的位置）接一根线缆，再把终端或合成终端装在这根线缆上。
5. 把存储元件塞进驱动器。
6. 开张致富。
7. 随便点点终端里的设置。
8. 陶醉于自己无所不能的感觉。
9. 然后意识到：放在全局里看，这套网其实还挺小的。

### 扩展你的网络

有了基础存储与访问方式只是起步，接下来你多半会想自动化一些处理。

一个经典例子：在**熔炉顶面**装 <ItemLink id="export_bus" /> 往炉子里喂矿石，在**熔炉底面**装 <ItemLink id="import_bus" /> 把烧好的产物抽回网络。

<ItemLink id="export_bus" /> 负责把物品从网络**导出**到所附容器；<ItemLink id="import_bus" /> 则把所附容器里的物品**导入**网络。

### 突破限制

到这一步，你大概已经快要挂满 8 台左右的[设备](ae2-mechanics/devices.md)了；一旦达到 9 台，就要开始管理[频道](ae2-mechanics/channels.md)。许多（并非全部）设备需要占用频道才能工作。

默认情况下，一张网只有 8 个频道；超出后就必须加入 <ItemLink id="controller" />，才能把网络大幅做大。[智能线缆](items-blocks-machines/cables.md)可以直观显示频道如何走线——初学频道时建议多用；若你手里红石和荧石很多，也可以多用。
