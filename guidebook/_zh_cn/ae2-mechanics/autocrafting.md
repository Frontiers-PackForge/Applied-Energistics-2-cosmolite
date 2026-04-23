---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 自动合成
  icon: pattern_provider
---

# 自动合成

### 重头戏

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="../assets/assemblies/autocraft_setup_greebles.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

自动合成是 AE2 最核心的能力之一。你不用再手动合齐每个中间材料并一层层搓配方，而是直接让 ME 系统代劳。你也可以让系统自动合成并导出到指定位置，或通过一些巧妙组合让库存自动补货。它同样支持流体；如果你装了支持额外材料类型（例如 Mekanism 气体）的附属，也能一并自动化。总之，非常强。

这个主题比较复杂，系好安全带，开始吧。

一套自动合成系统由三部分构成：
- 发起合成请求的对象
- 合成 CPU
- <ItemLink id="pattern_provider" />

基本流程如下：

1. 有东西发起了一个合成请求。可能是你在终端点击了某个可自动合成的物品，也可能是插了合成卡的输出总线或 ME 接口在请求它们配置要输出/维持库存的物品。

*   （**重要：**如果要请求合成你已经有库存的东西，请用你绑定的“选取方块”（通常是鼠标中键）。它可能会和背包整理类模组冲突。）

2. ME 系统会计算完成请求所需的原料和前置步骤，并把这些任务数据放进选中的合成 CPU。

3. 含有对应[样板](../items-blocks-machines/patterns.md)的 <ItemLink id="pattern_provider" /> 会把样板定义的原料推送到相邻容器。
   如果是工作台配方（“合成样板”），目标通常是 <ItemLink id="molecular_assembler" />。
   如果是非工作台配方（“处理样板”），目标则可以是任意机器、方块，或更复杂的红石控制产线。

4. 合成结果会以某种方式回到系统中，比如通过导入总线、接口，或把产物推回某个样板供应器。
   **注意：必须触发一次“物品进入系统”事件；不能只是把产物导进一个挂着 <ItemLink id="storage_bus" /> 的箱子。**

5. 如果这一步是其他步骤的前置，产物会暂存在该合成 CPU 中，并在后续步骤消耗。

# 样板

<ItemImage id="crafting_pattern" scale="4" />

样板在 <ItemLink id="pattern_encoding_terminal" /> 中由空白样板编码而成。

按用途不同，样板分为多种类型：

*   <ItemLink id="crafting_pattern" />：编码工作台配方。你可以直接塞进 <ItemLink id="molecular_assembler" />，让它在拿到原料时执行合成；但更常见的用法是放进贴着分子装配室的 <ItemLink id="pattern_provider" />。
    在这种布局下，样板供应器会把对应样板信息和原料一起发给相邻装配室。由于装配室会自动把结果弹出到相邻容器，样板供应器 + 分子装配室就足够完成这类自动化。

***

*   <ItemLink id="smithing_table_pattern" />：与合成样板类似，但编码锻造台配方。它同样通过样板供应器 + 分子装配室自动化，机制完全一致。实际上，合成、锻造、切石样板可以共用一套设施。

***

*   <ItemLink id="stonecutting_pattern" />：与合成样板类似，但编码切石机配方。它同样通过样板供应器 + 分子装配室自动化，机制完全一致。实际上，合成、锻造、切石样板可以共用一套设施。

***

*   <ItemLink id="processing_pattern" />：自动合成灵活性的主要来源。它是最通用的类型，只表达一件事：
    “当样板供应器把这些原料推给相邻容器后，ME 系统会在未来某个时刻收到这些产物。”
    你可以用它对接几乎所有模组机器、熔炉等处理流程。因为它不关心“投料”与“收产物”之间发生了什么，你可以把中间流程做得非常复杂；只要最终交回样板声明的结果，系统就认。
    甚至原料与产物逻辑上完全无关也可以：比如你可以定义“1 樱花木板 = 1 下界之星”，然后让一个凋灵农场在收到木板后击杀凋灵来产出下界之星，系统照样能跑通。

支持多个带相同样板的 <ItemLink id="pattern_provider" /> 并行工作。此外，你也可以把样板写成“8 圆石 = 8 石头”而不是“1 = 1”；这样每次会整批投放 8 个，而不是一次只投 1 个。

## “样板”的最泛化形式

实际上，还有一种比处理样板更“泛”的“样板”：插了合成卡的 <ItemLink id="level_emitter" /> 可以配置为“通过红石信号触发合成”。这种“样板”不定义、也不关心原料；它只表达：
“当这个标准发信器发红石时，ME 系统会在未来某个时刻收到目标物品。”
这常用于启停不需要输入原料的无限农场，或触发能处理递归配方（标准自动合成无法理解）的系统，例如你有复制圆石的机器时，做出类似 “1 圆石 = 2 圆石” 的循环逻辑。

# 合成 CPU

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/assemblies/crafting_cpus.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

合成 CPU 负责管理合成请求/任务。它会在多步骤任务执行期间存放中间物料，并影响可处理任务规模，以及在一定程度上影响速度。它是多方块结构，必须搭成长方体，且至少包含 1 个合成存储器。

合成 CPU 由以下部件构成：

