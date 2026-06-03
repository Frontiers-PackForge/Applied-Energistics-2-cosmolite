---
navigation:
  parent: example-setups/example-setups-index.md
  title: 物品/流体「管道」子网
  icon: storage_bus
---

# 物品/流体「管道」子网

以下是用 AE2 [设备](../ae2-mechanics/devices.md)模拟物品与/或流体管道的简单做法，凡是用得上物品或流体管道的场合都适用，其中也包括把合成产物送回<ItemLink id="pattern_provider" />。

通常有两种方法达成这种效果：

## 输入总线 → 存储总线

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/import_storage_pipe.snbt" />

<BoxAnnotation color="#dddddd" min="3.7 0 0" max="4 1 1">
        （1）输入总线：可过滤。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 0 0" max="1.3 1 1">
        （2）存储总线：可过滤。此总线（以及其他要设为传输终点的存储总线）
        必须为网络中唯一的存储位置。
  </BoxAnnotation>

<DiamondAnnotation pos="4.5 0.5 0.5" color="#00ff00">
        起点
    </DiamondAnnotation>

<DiamondAnnotation pos="0.5 0.5 0.5" color="#00ff00">
        终点
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

位于起点容器上的<ItemLink id="import_bus" />（1）会导入物品或流体，并尝试将其存入[网络存储](../ae2-mechanics/import-export-storage.md)。由于网络上唯一的存储是<ItemLink id="storage_bus" />（2）（因此这必须是子网，而不能搭在你的主网络上），物品或流体会进入终点容器，从而完成传输。能量通过<ItemLink id="quartz_fiber" />接入。输入总线与存储总线均可设置过滤；若未设置过滤，则会传输其能访问的全部物品与流体。此结构也可使用多条输入总线与多条存储总线。

## 存储总线 → 输出总线

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/storage_export_pipe.snbt" />

<BoxAnnotation color="#dddddd" min="3.7 0 0" max="4 1 1">
        （1）存储总线：可过滤。
        此总线（以及其他要设为传输起点的存储总线）必须为网络中唯一的存储位置。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 0 0" max="1.3 1 1">
        （2）输出总线：必须过滤。
  </BoxAnnotation>

<DiamondAnnotation pos="4.5 0.5 0.5" color="#00ff00">
        起点
    </DiamondAnnotation>

<DiamondAnnotation pos="0.5 0.5 0.5" color="#00ff00">
        终点
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

位于终点容器上的<ItemLink id="export_bus" />会尝试按其过滤从[网络存储](../ae2-mechanics/import-export-storage.md)中抽取物品或流体。由于网络上唯一的存储是<ItemLink id="storage_bus" />（因此这必须是子网，而不能搭在你的主网络上），物品或流体会从起点容器被抽出，从而完成传输。能量通过<ItemLink id="quartz_fiber" />接入。因为输出总线必须设过滤才能工作，所以只有为输出总线配置过滤后，这一结构才会运行。此结构也可使用多条存储总线与多条输出总线。

## 无法运作的设计（输入总线 → 输出总线）

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/import_export_pipe.snbt" />

<BoxAnnotation color="#dd3333" min="3.7 0 0" max="4 1 1">
        （1）输入总线：由于网络中没有存储空间，输入总线的输入目标不存在。
  </BoxAnnotation>

<BoxAnnotation color="#dd3333" min="1 0 0" max="1.3 1 1">
        （2）输出总线：由于网络中没有存储空间，输出总线的输出来源不存在。
  </BoxAnnotation>

<DiamondAnnotation pos="4.5 0.5 0.5" color="#ff0000">
        起点
    </DiamondAnnotation>

<DiamondAnnotation pos="0.5 0.5 0.5" color="#ff0000">
        终点
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

