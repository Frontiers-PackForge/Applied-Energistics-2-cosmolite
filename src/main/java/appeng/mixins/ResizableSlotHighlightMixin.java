package appeng.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import appeng.client.gui.AEBaseScreen;

/**
 * Allows custom highlight for slots in {@link AEBaseScreen}.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class ResizableSlotHighlightMixin {
    @Inject(method = "renderSlotHighlight*", at = @At("HEAD"), cancellable = true)
    private static void renderResizableSlotHighlight(GuiGraphics guiGraphics, int x, int y, int z, int color,
            CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof AEBaseScreen<?> self) {
            self.renderCustomSlotHighlight(guiGraphics, x, y, z);
            ci.cancel();
        }
    }
}
