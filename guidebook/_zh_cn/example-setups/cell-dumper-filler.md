---
navigation:
  parent: example-setups/example-setups-index.md
  title: 元件清空与装填
  icon: io_port
---

# 元件清空与装填

你可能会问：「怎样快速把存储元件里的东西倒进箱子、抽屉阵列或背包？反过来又怎样从这些地方装满元件？」

做法是使用<ItemLink id="io_port" />，再配合子网划分，限制物品只能写入或只能从特定位置抽出。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/cell_dumper_filler.snbt" />

<BoxAnnotation color="#dddddd" min="1 1 0" max="2 2 1">
        （1）IO 端口：用 GUI 中央的箭头按钮在「将数据传入网络」与「将数据传入存储元件」之间切换。装有 3 张加速卡。
        <ItemImage id="speed_card" scale="2" />
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 0.7 0" max="1 1 1">
        （2）存储总线：默认配置。
  </BoxAnnotation>

<BoxAnnotation color="#33dd33" min="0 1 0" max="1 2 1">
        在此放置你想用来装填或清空的容器。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="2 0.35 0.35" max="2.3 0.65 0.65">
        石英纤维：仅当能量来自另一网络时才需要。
  </BoxAnnotation>

<DiamondAnnotation pos="3 0.5 0.5" color="#00ff00">
        接向某处能量源，例如另一网络或能源接收器
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="io_port" />（1）可用 GUI 中央箭头在「将数据传入网络」与「将数据传入存储元件」之间切换；装有 3 张加速卡以达到最大速度。
* <ItemLink id="storage_bus" />（2）为默认配置。

## 工作原理

### 「传入网络」模式

1. <ItemLink id="io_port" />尝试把插入的[存储元件](../items-blocks-machines/storage_cells.md)中的内容卸入[网络存储](../ae2-mechanics/import-export-storage.md)。
2. 子网上唯一的存储是<ItemLink id="storage_bus" />，物品、流体等会写入其面前的容器。

此外，建议用 <ItemLink id="energy_cell" /> 提供足够大的[能量](../ae2-mechanics/energy.md)缓冲，使网络在每游戏刻传输大量物品时不会因功耗而耗尽能量。

### 「传入存储元件」模式

1. <ItemLink id="io_port" />尝试把[网络存储](../ae2-mechanics/import-export-storage.md)中的内容灌入插入的[存储元件](../items-blocks-machines/storage_cells.md)。
2. 子网上唯一的存储是<ItemLink id="storage_bus" />，它会从其面前的容器中抽出物品、流体等。

此外，建议用 <ItemLink id="energy_cell" /> 提供足够大的[能量](../ae2-mechanics/energy.md)缓冲，使网络在每游戏刻传输大量物品时不会因功耗而耗尽能量。