只由输入总线和输出总线组成的设施不会运作。输入总线会尝试从起点容器中抽取物品和流体并存入网络存储。输出总线会尝试从网络存储中抽取物品和流体并输出至终点容器。但是此网络**没有存储位置**，输入总线无法输入，输出总线也无法输出，设施不会工作。

## 在同一面上输入输出

假如有机器能在单个面上同时接收输入和弹出输出（比如<ItemLink id="charger" />），则可综合 2 种管道子网以在同一面上输入材料并抽出产物：

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/import_storage_export_pipe.snbt" />

<BoxAnnotation color="#dddddd" min="4 1 1" max="5 1.3 2">
        （1）输入总线：可过滤。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="2 1 1" max="3 1.3 2">
        （2）存储总线：可过滤。此存储总线（以及其他用于输入输出的存储总线）必须为网络中唯一的存储位置。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="2 0 1" max="3 1 2">
        （3）需要输入输出的设备：此处为充能器。
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 1 1" max="1 1.3 2">
        （4）输出总线：必须过滤。
  </BoxAnnotation>

<DiamondAnnotation pos="4.5 0.5 1.5" color="#00ff00">
        起点
    </DiamondAnnotation>

<DiamondAnnotation pos="0.5 0.5 1.5" color="#00ff00">
        终点
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 接口

事实上，除输入总线与输出总线外，还有其他[设备](../ae2-mechanics/devices.md)也能把物品推入或拉出[网络存储](../ae2-mechanics/import-export-storage.md)；这里相关的就是<ItemLink id="interface" />。若有物品被插入接口，而接口并未将该物品设为在自身中库存，则接口会把该物品推入网络存储——这一点可利用，效果类似「输入总线 → 存储总线」管道。若把接口设为在自身中库存某物品，则会从网络存储中拉出该物品，效果类似「存储总线 → 输出总线」管道。接口也可设为只库存部分物品而不库存其余物品，从而借助存储总线远程推拉——若你有理由要这么做的话。

<GameScene zoom="6" background="transparent">
<ImportStructure src="../assets/assemblies/interface_pipes.snbt" />

<BoxAnnotation color="#dddddd" min="3.7 0 0" max="4 1 1">
        接口
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 0 0" max="1.3 1 1">
        存储总线
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="3.7 0 2" max="4 1 3">
        存储总线
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 1 2" max="1 1.3 3">
        接口
  </BoxAnnotation>

<IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 一对多与多对一（以及多对多）

当然，<ItemLink id="import_bus" />、<ItemLink id="export_bus" />，以及<ItemLink id="storage_bus" />并非只能使用一个。

<GameScene zoom="3" background="transparent">
<ImportStructure src="../assets/assemblies/many_to_many_pipe.snbt" />

<IsometricCamera yaw="185" pitch="30" />
</GameScene>

## 向多处提供材料

综合上述设计，即可得出从单个<ItemLink id="pattern_provider" />面向多处运输材料的方式，适用于机器阵列，或是单台机器的多个面。

不采用「输入 → 存储」管道以及「存储 → 输出」管道，因为<ItemLink id="pattern_provider" />无法存储材料，而是会将材料*输出*至相邻容器。因此我们需要能输入物品的某种相邻容器。

而符合条件的设备……就是<ItemLink id="interface" />！并且供应器需为方向型或面板型，或接口为面板型，或两个条件均满足，以避免两者形成网络连接。

<GameScene zoom="6" background="transparent">
<ImportStructure src="../assets/assemblies/provider_interface_storage.snbt" />

<BoxAnnotation color="#dddddd" min="2.7 0 1" max="3 1 2">
        接口（必须为面板型，不能为方块型）
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="1 0 0" max="1.3 1 4">
        存储总线
  </BoxAnnotation>

<BoxAnnotation color="#dddddd" min="0 0 0" max="1 1 4">
        样板供应目的地（多台机器，或单台机器的多个面）
  </BoxAnnotation>

<IsometricCamera yaw="185" pitch="30" />
</GameScene>