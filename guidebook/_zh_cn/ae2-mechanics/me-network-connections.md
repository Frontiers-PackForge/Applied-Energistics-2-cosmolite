---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 网络连接
  icon: fluix_glass_cable
---

# 网络连接

## “网络”是什么意思？

“网络”可以理解为一组通过可传递[频道](../ae2-mechanics/channels.md)的方块连接起来的[设备](../ae2-mechanics/devices.md)。
这些“可传频道”的方块包括[线缆](../items-blocks-machines/cables.md)、整方块机器和各类设备（<ItemLink id="charger" />、<ItemLink id="interface" />、<ItemLink id="drive" /> 等）。
严格来说，单根线缆本身也算一个网络。

## 设备位置简述

对于承担特定网络功能的[设备](../ae2-mechanics/devices.md)（例如向[网络存储](../ae2-mechanics/import-export-storage.md)推拉物品的 <ItemLink id="interface" />、读取存储状态的 <ItemLink id="level_emitter" />、作为网络存储本体的 <ItemLink id="drive" />），设备放在物理空间的哪个位置通常并不重要。

再强调一次：**设备的物理位置不重要**。真正重要的是它是否接入网络，以及接入的是哪个网络。

## 网络连接

用 <ItemLink id="network_tool" /> 可以很直观地检查网络连接情况。它会显示该网络内的全部组件；如果你看到了不该连接的东西，或缺少了本该连接的东西，就说明连接有问题。

例如，下面是 2 个独立网络：

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/2_networks_1.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="1 2 2">
        网络 1
  </BoxAnnotation>

<BoxAnnotation color="#915dcd" min="2 0 0" max="3 2 2">
        网络 2
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

下面这个也同样是 2 个独立网络，因为 <ItemLink id="quartz_fiber" /> 只共享[能量](../ae2-mechanics/energy.md)，不会建立网络连接。

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/2_networks_2.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="1 2 2">
        网络 1
  </BoxAnnotation>

  <BoxAnnotation color="#915dcd" min="1.3 0 0" max="3 2 2">
        网络 2
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

而下面这个其实只有 1 个网络，不是两个。因为[量子桥](../items-blocks-machines/quantum_bridge.md)的行为类似无线[致密线缆](../items-blocks-machines/cables.md#dense-cable)，两端属于同一网络。

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/assemblies/actually_1_network.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="7 3 3">
        整体为同一个网络
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

下面这个也同样只有 1 个网络。线缆颜色本身不决定网络归属；它只影响“不同颜色线缆彼此不连接”这一点。所有颜色都能连接福鲁伊克斯（未上色）线缆。

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/actually_1_network_2.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="4 2 2">
        整体为同一个网络
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 不太直观的连接

这张图里其实也只有 1 个网络。因为 <ItemLink id="pattern_provider" /> 作为整方块设备，会像线缆一样传递网络连接；<ItemLink id="inscriber" /> 也有类似行为，所以连接会“穿过”样板供应器和压印器继续传递。

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/pattern_provider_network_connection_1.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="4 2 2">
        整体为同一个网络
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

如果你想避免这种连接（这在许多涉及[子网](../ae2-mechanics/subnetworks.md)的自动合成布局里很有用），可以用 <ItemLink id="certus_quartz_wrench" /> 右击样板供应器把它切换为定向模式。这样它会在一个方向上不再传递频道。

<Row gap="40">
<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/pattern_provider_network_connection_2.snbt" />

  <BoxAnnotation color="#915dcd" min="0 0 0" max="2 2 2">
        网络 1
  </BoxAnnotation>

  <BoxAnnotation color="#915dcd" min="2 0 0" max="4 2 2">
        网络 2
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/pattern_provider_directional_connection.snbt" />

  <BoxAnnotation color="#ee3333" min="1 .3 .3" max="1.3 .7 .7">
        注意此处线缆不再连接
  </BoxAnnotation>

  <IsometricCamera yaw="255" pitch="30" />
</GameScene>
</Row>

其他不会提供“沿设备方向继续连接”能力的部件，多数是[线缆子部件](../ae2-mechanics/cable-subparts.md)类[设备](../ae2-mechanics/devices.md)，例如 <ItemLink id="import_bus" />、<ItemLink id="storage_bus" /> 和 <ItemLink id="cable_interface" />。

<GameScene zoom="6" background="transparent">
  <ImportStructure src="../assets/assemblies/subpart_no_connection.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>
