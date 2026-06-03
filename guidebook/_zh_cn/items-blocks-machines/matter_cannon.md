---
navigation:
  parent: items-blocks-machines/items-blocks-machines-index.md
  title: 物质炮
  icon: matter_cannon
  position: 410
categories:
- tools
item_ids:
- ae2:matter_cannon
---

# 物质炮

<ItemImage id="matter_cannon" scale="4" />

物质炮是可将小型物品射出的便携式轨道炮，弹药包括 <ItemLink id="matter_ball" /> 和金属粒。伤害由弹药决定：如金粒（10 点伤害）这类较“重”的弹药，比物质球（2 点伤害）这类较轻弹药造成更高伤害。每次发射基础消耗 1600 AE。

配置“matterCannonBlockDamage”为true时，物质炮会根据方块硬度和弹药伤害破坏方块。

可在<ItemLink id="charger" />中为其充能。

物质炮和[存储元件](storage_cells.md)表现类似，可将其放入<ItemLink id="chest" />的元件槽以补充弹匣。

## 升级

物质炮支持如下[升级](upgrade_cards.md)，需用<ItemLink id="cell_workbench" />装入：

*   <ItemLink id="fuzzy_card" />使得物质炮可按耐久度或忽略NBT分区
*   <ItemLink id="inverter_card" />将白名单变为黑名单
*   <ItemLink id="speed_card" />增加每次发射的能量消耗，射出的子弹能量更大
*   <ItemLink id="void_card" />会在物质炮已满时销毁输入的物品，设置分区的时候要小心！
*   <ItemLink id="energy_card" />可增加其能量容量

## 配方

<RecipeFor id="matter_cannon" />
