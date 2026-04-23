---
navigation:
  parent: items-blocks-machines/items-blocks-machines-index.md
  title: 处理器
  icon: logic_processor
  position: 010
categories:
- misc ingredients blocks
item_ids:
- ae2:logic_processor
- ae2:calculation_processor
- ae2:engineering_processor
- ae2:printed_silicon
- ae2:printed_logic_processor
- ae2:printed_calculation_processor
- ae2:printed_engineering_processor
- ae2:silicon
---

# 处理器

<Row>
  <ItemImage id="logic_processor" scale="4" />

  <ItemImage id="calculation_processor" scale="4" />

  <ItemImage id="engineering_processor" scale="4" />
</Row>

处理器是 AE2 [设备](../ae2-mechanics/devices.md)和机器的基础合成材料之一，也是你会遇到的第一个大型自动化挑战。共有三种处理器，分别对应金、<ItemLink id="certus_quartz_crystal" /> 和钻石路线。它们可在 <ItemLink id="inscriber" /> 中借助[压印模板](presses.md)通过多步流程制造（通常由多台压印器与带过滤的物流系统实现）。

## 生产步骤

<Column gap="5">

  1.  收集/制造所需材料：硅、红石、金、<ItemLink id="certus_quartz_crystal" />、钻石。

  <RecipeFor id="silicon" />

  <br />

  2.  压印出中间产物电路板

  <Row>
    <RecipeFor id="printed_silicon" />

    <RecipeFor id="printed_logic_processor" />
  </Row>

  <Row>
    <RecipeFor id="printed_calculation_processor" />

    <RecipeFor id="printed_engineering_processor" />
  </Row>

  <br />

  3.  最终组装步骤

  <Row>
    <RecipeFor id="logic_processor" />

    <RecipeFor id="calculation_processor" />
  </Row>

  <RecipeFor id="engineering_processor" />
</Column>
