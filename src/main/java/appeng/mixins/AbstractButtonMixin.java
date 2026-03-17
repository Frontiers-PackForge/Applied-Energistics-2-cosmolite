package appeng.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.ScreenColor;

@Mixin(AbstractButton.class)
public class AbstractButtonMixin {
    /**
     * Allows us to set the color of the button to the screen color, only in AE screens.
     */
    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;setColor(FFFF)V", ordinal = 0))
    private void redirectSetColor(GuiGraphics instance, float r, float g, float b, float a) {
        if (Minecraft.getInstance().screen instanceof AEBaseScreen<?>) {
            instance.setColor(ScreenColor.getRed(), ScreenColor.getGreen(), ScreenColor.getBlue(), a);
        } else {
            instance.setColor(r, g, b, a);
        }
    }
}
