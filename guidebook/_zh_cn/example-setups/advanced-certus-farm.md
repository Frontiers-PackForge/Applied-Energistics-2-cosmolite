---
navigation:
  parent: example-setups/example-setups-index.md
  title: 进阶赛特斯石英农场
  icon: certus_quartz_crystal
  position: 120
---

# 进阶赛特斯石英农场

本设计大体就是[半自动赛特斯石英农场](semiauto-certus-farm.md)，只是已完全接入你的 ME 系统。

不必再大量囤积母岩、隔一段时间就手动刷新，本设施借助[充能器自动化](charger-automation.md)与[投水自动化](throw-in-water-automation.md)自动完成这些步骤。

**本建筑前后遮挡较多，请旋转视角从各方向查看。**

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/advanced_certus_farm.snbt" />

  <BoxAnnotation color="#ddaaaa" min="3.7 2 1" max="4 3 2">
        （1）破坏面板 #1：无 GUI、无法配置，可附时运。
  </BoxAnnotation>

  <BoxAnnotation color="#ddaaaa" min="2 2 1.7" max="3 3 2">
        （2）存储总线 #1：过滤赛特斯石英水晶。
        <ItemImage id="certus_quartz_crystal" scale="2" />
  </BoxAnnotation>

  <DiamondAnnotation pos="3 2.5 1.5" color="#ff0000">
    石英簇破坏子网
  </DiamondAnnotation>

  <BoxAnnotation color="#aaddaa" min="3.7 1 1" max="4 2 2">
        （3）破坏面板 #2：无 GUI、无法配置，附有精准采集。
  </BoxAnnotation>

  <BoxAnnotation color="#aaddaa" min="2 1 1.7" max="3 2 2">
        （4）存储总线 #2：过滤赛特斯石英块。
        <BlockImage id="quartz_block" scale="2" />
  </BoxAnnotation>

  <DiamondAnnotation pos="3 1.5 1.5" color="#00ff00">
    赛特斯石英块破坏子网
  </DiamondAnnotation>

  <BoxAnnotation color="#ffddaa" min="4 0.7 1" max="5 1 2">
        （5）成型面板：默认配置。
  </BoxAnnotation>

  <BoxAnnotation color="#ffddaa" min="2 0.7 2" max="3 1 3">
        （6）输入总线：过滤有瑕赛特斯石英母岩。
        <BlockImage id="flawed_budding_quartz" scale="2" />
  </BoxAnnotation>

  <DiamondAnnotation pos="3 0.5 1.5" color="#ddcc00">
    母岩放置子网
  </DiamondAnnotation>

  <BoxAnnotation color="#aaaadd" min="1.7 2 2" max="2 3 3">
        （7）存储总线 #3：过滤赛特斯石英水晶，优先级高于你的主存储。
        <ItemImage id="certus_quartz_crystal" scale="2" />
  </BoxAnnotation>

  <BoxAnnotation color="#aaaadd" min="2 1 2" max="3 2 3">
        （8）接口：在自身缓存 1 个有瑕赛特斯石英母岩，装有合成卡。
        <Row><BlockImage id="flawed_budding_quartz" scale="2" /> <ItemImage id="crafting_card" scale="2" /></Row>
  </BoxAnnotation>

<DiamondAnnotation pos="1.5 0.5 0" color="#00ff00">
        至主网络、充能器自动化与投水自动化
        <Row>
        <GameScene zoom="3" background="transparent">
          <ImportStructure src="../assets/assemblies/charger_automation.snbt" />
          <IsometricCamera yaw="195" pitch="30" />
        </GameScene>
        <GameScene zoom="3" background="transparent">
          <ImportStructure src="../assets/assemblies/throw_in_water.snbt" />
          <IsometricCamera yaw="195" pitch="30" />
        </GameScene>
        </Row>
    </DiamondAnnotation>

  <IsometricCamera yaw="165" pitch="5" />
</GameScene>

## 配置

### 石英簇破坏器

* 第一个<ItemLink id="annihilation_plane" />（1）无 GUI、无法配置，可附时运。
* 第一个<ItemLink id="storage_bus" />（2）过滤<ItemLink id="certus_quartz_crystal" />。

### 赛特斯石英块破坏器

* 第二个<ItemLink id="annihilation_plane" />（3）无 GUI、无法配置，必须附有精准采集。
* 第二个<ItemLink id="storage_bus" />（4）过滤<ItemLink id="quartz_block" />。

### 母岩放置器

* <ItemLink id="formation_plane" />（5）为默认配置。
* <ItemLink id="import_bus" />（6）过滤<ItemLink id="flawed_budding_quartz" />。

### 主网络侧

* 第三个<ItemLink id="storage_bus" />（7）过滤<ItemLink id="certus_quartz_crystal" />，且[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)高于你的主存储。
* <ItemLink id="interface" />（8）在自身缓存 1 个<ItemLink id="flawed_budding_quartz" />，并装有<ItemLink id="crafting_card" />。

## 工作原理

### 石英簇破坏器

石英簇破坏子网与[简单赛特斯石英农场](simple-certus-farm.md)中的子网工作原理非常相似。

1. <ItemLink id="annihilation_plane" />尝试破坏前方方块，但子网上唯一的存储是过滤为<ItemLink id="certus_quartz_crystal" />的<ItemLink id="storage_bus" />，因此它只会破坏<ItemLink id="quartz_cluster" />。
2. <ItemLink id="storage_bus" />把赛特斯石英水晶存入木桶。

### 赛特斯石英块破坏器

赛特斯石英块破坏子网用于在枯竭母岩变成普通<ItemLink id="quartz_block" />时将其破坏，机制与石英簇破坏器类似。

1. <ItemLink id="annihilation_plane" />尝试破坏前方方块，但子网上唯一的存储是过滤为<ItemLink id="quartz_block" />的<ItemLink id="storage_bus" />，因此它只会破坏<ItemLink id="quartz_block" />。面板须带精准采集，以免在破坏母岩本身时触发降级，进而过早打碎尚未枯竭的母岩。
2. <ItemLink id="storage_bus" />把赛特斯石英块存入<ItemLink id="interface" />，供[投水自动化](throw-in-water-automation.md)用来制造新的<ItemLink id="flawed_budding_quartz" />。

### 母岩放置器

母岩放置子网在破坏子网打碎旧的枯竭母岩时放置新的<ItemLink id="flawed_budding_quartz" />。

1. <ItemLink id="import_bus" />从<ItemLink id="interface" />取出母岩并导入[网络存储](../ae2-mechanics/import-export-storage.md)。
2. 子网上唯一的存储是<ItemLink id="formation_plane" />，由它放置母岩。

### 主网络侧

* <ItemLink id="storage_bus" />使主网络（以及[充能器自动化](charger-automation.md)）能访问木桶中的全部赛特斯石英水晶；其[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)设为较高，使赛特斯石英水晶优先回到木桶而非主网络存储。
* <ItemLink id="interface" />让母岩放置子网能拿到 1 个<ItemLink id="flawed_budding_quartz" />，也让赛特斯石英块破坏子网能把枯竭方块送回主网络；<ItemLink id="crafting_card" />使接口可向主网络的[自动合成](../ae2-mechanics/autocrafting.md)请求新的母岩。