*   （必需）[合成存储器](../items-blocks-machines/crafting_cpu_multiblock.md)，有标准容量档位（1k、4k、16k、64k、256k）。它用于存放任务所需和中间物料；任务涉及的物料越多，就需要越大或越多的存储器。
*   （可选）<ItemLink id="crafting_accelerator" />，可提升样板供应器发出原料批次的速度。
    例如，一个样板供应器周围有 6 台分子装配室时，能同时喂给 6 台，而不是每次只用 1 台。
*   （可选）<ItemLink id="crafting_monitor" />，显示该 CPU 当前处理的任务；可通过 <ItemLink id="color_applicator" /> 染色。
*   （可选）<ItemLink id="crafting_unit" />，仅用于补齐体积，使 CPU 成为长方体。

每个合成 CPU 同时只能处理 1 个请求/任务。所以如果你想一次请求“运算处理器”和“256 个平滑石头”，就需要 2 组 CPU 多方块。

你可以把 CPU 设置为仅处理玩家请求、仅处理自动化请求（输出总线与接口），或两者都处理。

# 样板供应器

<Row>
<BlockImage id="pattern_provider" scale="4" />

<BlockImage id="pattern_provider" p:push_direction="up" scale="4" />

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/blocks/cable_pattern_provider.snbt" />
</GameScene>
</Row>

<ItemLink id="pattern_provider" /> 是自动合成系统与世界交互的核心部件。它会把[样板](../items-blocks-machines/patterns.md)中的原料推送到相邻容器；同时也可以向它插入物品，以便把物品送入网络。很多时候可以省一个频道：把机器产物用管道接回附近的样板供应器（常常就是最初投料的那台），而不是额外用 <ItemLink id="import_bus" /> 把产物拉回网络。

要注意，样板供应器是直接从合成 CPU 的[合成存储器](../items-blocks-machines/crafting_cpu_multiblock.md#crafting-storage)中取料并推送的，它自身库存里不会真正缓存这批原料，因此你不能直接从它抽出这些料。需要先让它推到其他容器（例如桶/箱），再从那个容器抽。

另一个关键点：样板供应器必须一次推完一整批原料，不能推半批。这一点可以被用于一些高级自动化技巧。

样板供应器与[子网](../ae2-mechanics/subnetworks.md)上的接口有一个特殊交互：当接口是“未修改状态”（请求槽为空）时，供应器会跳过接口，直接把料推到该子网的[存储](../ae2-mechanics/import-export-storage.md)，从而避免接口被塞满配方批次；更重要的是，只有当存储有空位时，下一批才会继续推进。

支持多个内容相同的样板供应器并行工作。

样板供应器会尽量按轮询方式把批次分配到各个面，以并行利用所有连接机器。

## 变种

样板供应器有三种变体：普通、定向、平面。它们影响“向哪些面推料、从哪些面收物、哪些面提供网络连接”。

*   普通样板供应器：向所有面推料、从所有面收物，并像大多数 AE2 机器一样向所有面提供网络连接。

*   定向样板供应器：对普通样板供应器使用 <ItemLink id="certus_quartz_wrench" /> 可切换为定向模式。
    它只向选定面推料、从所有面收物，并且在选定面不提供网络连接。这样就能向 AE2 机器投料，同时避免网络直接连通，便于构建子网。

*   平面样板供应器：属于[线缆子部件](../ae2-mechanics/cable-subparts.md)，可在同一根线缆上放置多个，布局更紧凑。
    它的行为类似定向样板供应器的“选定面”：提供样板、接收输入，并且在其正面不提供网络连接。

普通样板供应器和平面样板供应器可在合成网格中相互转换。

## 设置

样板供应器有多种模式：

*   **阻挡模式**：若机器里已经有原料，则阻止继续推送新批次。
*   **锁定合成**：可在多种红石条件下锁定供应器，或锁到“上一轮产物被插回这台供应器”为止。
*   可在 <ItemLink id="pattern_access_terminal" /> 中设置该供应器显示或隐藏。

## 优先级

点击 GUI 右上角扳手可设置优先级。当多个[样板](../items-blocks-machines/patterns.md)都能产出同一物品时，会优先使用高优先级供应器中的样板；除非网络凑不齐该高优先级样板所需原料。

# 分子装配室

<BlockImage id="molecular_assembler" scale="4" />

<ItemLink id="molecular_assembler" /> 会接收输入物品，并执行相邻 <ItemLink id="pattern_provider" /> 定义的操作，或执行直接插入其中的 <ItemLink id="crafting_pattern" />、<ItemLink id="smithing_table_pattern" />、<ItemLink id="stonecutting_pattern" />，然后把结果推送到相邻容器。

它最常见的用法是贴着 <ItemLink id="pattern_provider" /> 放置。此时样板供应器会把对应样板信息与原料发给相邻装配室；装配室又会自动把结果弹出到相邻容器（也就能回到样板供应器返还槽）。因此，样板供应器 + 分子装配室就足以自动化合成类样板。

<GameScene zoom="4" background="transparent">
<ImportStructure src="../assets/assemblies/assembler_tower.snbt" />
<IsometricCamera yaw="195" pitch="30" />
</GameScene>
