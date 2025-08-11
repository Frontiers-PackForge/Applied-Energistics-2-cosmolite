package appeng.crafting.pattern;

import appeng.api.stacks.GenericStack;
import java.util.List;
import java.util.Set;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record PatternKeyTooltipComponent(
        List<GenericStack> inputs,
        List<GenericStack> outputs,
        String author,
        boolean isCrafting,
        boolean canSubstitute,
        boolean canSubstituteFluids,
        boolean showAmounts) implements TooltipComponent {

}
