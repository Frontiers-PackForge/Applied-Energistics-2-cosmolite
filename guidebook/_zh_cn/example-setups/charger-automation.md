---
navigation:
  parent: example-setups/example-setups-index.md
  title: 充能器自动化
  icon: charger
---

# 充能器自动化

由于使用了<ItemLink id="pattern_provider" />，本设计意在接入你的[自动合成](../ae2-mechanics/autocrafting.md)体系。若只想单独自动化<ItemLink id="charger" />，用漏斗、箱子之类即可。

自动化<ItemLink id="charger" />相当简单：<ItemLink id="pattern_provider" />把原料推进充能器，再由[管道子网](pipe-subnet.md)或其他物品管道把产物送回供应器。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/charger_automation.snbt" />

<BoxAnnotation color="#dddddd" min="1 0 0" max="2 1 1">
        （1）样板供应器：默认配置，装有相应的处理样板，并为充能器供电。

        ![充能器样板](../assets/diagrams/charger_pattern_small.png)
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 1 0" max="1 1.3 1">
        （2）输入总线：默认配置。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 1 0" max="2 1.3 1">
        （3）存储总线：默认配置。
  </BoxAnnotation>

<DiamondAnnotation pos="4 0.5 0.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="pattern_provider" />（1）为默认配置，装有相应的<ItemLink id="processing_pattern" />；它也会给<ItemLink id="charger" />提供[能量](../ae2-mechanics/energy.md)，行为类似一根[线缆](../items-blocks-machines/cables.md)。

  ![充能器样板](../assets/diagrams/charger_pattern.png)

* <ItemLink id="import_bus" />（2）为默认配置。
* <ItemLink id="storage_bus" />（3）为默认配置。

## 工作原理

1. <ItemLink id="pattern_provider" />把原料推入<ItemLink id="charger" />。
2. 充能器照常完成充能。
3. 绿色子网上的<ItemLink id="import_bus" />从充能器取出成品，并尝试存入[网络存储](../ae2-mechanics/import-export-storage.md)。
4. 绿色子网上唯一的存储是<ItemLink id="storage_bus" />，它把成品存回样板供应器，从而回到主网络。
