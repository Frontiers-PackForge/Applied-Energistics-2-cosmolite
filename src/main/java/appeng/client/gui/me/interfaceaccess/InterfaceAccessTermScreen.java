package appeng.client.gui.me.interfaceaccess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.core.AppEng;
import appeng.menu.implementations.InterfaceAccessTermMenu;

public class InterfaceAccessTermScreen<C extends InterfaceAccessTermMenu> extends AEBaseScreen<C> {
    private static final int GUI_WIDTH = 195;
    private static final int GUI_TOP_AND_BOTTOM_PADDING = 54;
    private static final int ROW_HEIGHT = 18;
    private static final int SLOT_SIZE = ROW_HEIGHT;
    private static final int COLUMNS = 9;
    private static final int GUI_PADDING_X = 8;
    private static final int GUI_PADDING_Y = 6;
    private static final int GUI_HEADER_HEIGHT = 17;

    private static final net.minecraft.client.renderer.Rect2i HEADER_BBOX = new net.minecraft.client.renderer.Rect2i(0,
            0, GUI_WIDTH, 17);
    private static final net.minecraft.client.renderer.Rect2i ROW_TEXT_TOP_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 17, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i ROW_TEXT_MIDDLE_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 53, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i ROW_TEXT_BOTTOM_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 89, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i ROW_INVENTORY_TOP_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 35, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i ROW_INVENTORY_MIDDLE_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 71, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i ROW_INVENTORY_BOTTOM_BBOX = new net.minecraft.client.renderer.Rect2i(
            0, 107, GUI_WIDTH, ROW_HEIGHT);
    private static final net.minecraft.client.renderer.Rect2i FOOTER_BBOX = new net.minecraft.client.renderer.Rect2i(0,
            125, GUI_WIDTH, 97);

    private final Scrollbar scrollbar;
    private int visibleRows = 0;

    public InterfaceAccessTermScreen(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.imageWidth = GUI_WIDTH;
        this.scrollbar = widgets.addScrollBar("scrollbar");
    }

    static class HostRecord {
        final long id;
        final ItemStack icon;
        final SimpleInventory config;
        final SimpleInventory storage;

        HostRecord(long id, ItemStack icon, int configSize, int storageSize) {
            this.id = id;
            this.icon = icon;
            this.config = new SimpleInventory(configSize);
            this.storage = new SimpleInventory(storageSize);
        }
    }

    private final HashMap<Long, HostRecord> hosts = new HashMap<>();
    private final ArrayList<HostRecord> sortedHosts = new ArrayList<>();
    private final ArrayList<Row> rows = new ArrayList<>();

    public void postFullUpdate(long id, ItemStack icon, int configSize, int storageSize,
            Int2ObjectMap<ItemStack> config, Int2ObjectMap<ItemStack> storage) {
        var rec = new HostRecord(id, icon, configSize, storageSize);
        for (var e : config.int2ObjectEntrySet())
            rec.config.setItemDirect(e.getIntKey(), e.getValue());
        for (var e : storage.int2ObjectEntrySet())
            rec.storage.setItemDirect(e.getIntKey(), e.getValue());
        hosts.put(id, rec);
        rebuildList();
    }

    public void postIncrementalUpdate(long id, Int2ObjectMap<ItemStack> config, Int2ObjectMap<ItemStack> storage) {
        var rec = hosts.get(id);
        if (rec == null)
            return;
        for (var e : config.int2ObjectEntrySet())
            rec.config.setItemDirect(e.getIntKey(), e.getValue());
        for (var e : storage.int2ObjectEntrySet())
            rec.storage.setItemDirect(e.getIntKey(), e.getValue());
        rebuildList();
    }

    public void clear() {
        hosts.clear();
        sortedHosts.clear();
        rows.clear();
    }

    @Override
    public void init() {
        this.visibleRows = config.getTerminalStyle().getRows(
                (this.height - 17 - 97 - GUI_TOP_AND_BOTTOM_PADDING) / ROW_HEIGHT);
        this.imageHeight = 17 + 97 + this.visibleRows * ROW_HEIGHT;
        super.init();
        resetScrollbar();
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        // Draw header & footer
        blit(guiGraphics, offsetX, offsetY, HEADER_BBOX);
        final int scrollLevel = scrollbar.getCurrentScroll();
        int currentY = offsetY + 17;
        // Footer first so slots draw on top
        blit(guiGraphics, offsetX, currentY + this.visibleRows * ROW_HEIGHT, FOOTER_BBOX);

        for (int i = 0; i < this.visibleRows; ++i) {
            boolean firstLine = i == 0;
            boolean lastLine = i == this.visibleRows - 1;
            var bbox = selectRowBackgroundBox(false, firstLine, lastLine);
            blit(guiGraphics, offsetX, currentY, bbox);
            if (scrollLevel + i < this.rows.size()) {
                var row = this.rows.get(scrollLevel + i);
                if (row instanceof SlotsRow slotsRow) {
                    bbox = selectRowBackgroundBox(true, firstLine, lastLine);
                    bbox.setWidth(8 + SLOT_SIZE * slotsRow.slots - 1);
                    blit(guiGraphics, offsetX, currentY, bbox);
                }
            }
            currentY += ROW_HEIGHT;
        }
    }

