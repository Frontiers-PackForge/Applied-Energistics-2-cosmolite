---
navigation:
  parent: example-setups/example-setups-index.md
  title: “主网络”示例
  icon: controller
---

# “主网络”示例

许多其他设施都会提到「主网络」。你也可能好奇这些[设备](../ae2-mechanics/devices.md)如何拼成一个能跑起来的系统。下面是一个示例：

<GameScene zoom="2.5" interactive={true}>
  <ImportStructure src="../assets/assemblies/treelike_network_structure.snbt" />

    <BoxAnnotation color="#dddddd" min="3.9 0 1.9" max="9.1 5 7.1" thickness="0.05">
        大簇的样板供应器与装配室，为合成、切石、锻造等样板留出大量空间。
        棋盘式排布让供应器能并行使用多台装配室，同时保持紧凑。
        每 8 台一组，频道便不会错误寻路。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="3.9 0 9.9" max="5.1 3 12.1" thickness="0.05">
        一些机器，用管道子网把产物推进供应器。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="-0.1 0 8.9" max="1.1 3 13.1" thickness="0.05">
        若干终端与杂项小装置。（实际基地里通常只要合成终端即可，不必同时摆普通终端与合成终端。）
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="-0.1 0 -0.1" max="2.1 3 8.1" thickness="0.05">
        一排合成 CPU：少数容量大些，多数容量小些。
        真用起来往往还要更多协处理器，但那样场景会太大。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="5.9 0 13.9" max="7.1 1 15.1" thickness="0.05">
        控制器宜放在基地中央，体积多半也比这里更大；细长条形状往往不错。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="11.9 0 7.9" max="13.1 4 13.1" thickness="0.05">
        多种存储做法：驱动器或存储总线等。注意都是 8 个一组。
    </BoxAnnotation>

    <BoxAnnotation color="#dddddd" min="10.9 0 0.9" max="13.1 2 7.1" thickness="0.05">
        多种存储做法：驱动器或存储总线等。注意都是 8 个一组。
    </BoxAnnotation>

  <IsometricCamera yaw="315" pitch="30" />
</GameScene>
