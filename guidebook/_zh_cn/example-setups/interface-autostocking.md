---
navigation:
  parent: example-setups/example-setups-index.md
  title: 基于接口的自动维持物品量
  icon: interface
---

# 基于接口的自动维持物品量

你可能会问：「怎样在库存中同时维持多种物品各一定存量，并在需要时自动合成更多？」

一种做法是用<ItemLink id="interface" />与<ItemLink id="crafting_card" />，自动向网络的[自动合成](../ae2-mechanics/autocrafting.md)请求新物品。该方案更适合维持**种类多、每种数量少**的物品。

本演示为控制宽度做了截断；实际往往用 4 个<ItemLink id="interface" />和 4 条<ItemLink id="storage_bus" />最为理想，从而用满一根普通[线缆](../items-blocks-machines/cables.md)上的全部 8 条[频道](../ae2-mechanics/channels.md)。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/interface_autostocking.snbt" />

<BoxAnnotation color="#dddddd" min="0 0 0" max="2 1 1">
        （1）接口：在自身内部保留目标物品，装有合成卡。
        <ItemImage id="crafting_card" scale="2" />
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 1 0" max="2 1.3 1">
        （2）存储总线：「输入/输出模式」设为「仅取出」。
  </BoxAnnotation>

<DiamondAnnotation pos="4 0.5 0.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="interface" />（1）设为在自身内部保留目标物品：将目标物品点入或从 JEI 拖入上排槽位，再点击槽位上方的扳手图标设定数量；装有<ItemLink id="crafting_card" />。
* <ItemLink id="storage_bus" />（2）将「输入/输出模式」设为「仅取出」。

## 工作原理

1. 若某个<ItemLink id="interface" />无法从[网络存储](../ae2-mechanics/import-export-storage.md)取够所配置的物品（且装有<ItemLink id="crafting_card" />），它会向网络发起[自动合成](../ae2-mechanics/autocrafting.md)请求以补足该物品。
2. <ItemLink id="storage_bus" />使网络能够访问<ItemLink id="interface" />内的物品。
