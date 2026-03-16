package appeng.client.gui;

import appeng.core.AEConfig;

public class ScreenColor {
    public static int getColor() {
        return AEConfig.instance().getScreenColor();
    }

    public static float getRed() {
        return (getColor() >> 16 & 255) / 255f;
    }

    public static float getGreen() {
        return (getColor() >> 8 & 255) / 255f;
    }

    public static float getBlue() {
        return (getColor() & 255) / 255f;
    }
}
