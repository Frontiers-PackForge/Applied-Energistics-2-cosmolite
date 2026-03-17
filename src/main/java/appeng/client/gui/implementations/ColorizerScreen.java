package appeng.client.gui.implementations;

import java.awt.Color;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.AEConfig;
import appeng.core.localization.GuiText;
import appeng.menu.implementations.ColorizerMenu;

public class ColorizerScreen extends AEBaseScreen<ColorizerMenu> {

    private final EditBox hexInput;
    private final ColorSlider redSlider;
    private final ColorSlider greenSlider;
    private final ColorSlider blueSlider;

    private int currentColor;

    public ColorizerScreen(ColorizerMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.currentColor = AEConfig.instance().getScreenColor();

        int r = (this.currentColor >> 16) & 0xFF;
        int g = (this.currentColor >> 8) & 0xFF;
        int b = this.currentColor & 0xFF;

        this.redSlider = new ColorSlider("Red", r / 255.0);
        this.greenSlider = new ColorSlider("Green", g / 255.0);
        this.blueSlider = new ColorSlider("Blue", b / 255.0);

        this.widgets.add("redSlider", this.redSlider);
        this.widgets.add("greenSlider", this.greenSlider);
        this.widgets.add("blueSlider", this.blueSlider);

        this.hexInput = widgets.addTextField("hexInput");
        this.hexInput.setMaxLength(7);
        this.hexInput.setValue(String.format("#%06X", this.currentColor));
        this.hexInput.setResponder(this::onHexChanged);

        this.widgets.add("preview", new ColorPreview());

        widgets.addButton("confirm", GuiText.Confirm.text(), button -> {
            AEConfig.instance().setScreenColor(this.currentColor);
            this.onClose();
        });
    }

    private void onHexChanged(String hex) {
        if (isValidHex(hex)) {
            try {
                int color = Color.decode(hex).getRGB() & 0xFFFFFF;
                this.currentColor = color;
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                this.redSlider.setSliderValue(r / 255.0);
                this.greenSlider.setSliderValue(g / 255.0);
                this.blueSlider.setSliderValue(b / 255.0);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private void updateColorFromSliders() {
        int r = (int) (this.redSlider.getSliderValue() * 255);
        int g = (int) (this.greenSlider.getSliderValue() * 255);
        int b = (int) (this.blueSlider.getSliderValue() * 255);
        this.currentColor = (r << 16) | (g << 8) | b;

        if (!this.hexInput.isFocused()) {
            this.hexInput.setValue(String.format("#%06X", this.currentColor));
        }
    }

    private boolean isValidHex(String colorStr) {
        if (colorStr == null || !colorStr.startsWith("#") || colorStr.length() != 7) {
            return false;
        }
        try {
            Color.decode(colorStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.getFocused() != null && this.isDragging() && button == 0) {
            if (this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private class ColorSlider extends AbstractSliderButton {
        private final String label;

        public ColorSlider(String label, double initialValue) {
            super(0, 0, 0, 0, Component.literal(""), initialValue);
            this.label = label;
            this.updateMessage();
        }

        public double getSliderValue() {
            return this.value;
        }

        public void setSliderValue(double val) {
            this.value = val;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int val = (int) (this.value * 255);
            this.setMessage(Component.literal(this.label + ": " + val));
        }

        @Override
        protected void applyValue() {
            ColorizerScreen.this.updateColorFromSliders();
        }
    }

    private class ColorPreview extends AbstractWidget {

        public ColorPreview() {
            super(0, 0, 0, 0, Component.literal(""));
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
            int colorWithAlpha = 0xFF000000 | ColorizerScreen.this.currentColor;
            guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colorWithAlpha);
            guiGraphics.renderOutline(getX(), getY(), getWidth(), getHeight(), 0xFFFFFFFF);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}