    @Override
    protected void slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (slot instanceof InterfaceSlot is) {
            appeng.helpers.InventoryAction action = null;
            if (mouseButton == 2) {
                action = appeng.helpers.InventoryAction.CREATIVE_DUPLICATE;
            } else {
                switch (clickType) {
                    case PICKUP:
                        action = mouseButton == 1 ? appeng.helpers.InventoryAction.SPLIT_OR_PLACE_SINGLE
                                : appeng.helpers.InventoryAction.PICKUP_OR_SET_DOWN;
                        break;
                    case QUICK_MOVE:
                        action = mouseButton == 1 ? appeng.helpers.InventoryAction.PICKUP_SINGLE
                                : appeng.helpers.InventoryAction.SHIFT_CLICK;
                        break;
                    case CLONE:
                        action = appeng.helpers.InventoryAction.CREATIVE_DUPLICATE;
                        break;
                    default:
                }
            }
            if (action != null) {
                var packet = new appeng.core.sync.packets.InventoryActionPacket(action, is.getServerSlot(),
                        is.getServerId());
                appeng.core.sync.network.NetworkHandler.instance().sendToServer(packet);
                return;
            }
        }
        super.slotClicked(slot, slotIdx, mouseButton, clickType);
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        // Remove old dynamic slots
        this.menu.slots.removeIf(slot -> slot instanceof InterfaceSlot);
        final int scrollLevel = scrollbar.getCurrentScroll();
        int currentY = offsetY + GUI_HEADER_HEIGHT;
        for (int i = 0; i < this.visibleRows; ++i) {
            if (scrollLevel + i < this.rows.size()) {
                var row = this.rows.get(scrollLevel + i);
                if (row instanceof GroupHeaderRow header) {
                    var icon = header.host.icon;
                    if (!icon.isEmpty()) {
                        guiGraphics.renderItem(icon, offsetX + GUI_PADDING_X, currentY);
                    } else {
                        guiGraphics.renderItem(new ItemStack(Items.CHEST), offsetX + GUI_PADDING_X, currentY);
                    }
                    var name = header.host.icon.getHoverName();
                    guiGraphics.drawString(font, name, offsetX + GUI_PADDING_X + 18 + 2, currentY + 4, 0xFFFFFF, false);
                    currentY += ROW_HEIGHT;
                } else if (row instanceof SlotsRow slotsRow) {
                    for (int col = 0; col < slotsRow.slots; col++) {
                        int slotIndex = slotsRow.offset + col;
                        var st = slotsRow.inv.getStackInSlot(slotIndex);
                        guiGraphics.renderItem(st, offsetX + GUI_PADDING_X + col * SLOT_SIZE, currentY);
                        int combined = slotsRow.baseIndex + slotIndex;
                        this.menu.slots.add(new InterfaceSlot(slotsRow.inv, slotIndex, slotsRow.host.id, combined,
                                offsetX + GUI_PADDING_X + col * SLOT_SIZE, currentY));
                    }
                    currentY += ROW_HEIGHT;
                }
            }
        }
    }

    // Simple client-side inventory holder for slots drawing
    static class SimpleInventory extends appeng.util.inv.AppEngInternalInventory {
        public SimpleInventory(int size) {
            super(size);
        }

        public void setItemDirect(int slot, ItemStack st) {
            super.setItemDirect(slot, st);
        }
    }

    private void rebuildList() {
        sortedHosts.clear();
        sortedHosts.addAll(hosts.values());
        sortedHosts.sort(Comparator.comparing(rec -> rec.icon.getHoverName().getString()));
        rows.clear();
        for (var host : sortedHosts) {
            rows.add(new GroupHeaderRow(host));
            for (int offset = 0; offset < host.config.size(); offset += COLUMNS) {
                int slots = Math.min(COLUMNS, host.config.size() - offset);
                rows.add(new SlotsRow(host, host.config, 0, offset, slots));
            }
            int base = host.config.size();
            for (int offset = 0; offset < host.storage.size(); offset += COLUMNS) {
                int slots = Math.min(COLUMNS, host.storage.size() - offset);
                rows.add(new SlotsRow(host, host.storage, base, offset, slots));
            }
        }
        resetScrollbar();
    }

    private void resetScrollbar() {
        scrollbar.setHeight(this.visibleRows * ROW_HEIGHT - 2);
        scrollbar.setRange(0, Math.max(0, this.rows.size() - this.visibleRows), 2);
    }

    private void blit(GuiGraphics g, int x, int y, net.minecraft.client.renderer.Rect2i src) {
        var tex = AppEng.makeId("textures/guis/patternaccessterminal.png");
        g.blit(tex, x, y, src.getX(), src.getY(), src.getWidth(), src.getHeight());
    }

    private net.minecraft.client.renderer.Rect2i selectRowBackgroundBox(boolean isInvLine, boolean firstLine,
            boolean lastLine) {
        if (isInvLine) {
            if (firstLine)
                return ROW_INVENTORY_TOP_BBOX;
            else if (lastLine)
                return ROW_INVENTORY_BOTTOM_BBOX;
            else
                return ROW_INVENTORY_MIDDLE_BBOX;
        } else if (firstLine) {
            return ROW_TEXT_TOP_BBOX;
        } else if (lastLine) {
            return ROW_TEXT_BOTTOM_BBOX;
        } else {
            return ROW_TEXT_MIDDLE_BBOX;
        }
    }

    sealed interface Row permits GroupHeaderRow, SlotsRow {
    }

    private record GroupHeaderRow(HostRecord host) implements Row {
    }

    private record SlotsRow(HostRecord host, SimpleInventory inv, int baseIndex, int offset, int slots)
            implements
                Row {
    }
}
