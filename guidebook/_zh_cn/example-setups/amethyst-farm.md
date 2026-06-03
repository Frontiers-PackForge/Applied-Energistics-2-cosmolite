---
navigation:
  parent: example-setups/example-setups-index.md
  title: 紫水晶农场
  icon: minecraft:amethyst_shard
---

# 刷取紫水晶

虽然<ItemLink id="growth_accelerator" />对紫水晶也有效，但用<ItemLink id="annihilation_plane" />过滤[赛特斯石英芽](../items-blocks-machines/budding_certus.md)的常规做法对紫晶芽行不通：未长成的赛特斯芽会掉落<ItemLink id="certus_quartz_dust" />，而未长成的紫晶芽什么都不掉，网络永远能「存下」空无一物，破坏面板便会一直破坏它们。

解决办法是给破坏面板附上精准采集。这样未长成的紫晶芽*会*掉落实体（各生长阶段的芽方块），也就可以用过滤处理。

随后需由<ItemLink id="formation_plane" />再次放置<ItemLink id="minecraft:amethyst_cluster" />，再由未附精准采集的<ItemLink id="annihilation_plane" />打碎，才能得到<ItemLink id="minecraft:amethyst_shard" />。

注意晶簇有朝向：与成型面板正相对的一侧必须有一整块实体方块表面。

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/amethyst_farm.snbt" />

  <BoxAnnotation color="#dddddd" min="2.7 1 1" max="3 2 2">
        （1）破坏面板 #1：无 GUI，附有精准采集。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="2 1 1" max="2.3 2 2">
        （2）成型面板：过滤紫水晶簇。
        <ItemImage id="minecraft:amethyst_cluster" scale="2" />
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="1.3 0.7 1" max="2 1 2">
        （3）破坏面板 #2：无 GUI，可附时运。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="1 0 1" max="1.3 1 2">
        （4）存储总线 #1：过滤紫水晶碎片。
        <ItemImage id="minecraft:amethyst_shard" scale="2" />
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="0 0 .7" max="1 1 1">
        （5）存储总线 #2：过滤紫水晶碎片，[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)高于主存储。
        <ItemImage id="minecraft:amethyst_shard" scale="2" />
  </BoxAnnotation>

<DiamondAnnotation pos="0 0.5 0.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* 第一个<ItemLink id="annihilation_plane" />（1）无 GUI、无法配置，但必须附有精准采集。
* <ItemLink id="formation_plane" />（2）过滤<ItemLink id="minecraft:amethyst_cluster" />。
* 第二个<ItemLink id="annihilation_plane" />（3）无 GUI、无法配置，可附时运。
* 第一个<ItemLink id="storage_bus" />（4）过滤<ItemLink id="minecraft:amethyst_shard" />。
* 第二个<ItemLink id="storage_bus" />（5）过滤<ItemLink id="minecraft:amethyst_shard" />，且[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)高于主存储。

## 工作原理

1. 第一个<ItemLink id="annihilation_plane" />尝试破坏前方的方块，但子网上唯一的存储是过滤为紫水晶簇的<ItemLink id="formation_plane" />，因此它只会破坏<ItemLink id="minecraft:amethyst_cluster" />。这依赖精准采集：否则未长成的芽不掉落任何东西，面板仍会破坏它们。
2. <ItemLink id="formation_plane" />把簇放在其对面方块上。
3. 第二个<ItemLink id="annihilation_plane" />打碎簇，产出<ItemLink id="minecraft:amethyst_shard" />。
4. 第一个<ItemLink id="storage_bus" />把碎片存入木桶。理论上可不设过滤，因为第二个破坏面板只会遇到长成的簇。
5. 第二个<ItemLink id="storage_bus" />让主网络能访问木桶里的全部紫水晶碎片；其[优先级](../ae2-mechanics/import-export-storage.md#storage-priority)设为高于你的主存储，使紫水晶碎片优先回到木桶而非主存储。
