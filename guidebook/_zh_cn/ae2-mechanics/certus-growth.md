---
navigation:
  parent: ae2-mechanics/ae2-mechanics-index.md
  title: 赛特斯石英的生长
  icon: quartz_cluster
---

# 赛特斯石英的生长

## 基本内容与「开始与入门」页相同

<GameScene zoom="6" background="transparent">
<ImportStructure src="../assets/assemblies/budding_certus_1.snbt" />
</GameScene>

赛特斯石英芽会从[赛特斯石英母岩](../items-blocks-machines/budding_certus.md)上生长，机制与紫水晶相似。若打掉**尚未成熟**的芽，会掉落 1 个 <ItemLink id="certus_quartz_dust" />，**不受**时运影响。若打掉**完全成熟**的簇，会掉落 4 个 <ItemLink id="certus_quartz_crystal" />，且时运可提升掉落量。

赛特斯石英母岩共有 4 个等级：**无瑕、有瑕、开裂、破损**。

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/assemblies/budding_blocks.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

每当赛特斯石英芽再长一阶，母岩都有概率**降一级**，最终退化为普通赛特斯石英块。把母岩（或普通赛特斯石英块）与一枚或多枚 <ItemLink id="charged_certus_quartz_crystal" /> 一同丢入水中，即可**修复**并生成新的母岩。

<RecipeFor id="damaged_budding_quartz" />

无瑕母岩**不会**降级，可无限产出赛特斯石英；但它们**无法**合成，也无法用镐（含精准采集）完好挖走搬运。（不过*可以*用[空间存储](../ae2-mechanics/spatial-io.md)整体搬迁。）

仅靠自然生长时，芽会非常慢。好在把 <ItemLink id="growth_accelerator" /> 贴在母岩旁边可**大幅**提速——建议尽早做几台。

<GameScene zoom="4" background="transparent">
  <ImportStructure src="../assets/assemblies/budding_certus_2.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

若石英还不够做 <ItemLink id="energy_acceptor" /> 或 <ItemLink id="vibration_chamber" />，可以先做一个 <ItemLink id="crank" />，装在催生器末端手摇供能。

自动采收赛特斯石英的做法见[此处](../example-setups/simple-certus-farm.md)。
