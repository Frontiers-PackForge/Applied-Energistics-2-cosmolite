---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 频道
  icon: controller
---

# 频道

应用能源 2 的 [ME 网络](me-network-connections.md)需要频道，才能支持使用网络存储或其他网络服务的[设备](../ae2-mechanics/devices.md)。你可以把频道想象成连接设备的 USB 线：一台电脑的 USB 口有限，可接设备数量也有限。大多数机器、整方块设备和普通线缆最多只能传 8 个频道，可以把它们看作一束“8 芯频道线”。而[致密线缆](../items-blocks-machines/cables.md#dense-cable)最多可传 32 个频道；另外能传 32 频道的还有 <ItemLink id="me_p2p_tunnel" /> 和[量子网桥](../items-blocks-machines/quantum_bridge.md)。每有一个设备占用频道，就像从这束线里抽走一芯，后续可用频道就会减少。

<GameScene zoom="7" interactive={true}>
  <ImportStructure src="../assets/assemblies/channel_demonstration_1.snbt" />

  <LineAnnotation color="#33ff33" from="1 .4 .7" to="2.4 .4 .7" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .6 .7" to="2.4 .6 .7" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .4 .6" to="2.6 .4 .6" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .6 .6" to="2.6 .6 .6" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .6 .6" to="2.6 .6 .6" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="2.4 .6 .7" to="2.4 .6 1.5" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="2.4 .4 .7" to="2.4 .4 1.5" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="2.6 .6 .6" to="2.6 .6 1.5" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="2.6 .4 .6" to="2.6 .4 1.5" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="2.1 .6 1.5" to="2.4 .6 1.5" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="2.6 .4 1.5" to="2.9 .4 1.5" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="2.6 .6 1.5" to="2.6 .9 1.5" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="2.4 .1 1.5" to="2.4 .4 1.5" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="1 .6 .4" to="3.5 .6 .4" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .4 .4" to="3.5 .4 .4" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="3.5 .6 .4" to="3.5 .9 .4" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="3.5 .1 .4" to="3.5 .4 .4" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="1 .6 .3" to="1.5 .6 .3" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1 .4 .3" to="1.5 .4 .3" alwaysOnTop={true}/>

  <LineAnnotation color="#33ff33" from="1.5 .6 .3" to="1.5 .9 .3" alwaysOnTop={true}/>
  <LineAnnotation color="#33ff33" from="1.5 .1 .3" to="1.5 .4 .3" alwaysOnTop={true}/>

  <LineAnnotation color="#ff3333" from="3.5 .5 .5" to="5.5 .5 .5" alwaysOnTop={true}>
  线缆中全部 8 个频道都已被占用，因此驱动器未获得频道。
  </LineAnnotation>

  <LineAnnotation color="#993333" from="1 .5 .5" to="1.25 .5 .5" alwaysOnTop={true}/>
  <LineAnnotation color="#993333" from="1.5 .5 .5" to="1.75 .5 .5" alwaysOnTop={true}/>
  <LineAnnotation color="#993333" from="2 .5 .5" to="2.25 .5 .5" alwaysOnTop={true}/>
  <LineAnnotation color="#993333" from="2.5 .5 .5" to="2.75 .5 .5" alwaysOnTop={true}/>
  <LineAnnotation color="#993333" from="3 .5 .5" to="3.25 .5 .5" alwaysOnTop={true}/>

  <DiamondAnnotation pos="3.6 0.5 0.5" color="#ff0000">
        线缆中全部 8 个频道都已被占用，因此驱动器未获得频道。
    </DiamondAnnotation>

  <IsometricCamera yaw="15" pitch="30" />
</GameScene>

观察频道占用和寻路的最佳方式之一是使用[智能线缆](../items-blocks-machines/cables.md)，它会直接显示相关信息。

频道每经过一个节点会额外消耗 1/128 AE/t。也就是说，对于一个有 8 台设备且节点数超过 96 的网络，加入 <ItemLink id="controller" /> 之后，整体能耗反而可能下降，因为频道分配路径发生了变化。

需要注意，**频道和线缆颜色没有关系**，线缆颜色只能阻止线缆连接。

## 频道寻路

在使用 <ItemLink id="controller" /> 时，频道寻路分 3 步：第一步，沿最短路径穿过相邻机器，先到最近的[普通线缆](../items-blocks-machines/cables.md)（玻璃、包层、智能）；第二步，沿最短路径经由该普通线缆，前往最近的[致密线缆](../items-blocks-machines/cables.md)（致密、致密智能）；第三步，沿最短路径经由该致密线缆到达 <ItemLink id="controller" />。若最短路径已被占满，某些[设备](devices.md)就会拿不到频道。可借助染色线缆、线缆锚和 P2P 通道引导频道按预期路径分配。

例如，在下述示例中某些驱动器分不到频道：尽管线缆总容量足够，频道会尝试沿最短路径行进，从而导致某些线缆过载而其他线缆留空。

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="../assets/assemblies/channel_path_length_issue.snbt" />

  <LineAnnotation color="#33ff33" from="3 .5 1.4" to="0.4 0.5 1.4" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="0.4 .5 1.4" to="0.4 0.5 3.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="0.4 0.5 3.6" to="1.4 0.5 3.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="1.4 0.5 3.6" to="1.4 0.5 5" alwaysOnTop={true} thickness="0.05"/>

  <LineAnnotation color="#33ff33" from="3 0.5 3.6" to="1.6 0.5 3.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="1.6 0.5 3.6" to="1.6 0.5 5" alwaysOnTop={true} thickness="0.05"/>

  <LineAnnotation color="#ff3333" from="3 .5 1.6" to="0.6 .5 1.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#ff3333" from="0.6 .5 1.6" to="0.6 .5 3.4" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#ff3333" from="0.6 .5 3.4" to="1.4 .5 3.4" alwaysOnTop={true} thickness="0.05"/>

  <LineAnnotation color="#ff3333" from="3 .5 3.4" to="1.6 .5 3.4" alwaysOnTop={true} thickness="0.05"/>

  <BoxAnnotation color="#dddddd" min="1.2 0.2 3.2" max="1.8 0.8 3.8" alwaysOnTop={true} thickness="0.05">
        试图在此处传输多于8个频道，因此某些频道路径被截断。
  </BoxAnnotation>

  <IsometricCamera yaw="90" pitch="90" />

</GameScene>

这一问题可由限制频道路径解决。网络的形态应当是树形（或灌木型）。应当避免出现环形和不明确路径。

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="../assets/assemblies/channel_path_length_issue_fix.snbt" />

  <LineAnnotation color="#33ff33" from="3 .5 1.4" to="0.4 0.5 1.4" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="0.4 .5 1.4" to="0.4 0.5 5.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="0.4 0.5 5.6" to="1 0.5 5.6" alwaysOnTop={true} thickness="0.05"/>

  <LineAnnotation color="#33ff33" from="3 0.5 3.6" to="1.6 0.5 3.6" alwaysOnTop={true} thickness="0.05"/>
  <LineAnnotation color="#33ff33" from="1.6 0.5 3.6" to="1.6 0.5 5" alwaysOnTop={true} thickness="0.05"/>

  <IsometricCamera yaw="90" pitch="90" />

</GameScene>

## 自组织网络

不带<ItemLink id="controller" />的网络是自组织网络，最多能支持8台占用频道的设备。如果占用频道的设备超过8台，则网络会失效，可移除设备或加入<ItemLink id="controller" />以解决。

和带有控制器的网络不同的是，自组织网络中的[智能线缆](../items-blocks-machines/cables.md)会显示整个网络的频道占用数，而非途经该段线缆的频道数。

自组织网络中的每台设备会占用整个网络中的1个频道，和沿最短路径分配频道的行为非常不同。

## 设计

正如前文[频道寻路](channels.md#channel-routing)中所提，推荐将网络设计为树形结构：从控制器处引出致密线缆，致密线缆处引出普通线缆，在普通线缆上最多连接8台[设备](../ae2-mechanics/devices.md)。

如下是一个反面示例：

沿频道路径来看，

1. 自控制器出发后，首先遇到的驱动器和普通线缆表现相同，因此将频道上限锁在了8个。

不过此处没有使用智能线缆，无法查看具体使用了多少频道。剩余 8 个频道。
2. 驱动器占用1个频道。
剩余7个频道。
3. 终端占用2个频道。
剩余5个频道。
4. 右侧的接口占用1个频道。
剩余4个频道。
5. 样板供应器占用1个频道。
剩余3个频道。
6. 右侧的输入总线占用1个频道。
剩余2个频道。
7. 用于供应装配室的样板供应器组只能拿到2个频道，其余2个则缺少频道。

总体看来，问题主要出在锁死频道数上限和未考虑频道分配方式上。

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="../assets/assemblies/bad_network_structure.snbt" />

<LineAnnotation color="#33ff33" from="6.5 .5 1.5" to="6 .5 1.5" alwaysOnTop={true} thickness="0.4">
  32个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="6 .5 1.5" to="5.5 .5 1.5" alwaysOnTop={true} thickness="0.2">
  8个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="5.5 .5 1.5" to="5.5 1.5 1.5" alwaysOnTop={true} thickness="0.1">
  2个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="5.5 .5 1.5" to="5.5 .3 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="5.5 1.5 1.5" to="5.5 2.5 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="5.5 2.5 1.5" to="5.5 2.5 1.1" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="5.5 .5 1.5" to="4.5 .5 1.5" alwaysOnTop={true} thickness="0.158">
  5个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="4.5 .5 1.5" to="4.5 .3 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="4.5 .5 1.5" to="4.5 1.5 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="4.5 .5 1.5" to="3.5 .5 1.5" alwaysOnTop={true} thickness="0.122">
  3个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="3.5 .5 1.5" to="3.5 2.5 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="3.5 2.5 1.5" to="3.7 2.5 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="3.5 .5 1.5" to="1.5 .5 1.5" alwaysOnTop={true} thickness="0.1">
  2个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="1.5 0.5 1.5" to="1.5 0.3 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="1.5 0.5 1.5" to="0.5 0.5 1.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#33ff33" from="0.5 0.5 1.5" to="0.5 0.5 0.5" alwaysOnTop={true} thickness="0.071">
  1个频道
</LineAnnotation>

<LineAnnotation color="#ff3333" from="0.5 1.5 1.5" to="0.5 1.3 1.5" alwaysOnTop={true} thickness="0.071">
  无频道
</LineAnnotation>

<LineAnnotation color="#ff3333" from="1.5 1.5 0.5" to="1.5 1.3 0.5" alwaysOnTop={true} thickness="0.071">
  无频道
</LineAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

---

再给出一个正面示例：

<GameScene zoom="2.5" interactive={true}>
  <ImportStructure src="../assets/assemblies/treelike_network_structure.snbt" />

    <BoxAnnotation color="#dddddd" min="6.9 0 4.9" max="9.1 4 7.1" thickness="0.05">
        注意样板供应器为8个一组。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="5 4 4" max="8 5 5" thickness="0.05">
        两条频道占用满的普通线缆连接处需用致密线缆。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="5 0 13" max="8 1 14" thickness="0.05">
        线缆颜色的不同可避免相邻的线缆相互连接。
    </BoxAnnotation>


  <IsometricCamera yaw="315" pitch="30" />
</GameScene>

## 频道模式

Minecraft 1.18 版本的 AE2 10.0.0 引入了改变 AE2 频道行为的新选项。在配置文件「通用」部分有新选项（`channels`）可供控制，管理员也可使用游戏内命令直接在游戏中更改模式。更改命令为 `/ae2 channelmode <模式>`，显示当前模式的命令为 `/ae2 channelmode`。如果是在游戏中进行的模式更改，则所有网络都会重置并立即改用新模式。

这重新引入了 Minecraft 1.12 中的选项，并加以改进；对于想要游戏体验稍微轻松些，但又不希望完全移除频道机制的玩家而言，这算是更好的选择。

配置文件和命令中可用模式列表如下。


| 设置         | 描述                                                               |
| ---------- | ---------------------------------------------------------------- |
| `default`  | 此指南描述的线缆与自组织网络标准频道容量                                             |
| `x2`       | 所有频道容量变为双倍（普通线缆16个，致密线缆64个，自组织网络16个）                             |
| `x3`       | 所有频道容量变为三倍（普通线缆24个，致密线缆92个，自组织网络24个）                             |
| `x4`       | 所有频道容量变为四倍（普通线缆32个，致密线缆128个，自组织网络32个）                            |
| `infinite` | 移除所有频道限制。控制器仍能*大幅*减少能量消耗。此时，智能线缆只有完全关闭（不传输频道）和完全打开（传输若干频道）这两个状态。 |

