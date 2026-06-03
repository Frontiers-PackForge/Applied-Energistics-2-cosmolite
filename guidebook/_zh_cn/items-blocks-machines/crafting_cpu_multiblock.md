---
navigation:
  parent: items-blocks-machines/items-blocks-machines-index.md
  title: 合成CPU多方块结构（合成存储器、并行处理单元、合成监控器、合成单元）
  icon: 1k_crafting_storage
  position: 210
categories:
- devices
item_ids:
- ae2:1k_crafting_storage
- ae2:4k_crafting_storage
- ae2:16k_crafting_storage
- ae2:64k_crafting_storage
- ae2:256k_crafting_storage
- ae2:crafting_accelerator
- ae2:crafting_monitor
- ae2:crafting_unit
---

# 合成CPU

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/assemblies/crafting_cpus.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

<Row>
  <BlockImage id="1k_crafting_storage" scale="4" />

  <BlockImage id="crafting_accelerator" scale="4" />

  <BlockImage id="crafting_monitor" scale="4" />

  <BlockImage id="crafting_unit" scale="4" />
</Row>

合成 CPU 负责管理合成请求与任务。它会在执行多步骤任务时存放中间产物，并影响任务规模上限；在一定程度上也会影响完成速度。详细说明见[自动合成](../ae2-mechanics/autocrafting.md)。

右击合成CPU会打开合成状态UI，可在此检查CPU正在处理的合成任务进度。

## 设置

*   合成 CPU 可设置为仅接受玩家请求、仅接受自动化请求（如装有 <ItemLink id="crafting_card" /> 的 <ItemLink id="export_bus" />），或两者都接受。

## 建造

合成CPU是多方块结构，且必须为没有空隙的实心长方形棱柱。它们由如下若干组件组成：

每个CPU必须包含至少1个合成存储器（可用的最小CPU实际上就是单个1k合成存储器）。

# 合成单元

<BlockImage id="crafting_unit" scale="4" />

（可选）合成单元仅用于填充CPU内空隙，以保证CPU的形状是实心长方形棱柱。没有其他组件时可用此填补。它们也是其他组件的合成材料。

<RecipeFor id="crafting_unit" />

# 合成存储器

<Row>
  <BlockImage id="1k_crafting_storage" scale="4" />

  <BlockImage id="4k_crafting_storage" scale="4" />

  <BlockImage id="16k_crafting_storage" scale="4" />

  <BlockImage id="64k_crafting_storage" scale="4" />

  <BlockImage id="256k_crafting_storage" scale="4" />
</Row>

（必需）合成存储器支持所有标准元件大小（1k、4k、16k、64k、256k）。它们会将与合成相关的材料和中间材料存于自身，因此处理所需材料更多的合成任务需要更大的或更多个合成存储器。

<Column>
  <Row>
    <RecipeFor id="1k_crafting_storage" />

    <RecipeFor id="4k_crafting_storage" />

    <RecipeFor id="16k_crafting_storage" />
  </Row>

  <Row>
    <RecipeFor id="64k_crafting_storage" />

    <RecipeFor id="256k_crafting_storage" />
  </Row>
</Column>

# 并行处理单元

<BlockImage id="crafting_accelerator" scale="4" />

（可选）并行处理单元通过提升 CPU 运转速度，让系统从 <ItemLink id="pattern_provider" /> 更频繁地发送材料批次，以跟上较快机器的处理速度。例如，当被 <ItemLink id="molecular_assembler" /> 包围的样板供应器送料速度高于单台装配室处理速度时，系统会把材料批次分配到各装配室并行执行。

某些复杂配方可能包含多个可并行进行的步骤，比如制造书架时并行制造木板和书。在合成状态UI（右击CPU或右击[终端](terminals.md)的锤子图标）中，这些步骤都显示为“计划合成”。每个并行处理单元都可额外使一个上述步骤并行进行（亦即显示为“正在合成”）。不过，这点通常不那么重要，因为加入大量并行处理单元的原因通常是为提升发送速度，而非增加加工配方的并行数量。

<RecipeFor id="crafting_accelerator" />

# 合成监控器

<BlockImage id="crafting_monitor" scale="4" />

（可选）合成监控器会显示CPU当前正在处理的任务。其屏幕可用<ItemLink id="color_applicator" />染色。

<RecipeFor id="crafting_monitor" />
