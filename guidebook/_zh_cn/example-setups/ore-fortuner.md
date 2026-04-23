---
navigation:
  parent: example-setups/example-setups-index.md
  title: 自动时运矿石机
  icon: minecraft:raw_iron
---

# 自动化时运挖掘矿石

<ItemLink id="annihilation_plane" />可附上任意镐类附魔，包括时运，因此常见用法之一是给数块破坏面板附上时运，并让<ItemLink id="formation_plane" />与<ItemLink id="annihilation_plane" />快速放置并破坏矿石。

注意<ItemLink id="import_bus" />会「逐渐加速」：设施启动时较慢，数秒后才会达到全速。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/ore_fortuner.snbt" />

  <BoxAnnotation color="#dddddd" min="2.7 0 2" max="3 1 3">
        （1）输入总线：内装若干加速卡。
        <ItemImage id="speed_card" scale="2" />
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="0 0 2" max="2 1 2.3">
        （2）成型面板：默认配置。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="0 0 0.7" max="2 1 1">
        （3）破坏面板：无 GUI，附有时运。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="2.7 0 0" max="3 1 1">
        （4）存储总线：默认配置。
  </BoxAnnotation>

<DiamondAnnotation pos="3.5 0.5 2.5" color="#00ff00">
        输入
    </DiamondAnnotation>

<DiamondAnnotation pos="3.5 0.5 0.5" color="#00ff00">
        输出
    </DiamondAnnotation>

<DiamondAnnotation pos="4 0.5 1.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="import_bus" />（1）内装有若干<ItemLink id="speed_card" />。阵列中成型面板越多，通常也需要更多加速卡，以便总线一次抽取更多物品。
* <ItemLink id="formation_plane" />（2）为默认配置。
* <ItemLink id="annihilation_plane" />（3）无 GUI、无法配置，附有时运。
* <ItemLink id="storage_bus" />（4）为默认配置。

## 工作原理

1. 绿色子网上的<ItemLink id="import_bus" />将方块从第一个木桶导入[网络存储](../ae2-mechanics/import-export-storage.md)。
2. 绿色子网上唯一的存储是<ItemLink id="formation_plane" />，由它放置方块。
3. 橙色子网上的<ItemLink id="annihilation_plane" />破坏方块，并应用时运。
4. 橙色子网上的<ItemLink id="storage_bus" />将破坏产物存入第二个木桶。
