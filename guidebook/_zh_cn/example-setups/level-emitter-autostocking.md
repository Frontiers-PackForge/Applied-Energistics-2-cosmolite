---
navigation:
  parent: example-setups/example-setups-index.md
  title: 基于标准发信器的自动维持物品量
  icon: level_emitter
---

# 基于标准发信器的自动维持物品量

你可能会问：「怎样在库存中维持一定数量的某种物品，并在需要时自动合成更多？」

一种做法是用<ItemLink id="export_bus" />、<ItemLink id="level_emitter" />与<ItemLink id="crafting_card" />，自动向网络的[自动合成](../ae2-mechanics/autocrafting.md)请求新物品。该方案用于维持**大量单一物品**。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/level_emitter_autostocking.snbt" />

  <BoxAnnotation color="#dddddd" min="1 1 0" max="2 1.3 1">
        （1）输出总线：过滤目标物品，装有红石卡与合成卡。红石模式为「有信号时激活」，合成行为为「不使用已存储物品」。
        <Row><ItemImage id="redstone_card" scale="2" /> <ItemImage id="crafting_card" scale="2" /></Row>
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="0.7 1 0" max="1 2 1">
        （2）标准发信器：配置目标物品与数量，模式为「存量低于阈值时输出红石信号」。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="1 0 0" max="2 1 1">
        （3）接口：默认配置。
  </BoxAnnotation>

<DiamondAnnotation pos="4 0.5 0.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="export_bus" />（1）过滤目标物品，装有<ItemLink id="redstone_card" />与<ItemLink id="crafting_card" />；「红石模式」为「有信号时激活」，「合成行为」为「不使用已存储物品」。
* <ItemLink id="level_emitter" />（2）配置目标物品与数量，并设为「存量低于阈值时输出红石信号」。
* <ItemLink id="interface" />（3）为默认配置。

## 工作原理

1. 若[网络存储](../ae2-mechanics/import-export-storage.md)中目标物品的数量低于<ItemLink id="level_emitter" />所设数量，发信器会输出红石信号。
2. 在收到红石信号时（且由于<ItemLink id="crafting_card" />及「不使用已存储物品」的设置），<ItemLink id="export_bus" />会向网络发起[自动合成](../ae2-mechanics/autocrafting.md)请求以补足目标物品，然后将其输出。
3. 当有物品被推入其中（且未配置为在内部库存中存放任何物品）时，<ItemLink id="interface" />会把该物品推入网络存储。
