---
navigation:
  title: 提示与技巧
  position: 20
---

# 提示与技巧

一堆零散的小建议。

* 卸掉 OptiFine。
* 对带有缩放与注释显隐按钮的交互场景，可旋转、缩放镜头，便于查看细节。
* 保持网络呈树状，避免环路。
* 全方块形态的[设备](ae2-mechanics/devices.md)应以不超过 8 台为一组摆放，除非你已透彻理解[频道](ae2-mechanics/channels.md)在网络中的寻路方式。
* 为所有[样板](items-blocks-machines/patterns.md)选定一种木材并坚持用到底。是的，在样板中启用替代材料有时可行，但在各处使用同一种木材会大大减少麻烦。
* 在<ItemLink id="pattern_access_terminal" />中把[样板](items-blocks-machines/patterns.md)纵向排列，或把样板分散到多台[样板供应器](items-blocks-machines/pattern_provider.md)上，以便配方可以并行执行。
* 添加一块[能源元件](items-blocks-machines/energy_cells.md)，以便网络能够应对能量尖峰。
* 你可以在<ItemLink id="condenser" />里用水。
* 保持网络干净的最佳做法是：不要把随机掉落的剑、盔甲等战利品放进网络。每种附魔与耐久的组合都会成为另一种[类型](ae2-mechanics/bytes-and-types.md)。
* 当[处理样板](items-blocks-machines/patterns.md)的产物需要回到网络时，必须发生一次「物品进入系统」事件——例如通过<ItemLink id="import_bus" />、<ItemLink id="interface" />，或<ItemLink id="pattern_provider" />的返还槽；不能只是把产物用管道打进箱子，再在箱子上接个<ItemLink id="storage_bus" />就算完事。
* <ItemLink id="pattern_provider" />只会推送**完整一批**配方材料，且只从**单个侧面**送出。这有助于确保机器不会收到半批材料；但有时你会希望配料去往多个位置。可以借助<ItemLink id="interface" />实现：既可搭成[「管道」子网](example-setups/pipe-subnet.md)，也可利用其能同时暂存多种物品栈、流体、化学品等的特性，把它当作小型中转箱/罐。
