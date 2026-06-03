---
navigation:
  parent: example-setups/example-setups-index.md
  title: 简单赛特斯石英农场
  icon: certus_quartz_crystal
  position: 110
---

# 简单赛特斯石英农场

正如[赛特斯石英的生长](../ae2-mechanics/certus-growth.md)所提，<ItemLink id="certus_quartz_crystal" />的自动化采集需要<ItemLink id="annihilation_plane" />和<ItemLink id="storage_bus" />。<ItemLink id="growth_accelerator" />可用于大幅加速赛特斯石英芽的生长，之后由破坏面板破坏长成的<ItemLink id="quartz_cluster" />。借助一项可资利用的性质即可区分芽与簇：未长成的赛特斯石英芽会掉落<ItemLink id="certus_quartz_dust" />，而不是像某些芽那样什么都不掉。

此农场可在<ItemLink id="flawless_budding_quartz" />上全自动运行；若使用有瑕、开裂、**破损**的赛特斯石英母岩，则需手动更换母岩。也可按[半自动赛特斯石英农场](semiauto-certus-farm.md)与[进阶赛特斯石英农场](advanced-certus-farm.md)所述改为自动更换。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/simple_certus_farm.snbt" />

  <BoxAnnotation color="#dddddd" min="3.7 1 1" max="4 2 2">
        （1）破坏面板：无 GUI 可配置，但可附有时运。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="3 1 1" max="3.3 2 2">
        （2）存储总线#1：过滤赛特斯石英水晶。
        <ItemImage id="certus_quartz_crystal" scale="2" />
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="3 1 .7" max="2 2 1">
        （3）存储总线#2：过滤赛特斯石英水晶。优先级高于主网络存储。
        <ItemImage id="certus_quartz_crystal" scale="2" />
  </BoxAnnotation>

<DiamondAnnotation pos="1 0.5 0.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* 第一个<ItemLink id="annihilation_plane" />（1）无 GUI、无法配置，但可附有时运。
* 第一个<ItemLink id="storage_bus" />（2）设置为过滤<ItemLink id="certus_quartz_crystal" />。
* 第二个<ItemLink id="storage_bus" />（3）设置为过滤<ItemLink id="certus_quartz_crystal" />，且[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)高于主网络存储。

## 工作原理

1. <ItemLink id="annihilation_plane" />尝试破坏其前方的方块，但由于子网中存储位置仅有过滤<ItemLink id="certus_quartz_crystal" />的<ItemLink id="storage_bus" />，其只会破坏<ItemLink id="quartz_cluster" />。
2. 第一个<ItemLink id="storage_bus" />将赛特斯石英水晶存入木桶。
3. 第二个<ItemLink id="storage_bus" />使主网络能够访问木桶中的全部赛特斯石英水晶；其[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)设为高于主存储，使赛特斯石英水晶优先回到木桶而非主网络存储。
