---
navigation:
  parent: example-setups/example-setups-index.md
  title: 投水自动化
  icon: fluix_crystal
---

# 自动化投水配方

需注意，此设施使用了<ItemLink id="pattern_provider" />，也即需与你的[自动合成](../ae2-mechanics/autocrafting.md)设施配合使用。

某些配方可能要求将物品投入水中（不过同种设施也可用于处理其他物品投入某处的要求）。可用<ItemLink id="formation_plane" />、<ItemLink id="annihilation_plane" />，以及辅助基础设施（也即 2 个经调整的[管道子网](pipe-subnet.md)）自动化这类配方。

此设施应与[充能器自动化](charger-automation.md)配合使用以生产<ItemLink id="charged_certus_quartz_crystal" />。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/throw_in_water.snbt" />

<BoxAnnotation color="#dddddd" min="2 0 1" max="3 1 2">
        （1）样板供应器：默认配置，装有相应处理样板。

        ![福鲁伊克斯样板](../assets/diagrams/fluix_pattern_small.png) ![有瑕母岩样板](../assets/diagrams/flawed_budding_pattern_small.png)
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1.7 0 1" max="2 1 2">
        （2）接口：默认配置。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 .7 1" max="2 1 2">
        （3）成型面板：设置为以物品形式掉落。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 2 1" max="2 2.3 2">
        （4）破坏面板：无 GUI 可配置。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="2 1 1" max="3 1.3 2">
        （5）存储总线：过滤样板输出。
        <Row><ItemImage id="fluix_crystal" scale="2" /><BlockImage id="flawless_budding_quartz" scale="2" /></Row>
  </BoxAnnotation>

<DiamondAnnotation pos="3.9 0.5 1.5" color="#00ff00">
        至主网络与充能器自动化
        <GameScene zoom="3" background="transparent">
          <ImportStructure src="../assets/assemblies/charger_automation.snbt" />
          <IsometricCamera yaw="195" pitch="30" />
        </GameScene>
    </DiamondAnnotation>

  <IsometricCamera yaw="180" pitch="0" />
</GameScene>

## 配置与样板

* <ItemLink id="pattern_provider" />（1）处于默认配置，装有相关<ItemLink id="processing_pattern" />。
  * 对于<ItemLink id="fluix_crystal" />，JEI/REI 的默认配方就可以了：

    ![福鲁伊克斯样板](../assets/diagrams/fluix_pattern.png)

  * 对于<ItemLink id="flawed_budding_quartz" />，直接用<ItemLink id="quartz_block" />制造更佳，否则输入输出的物品可能重叠，不利于配置过滤：

    ![有瑕母岩样板](../assets/diagrams/flawed_budding_pattern.png)

* <ItemLink id="interface" />（2）处于默认配置。
* <ItemLink id="formation_plane" />（3）设置为以物品形式掉落。
* <ItemLink id="annihilation_plane" />（4）没有 GUI 且无法配置。
* <ItemLink id="storage_bus" />（5）过滤为各样板的输出物品。

## 工作原理

1.  <ItemLink id="pattern_provider" />将原料推入位于其一侧、处于绿色子网上的<ItemLink id="interface" />。
2.  接口（默认配置为不在内部存储任何物品）尝试将其中的物品推入[网络存储](../ae2-mechanics/import-export-storage.md)。
3.  绿色子网上唯一的存储是<ItemLink id="formation_plane" />，它会把接收到的物品以掉落物形式丢入水中。
4.  橙色子网上的<ItemLink id="annihilation_plane" />会尝试捡起刚掉落的物品，但做不到，因为位于样板供应器上方的<ItemLink id="storage_bus" />（橙色子网上唯一的存储）被设为只接受可能合成配方的产物。
5.  物品在世界中完成其转化。
6.  此时<ItemLink id="storage_bus" />可以存放这些物品，<ItemLink id="annihilation_plane" />便能捡起其前方的物品。
7.  <ItemLink id="storage_bus" />将所得物品存入样板供应器，使其回到网络。
