---
navigation:
  parent: items-blocks-machines/items-blocks-machines-index.md
  title: 样板
  icon: crafting_pattern
  position: 410
categories:
- tools
item_ids:
- ae2:blank_pattern
- ae2:crafting_pattern
- ae2:processing_pattern
- ae2:smithing_table_pattern
- ae2:stonecutting_pattern
---

# 样板

<ItemImage id="crafting_pattern" scale="4" />

样板由空白样板在 <ItemLink id="pattern_encoding_terminal" /> 中编码而成，可装入 <ItemLink id="pattern_provider" /> 和 <ItemLink id="molecular_assembler" />。

有若干种不同的样板，分别为不同处理方式设计：

*   <ItemLink id="crafting_pattern" />能编码工作台的配方。可将此类样板直接放入<ItemLink id="molecular_assembler" />，使其在收到材料时自动合成；但其主要用途仍是放在与分子装配室相邻的<ItemLink id="pattern_provider" />中。样板供应器在此情况下有特殊行为，会将相关样板和材料输入相邻装配室。因为装配室会将产物自动弹出到相邻容器，相邻放置的装配室和样板供应器就是自动化合成样板所需的一切了。

***

*   <ItemLink id="smithing_table_pattern" />与合成样板非常相似，但编码的是锻造台配方。它们也可通过样板供应器和分子装配室自动化，工作流程也完全一致。实际上，合成、锻造台、切石机样板所需的设施均完全一致。

***

*   <ItemLink id="stonecutting_pattern" />与合成样板非常相似，但编码的是切石机配方。它们也可通过样板供应器和分子装配室自动化，工作流程也完全一致。实际上，合成、锻造台、切石机样板所需的设施均完全一致。

***

*   <ItemLink id="processing_pattern" /> 是自动合成灵活性的核心。它是最通用的样板类型，简单来说就是：“如果样板供应器把这些材料输出到相邻容器，那么 ME 系统会在未来某个时刻收到这些产物。”它适用于几乎所有模组机器（包括熔炉类流程）的自动化。因为它只关心“投入了什么”和“最终收到了什么”，而不关心中间过程，所以你可以做很复杂的链路。只要系统收到样板声明的产物，它就会认为任务完成。甚至原料与产物逻辑上无关也可以：例如定义“1x 樱花木板 = 1x 下界之星”，再让凋灵农场在收到樱花木板后击杀凋灵，系统照样可正常运行。

多个拥有相同样板的<ItemLink id="pattern_provider" />会并行工作，并且，还可以设置诸如「8× 圆石 = 8× 石头」的配方而非「1× 圆石 = 1× 石头」，样板供应器每次运行都会向烧炼设施输入 8 个圆石，而不是每次只输入 1 个。

## 配方

<RecipeFor id="blank_pattern" />
