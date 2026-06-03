---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 空间IO
  icon: spatial_storage_cell_2
---

# 空间IO

<GameScene zoom="6" interactive={true}>
  <ImportStructure src="../assets/assemblies/spatial_storage_1x1x1.snbt" />

  <BoxAnnotation color="#33dd33" min="1 1 1" max="2 2 2">
        会被移动的区域
  </BoxAnnotation>

  <IsometricCamera yaw="195" pitch="30" />

</GameScene>

空间IO是一种在世界中「剪切–粘贴」实体空间体积的方式。你可以用它来搬运 <ItemLink id="flawless_budding_quartz" />，在基地里做一间能随时更换内部陈设、一物多用的房间，甚至搬走末地传送门。

其原理是：把划定好的体积与**空间存储维度**里**同样大小**的一块体积**对调**——空间塔阵列里的一切会被送进该维度，而维度里原先那一块则会出现在阵列所在的位置。

这意味着，只要你有办法在维度之间往返（空间IO *可以*用来做传送，但实现起来非常复杂、也不太稳，且超出本指南范围），就可以把它当作**自定义尺寸**的压缩空间或「口袋维度」来用。

# 多方块设施

空间IO必须把各部件按特定方式摆好才能工作，并由此划定要被剪切–粘贴的体积。

所有部件必须在同一套[网络](me-network-connections.md)上才能运作，且**每个网络里只能有一套**空间IO。因此通常建议放在[子网](subnetworks.md)上。

## 空间IO端口

<BlockImage id="spatial_io_port" p:powered="true" scale="4" />

<ItemLink id="spatial_io_port" /> 负责控制空间IO的运行。它会显示这套多方块的数据，并容纳[空间元件](../items-blocks-machines/spatial_cells.md)。

界面会显示：

- 网络中已存储的[能量](energy.md)及其上限
- 执行一次操作所需的能量。该数值可能很大且会**瞬时**扣取，请准备足够的[能源元件](../items-blocks-machines/energy_cells.md)来承接尖峰。
- 空间塔阵列的效率
- 所定义区域的尺寸

要执行一次空间IO：把空间存储元件放进输入槽，再给空间IO端口一个红石脉冲。随后便会*交换*空间塔围出的体积与空间存储维度中的对侧体积。也就是说：若先把一批方块送进维度，*再在塔阵里换上另一批方块*，把元件放回输入槽并再次触发端口，则**第二批**会消失，**第一批**会重新出现。

**务必小心：划定体积内的任何实体——包括你自己——都会一起被带走。若没有出来的办法，你就会被困在空间存储维度里，待在又黑又空无一物的方盒中。**可以用来整蛊朋友！

## 空间塔

<BlockImage id="spatial_pylon" p:powered_on="true" scale="4" />

<ItemLink id="spatial_pylon" /> 是空间IO的主体，用来划定受影响的区域。

该区域由**所有空间塔外接长方体的外表面**再向**各方向内缩 1 格**得到。

规则如下：

- 外接长方体至少为 3×3×3（对应可定义 1×1×1 的体积）
- 所有空间塔都必须落在外接长方体的边界壳层之内
- 所有空间塔必须在同一网络
- 每根空间塔至少 **2 格**长

例如，若要定义 3×3×3 的体积：按规则 2，所有塔都必须位于目标体积外围那一圈 **1 格厚**的 5×5×5 外壳里；只要仍在这层壳内，彼此之间的具体排布几乎可以任意。

<GameScene zoom="4" interactive={true}>
<ImportStructure src="../assets/assemblies/spatial_storage_3x3x3_pylon_demonstration.snbt" />

<BoxAnnotation color="#33dd33" min="1 1 1" max="4 4 4">
        会被移动的区域
  </BoxAnnotation>

<BoxAnnotation color="#3333ff" min="5 5 0" max="0 0 5">
  </BoxAnnotation>

<IsometricCamera yaw="195" pitch="30" />
</GameScene>

更合理的一种摆法如下：

<GameScene zoom="4" interactive={true}>
<ImportStructure src="../assets/assemblies/better_spatial_storage_3x3x3.snbt" />

<BoxAnnotation color="#33dd33" min="1 1 1" max="4 4 4">
        会被移动的区域
  </BoxAnnotation>

<BoxAnnotation color="#3333ff" min="5 5 0" max="0 0 5">
  </BoxAnnotation>

<IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 效率

空间塔阵列的效率取决于你在「外壳」里填了多少。若用最省料的壳去包特别大的体积，效率会非常差，甚至可能吃掉**数以亿计**的 AE。

## 元件维度

[空间元件](../items-blocks-machines/spatial_cells.md)在**首次使用**后，会永久绑定一组 XYZ 尺寸（例如 3×4×2），并与空间存储维度中的某块体积一一对应。**空间元件一经使用，便无法重置、重新格式化或改尺寸。**若要换一套尺寸，请另做一枚新元件。

元件名字里写的尺寸与这组绑定尺寸不是一回事：一枚「16³」元件表示**最多**能容纳 16×16×16 的体积，实际绑定的可以是**不超过**该上限的任意合法尺寸。

注意：该体积**有方向**，不能随意旋转。2×2×3 与 3×2×2 虽然格子数相同，在系统里仍视为不同。

若元件上的 XYZ 与 IO 端口里显示的划定体积不一致，端口将**不会**执行操作。
