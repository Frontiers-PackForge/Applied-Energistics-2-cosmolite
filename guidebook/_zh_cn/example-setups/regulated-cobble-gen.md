---
navigation:
  parent: example-setups/example-setups-index.md
  title: 自调控造石机
  icon: minecraft:cobblestone
---

# 自调控造石机

自动化造石机很简单，把<ItemLink id="annihilation_plane" />朝向一个标准原版手动造石机即可。然而这么做会导致网络被圆石塞满，因此需要一些调控。

鉴于破坏面板的工作方式（类似<ItemLink id="import_bus" />），不能直接把<ItemLink id="level_emitter" />面向装有<ItemLink id="redstone_card" />的<ItemLink id="export_bus" />（因为无法在没有中间存储的情况下直接输入输出）。需要稍微绕点路。

<ItemLink id="toggle_bus" />可通过红石信号接通或断开部分网络，但每次切换都会导致网络重启。简单的解决办法是：把触发总线放在[子网](../ae2-mechanics/subnetworks.md)上，这样只会重启子网。

可以让由<ItemLink id="annihilation_plane" />与<ItemLink id="storage_bus" />构成的独立[子网](../ae2-mechanics/subnetworks.md)把物品推入主网上的<ItemLink id="interface" />。触发总线则通过接通或断开与<ItemLink id="quartz_fiber" />的连接，为子网供电或切断供电。

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="../assets/assemblies/regulated_cobble_gen.snbt" />

<BoxAnnotation color="#dddddd" min="3 2 2" max="7 2.3 3">
        （1）破坏面板：无 GUI、无法配置，可附效率与耐久以降低能耗。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="2 2 2" max="2.3 3 3">
        （2）存储总线：默认配置。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="2.3 2.3 2" max="2.7 2.7 2.3">
        （3）触发总线：注意要放在子网上，而非主网络。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="2.3 3 2.3" max="2.7 3.3 2.7">
        （4）标准发信器：按圆石与目标数量配置，模式为「存量低于阈值时输出红石信号」。
  </BoxAnnotation>

  <BoxAnnotation color="#dddddd" min="1 2 3" max="2 3 2">
        （5）接口：默认配置。
  </BoxAnnotation>

<DiamondAnnotation pos="0 2.5 1.5" color="#00ff00">
        至主网络
    </DiamondAnnotation>

<DiamondAnnotation pos="5 1.5 3.5" color="#00ff00">
        含水楼梯，防止水流至熔岩处而将其变为黑曜石。
    </DiamondAnnotation>

  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

## 配置

* <ItemLink id="annihilation_plane" />（1）无 GUI、无法配置，可附效率与耐久以降低能耗。
* <ItemLink id="storage_bus" />（2）处于默认配置。
* <ItemLink id="toggle_bus" />（3）必须接在<ItemLink id="quartz_fiber" />的**子网一侧**，不能接在主网络一侧，否则主网络会在每次切换时重启。
* <ItemLink id="level_emitter" />（4）按圆石与目标数量配置，并设为「存量低于阈值时输出红石信号」。
* <ItemLink id="interface" />（5）处于默认配置。

## 工作原理

1. 造石机产生圆石。
2. <ItemLink id="annihilation_plane" />破坏圆石。
3. <ItemLink id="storage_bus" />将圆石存入<ItemLink id="interface" />，并送入主网络。
4. 当主网络中的圆石数量超过设定值时，<ItemLink id="level_emitter" />停止输出红石信号，从而关闭<ItemLink id="toggle_bus" />。
5. 子网被切断供电，破坏面板停止工作。
