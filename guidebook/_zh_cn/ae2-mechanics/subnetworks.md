---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 子网
---

# 子网

<GameScene zoom="4" interactive={true}>
<ImportStructure src="../assets/assemblies/subnet_demonstration.snbt" />

<DiamondAnnotation pos="6.5 2.5 0.5" color="#00ff00">
        物品管道子网
    </DiamondAnnotation>

<DiamondAnnotation pos="5.5 2.5 0.5" color="#00ff00">
        流体管道子网
    </DiamondAnnotation>

<DiamondAnnotation pos="4.5 2.5 0.5" color="#00ff00">
        过滤型破坏面板
    </DiamondAnnotation>

<DiamondAnnotation pos="3.5 2.5 0.5" color="#00ff00">
        成型面板子网
    </DiamondAnnotation>

<DiamondAnnotation pos="2.5 2.5 0.5" color="#00ff00">
        利用“接口 + 存储总线”交互实现的本地子存储，主网可访问
    </DiamondAnnotation>

<DiamondAnnotation pos="1.5 1.5 0.5" color="#00ff00">
        另一个物品管道子网，用于把充能后的物品送回样板供应器
    </DiamondAnnotation>

<IsometricCamera yaw="195" pitch="30" />
</GameScene>

“子网”并没有特别严格的定义。你可以把它理解为：任何用来辅助主网络，或专门处理某个小任务的网络。它们通常规模不大，往往不需要控制器。常见用途主要有两类：

*   限制哪些[设备](../ae2-mechanics/devices.md)能访问哪些存储（例如你通常不希望“管道”子网上的导入总线能访问主网存储，否则它会把物品塞进主网存储元件，而不是目标容器）。
*   节省主网频道。例如，让一个样板供应器输出到一个接口，再通过该接口连接多台机器上的多个存储总线，这样主网只占 1 个频道；如果每台机器都单独放样板供应器，就会占用多个频道。

不同颜色线缆与“是否构成子网”本身无关；唯一相关点是不同颜色线缆彼此不连接。

子网可以做成很多形式，例如：

*   导入总线与存储总线：在两个容器间传输物品/流体，类似物品管道或流体管道。
*   破坏面板与存储总线：让破坏面板打掉的东西只能进入指定存储总线，从而实现过滤。
*   接口与成型面板：插入接口的物品会被推到成型面板，再放置/丢到世界中。
*   自动生产赛特斯石英的装置，并由主网上的 <ItemLink id="level_emitter" /> 调节与控制。
*   通过“接口上挂存储总线”的特殊交互实现一套主网可访问的专用存储，用于承接农场产出，防止主存储无限溢出。
*   等等。

<ItemLink id="quartz_fiber" /> 在做子网时非常有用。它可以在不连接网络的前提下跨网传电，让你无需在每个子网都单独放能源接收器和供电线缆。