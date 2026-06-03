---
navigation:
  parent: items-blocks-machines/items-blocks-machines-index.md
  title: ME存储总线
  icon: storage_bus
  position: 220
categories:
- devices
item_ids:
- ae2:storage_bus
---

# 存储总线

<GameScene zoom="8" background="transparent">
<ImportStructure src="../assets/blocks/storage_bus.snbt" />
</GameScene>

想保留你的“箱子怪”仓库，而不是换成更规整的方案？这正是存储总线的用武之地。

存储总线会把它所接触到的容器视为[网络存储](../ae2-mechanics/import-export-storage.md)。
具体来说，它让网络能够“看见”该容器内容，并能向该容器推送/从该容器拉取，以满足各类[设备](../ae2-mechanics/devices.md)对网络存储的读写需求。

基于 AE2 通过[设备](../ae2-mechanics/devices.md)功能交互产生涌现玩法的设计思路，存储总线并不一定只拿来“做存储”。你可以借助[子网](../ae2-mechanics/subnetworks.md)，让一个（或多个）存储总线成为该网络*唯一*的存储位置，从而把它当作物品传输的来源或目的地。（见[“管道”子网](../example-setups/pipe-subnet.md)）

存储总线是[线缆子部件](../ae2-mechanics/cable-subparts.md)。

## 过滤

默认情况下，存储总线会存储所有内容。放入其过滤槽的物品会形成白名单，仅允许这些指定物品被存储。

即使你手里没有该物品，也可以从 JEI/REI 直接拖拽物品或流体到过滤槽中。

用流体容器（如桶或流体储罐）右击可将该流体设为过滤目标，而不是容器物品本身。

## 优先级

可点击 GUI 右上角扳手设置优先级。
当物品进入网络时，会先尝试进入最高优先级存储。若两个存储优先级相同，且其中一个已存有该物品，则会优先选择已存有该物品的存储。若同一优先级组中存在已过滤存储，它会被视为“已存有该物品”。当物品从存储中取出时，会优先从最低优先级存储取出。  
这意味着：在不断存入与取出的过程中，高优先级存储会先被填满，低优先级存储会先被搬空。

## 设置

*   总线可按相邻容器当前内容进行分区（过滤）
*   可设置网络是否能看见相邻容器中“总线无法抽出”的物品
    （例如，存储总线无法从 <ItemLink id="inscriber" /> 的中间输入槽中抽出物品）
*   可设置为“存入与取出都过滤”或“仅存入过滤”
*   可设置为“双向”“仅存入”或“仅取出”

## 升级

存储总线支持以下[升级](upgrade_cards.md)：

*   <ItemLink id="capacity_card" /> 增加过滤槽数量
*   <ItemLink id="fuzzy_card" /> 允许按耐久度过滤和/或忽略物品 NBT
*   <ItemLink id="inverter_card" /> 将过滤从白名单切换为黑名单
*   <ItemLink id="void_card" /> 在所连容器已满时清空继续输入的物品，可避免农场堵塞。请务必先做好分区！

## 配方

<RecipeFor id="storage_bus" />
