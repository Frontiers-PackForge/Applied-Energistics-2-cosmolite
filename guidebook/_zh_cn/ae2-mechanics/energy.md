---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 能量
  icon: energy_cell
---

# 能量

网络需要能量才能运行。AE2 网络共享同一个能量池：[设备](../ae2-mechanics/devices.md)直接从池中取能，<ItemLink id="vibration_chamber" />、<ItemLink id="energy_acceptor" />（以及 <ItemLink id="controller" />）向池中供能。你可以手持 <ItemLink id="network_tool" /> 在网络任意位置右击，或在有控制器时右击控制器，查看整网能量数据。由于这种“全网统一存储与分配”机制，AE2 实际上**没有传统意义的输电速率上限**：设备可以瞬时抽取很高功率，能源接收器也可以非常快地灌能，真正的瓶颈在于网络当前可用的总储能。

## 能量接收

<Row>
  <BlockImage id="energy_acceptor" scale="4" />

  <GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/blocks/cable_energy_acceptor.snbt" />
  </GameScene>

  <BlockImage id="controller" p:state="online" scale="4" />

  <BlockImage id="vibration_chamber" p:active="true" scale="4" />
</Row>

AE2 在内部并不使用 Forge Energy（在 Forge 上）或 TechReborn Energy（在 Fabric 上），而是把外部能量转换成自己的单位 AE。这种转换是单向的。能量可由 <ItemLink id="energy_acceptor" /> 与 <ItemLink id="controller" /> 进行转换，不过控制器的面更适合用来多走[频道](../ae2-mechanics/channels.md)。<ItemLink id="vibration_chamber" /> 也可以发电，但 AE2 在设计上会希望你配合其他有更强发电能力的科技模组的能量网络使用。

以上这些都说明：在规划基地整体能源设施时，最好把一套 AE2 网络当成**一台**大型多方块机器来考虑。

Forge Energy 与 TechReborn Energy 的转换比例为：

*   2 FE = 1 AE（Forge）
*   1 E  = 2 AE（Fabric）

## 能量存储

<Row>
  <BlockImage id="energy_cell" scale="4" p:fullness="4" />

  <BlockImage id="dense_energy_cell" scale="4" p:fullness="4" />

  <BlockImage id="creative_energy_cell" scale="4" />
</Row>

网络在单个游戏刻内，无法**消耗**或**接收**超过其最大储能上限的能量。比如某网络最大仅能存 800 AE，则在“电量已满”时，[设备](../ae2-mechanics/devices.md)每刻最多只能消耗 800 AE；在“电量为零”时，<ItemLink id="energy_acceptor" /> 每刻最多也只能注入 800 AE。

这也是很多“诡异现象”的常见根源。比如你搭了一个只有能源接收器、驱动器、终端和少量设备的小网络，然后试图一次性往里塞一整背包圆石。系统在**一个**游戏刻内处理这批物品所需能量可能超过网络储能上限，于是会出现部分物品进不去、网络被瞬间抽干并**重启**的情况。

**加上若干能源元件即可解决上述问题。**

网络中每段线缆、每个机器，以及（线缆上的）每个部件，都自带 25 AE 的缓冲。

<ItemLink id="controller" /> 内部也有少量存电，共 8,000 AE。

<ItemLink id="energy_cell" /> 可存 200k AE，对多数用途一个就够，足以消纳日常使用中的功率尖峰。

<ItemLink id="dense_energy_cell" /> 可存 1.6M AE，适合想靠“电池”里存的电来撑住整套网络、或要应付大型[空间存储](spatial-io.md)等瞬时天量功耗的场景。

<ItemLink id="creative_energy_cell" /> 是用于测试的创造模式物品，提供无限能量。
