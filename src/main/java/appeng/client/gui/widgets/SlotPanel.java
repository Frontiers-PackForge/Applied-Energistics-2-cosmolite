/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.client.gui.widgets;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import appeng.client.gui.style.Blitter;

public class SlotPanel extends AbstractWidget implements ITooltip {

    public static final int SLOT_SIZE = 18;
    public static final int PADDING = 7;

    private static final Blitter BACKGROUND = Blitter.texture("guis/extra_panels.png", 128, 128);

    private final List<Slot> slots;
    private final Supplier<List<Component>> tooltipSupplier;

    public SlotPanel(List<Slot> slots, Supplier<List<Component>> tooltipSupplier) {
        super(0, 0, 2 * PADDING + SLOT_SIZE, 2 * PADDING + slots.size() * SLOT_SIZE, Component.empty());
        this.slots = slots;
        this.tooltipSupplier = tooltipSupplier;
        this.active = false;
    }

    public void layout(int screenLeft, int screenTop, int x, int y) {
        setX(screenLeft + x);
        setY(screenTop + y);

        for (int i = 0; i < slots.size(); i++) {
            var slot = slots.get(i);
            slot.x = x + PADDING + 1;
            slot.y = y + PADDING + 1 + i * SLOT_SIZE;
        }
    }

    @Override
    protected void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        for (int i = 0; i < slots.size(); i++) {
            var first = i == 0;
            var last = i == slots.size() - 1;

            BACKGROUND.src(0, first ? 0 : PADDING, getWidth(), SLOT_SIZE + (first ? PADDING : 0) + (last ? PADDING : 0))
                    .dest(getX(), getY() + PADDING + i * SLOT_SIZE - (first ? PADDING : 0))
                    .blit(gg);
        }
    }

    @Override
    public List<Component> getTooltipMessage() {
        return tooltipSupplier.get();
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return visible;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
