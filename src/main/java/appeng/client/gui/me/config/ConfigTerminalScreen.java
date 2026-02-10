package appeng.client.gui.me.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import appeng.api.implementations.blockentities.ConfigFilterGroup;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.guidebook.document.LytRect;
import appeng.client.guidebook.render.SimpleRenderContext;
import appeng.core.AppEng;
import appeng.core.localization.GuiText;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.HighlightPartRequestPacket;
import appeng.menu.implementations.ConfigurationTerminalMenu;

public class ConfigTerminalScreen<C extends ConfigurationTerminalMenu> extends AEBaseScreen<C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTerminalScreen.class);

    private static final int GUI_WIDTH = 195;
    private static final int GUI_TOP_AND_BOTTOM_PADDING = 54;
    private static final int GUI_PADDING_X = 8;
    private static final int GUI_PADDING_Y = 6;
    private static final int GUI_HEADER_HEIGHT = 17;
    private static final int GUI_FOOTER_HEIGHT = 97;
    private static final int COLUMNS = 9;
    private static final int PATTERN_PROVIDER_NAME_MARGIN_X = 2;
    private static final int TEXT_MAX_WIDTH = 155;
    private static final int ROW_HEIGHT = 18;
    private static final int SLOT_SIZE = ROW_HEIGHT;
    private static final int HIGHLIGHT_BUTTON_SIZE = 12;
    private static final int HIGHLIGHT_BUTTON_X_OFFSET = 158;

    private static final Rect2i HEADER_BBOX = new Rect2i(0, 0, GUI_WIDTH, GUI_HEADER_HEIGHT);
    private static final Rect2i ROW_TEXT_TOP_BBOX = new Rect2i(0, 17, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_TEXT_MIDDLE_BBOX = new Rect2i(0, 53, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_TEXT_BOTTOM_BBOX = new Rect2i(0, 89, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_INVENTORY_TOP_BBOX = new Rect2i(0, 35, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_INVENTORY_MIDDLE_BBOX = new Rect2i(0, 71, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i ROW_INVENTORY_BOTTOM_BBOX = new Rect2i(0, 107, GUI_WIDTH, ROW_HEIGHT);
    private static final Rect2i FOOTER_BBOX = new Rect2i(0, 125, GUI_WIDTH, GUI_FOOTER_HEIGHT);

    private static final Comparator<ConfigFilterRecord> RECORD_COMPARATOR = Comparator
            .comparing(ConfigFilterRecord::getSearchName, String::compareToIgnoreCase)
            .thenComparingLong(ConfigFilterRecord::getServerId);

    private final HashMap<Long, ConfigFilterRecord> byId = new HashMap<>();
    private final ArrayList<ConfigFilterRecord> records = new ArrayList<>();
    private final ArrayList<Row> rows = new ArrayList<>();
    private final Map<String, Set<ConfigFilterRecord>> cachedSearches = new WeakHashMap<>();
    private final Scrollbar scrollbar;
    private final AETextField searchField;
    private int visibleRows = 0;

    public ConfigTerminalScreen(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.scrollbar = widgets.addScrollBar("scrollbar");
        this.imageWidth = GUI_WIDTH;
        this.searchField = widgets.addTextField("search");
        this.searchField.setResponder(str -> this.refreshList());
        this.searchField.setPlaceholder(GuiText.SearchPlaceholder.text());
    }

    @Override
    public void init() {
        this.visibleRows = Math.max(1,
                (this.height - GUI_HEADER_HEIGHT - GUI_FOOTER_HEIGHT - GUI_TOP_AND_BOTTOM_PADDING) / ROW_HEIGHT);
        this.imageHeight = GUI_HEADER_HEIGHT + GUI_FOOTER_HEIGHT + this.visibleRows * ROW_HEIGHT;
        super.init();
        this.setInitialFocus(this.searchField);
        this.resetScrollbar();
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        this.menu.slots.removeIf(slot -> slot instanceof ConfigSlot);

        int textColor = style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB();
        int scrollLevel = scrollbar.getCurrentScroll();

        for (int i = 0; i < this.visibleRows; ++i) {
            if (scrollLevel + i < this.rows.size()) {
                var row = this.rows.get(scrollLevel + i);
                if (row instanceof SlotsRow slotsRow) {
                    var record = slotsRow.record;
                    for (int col = 0; col < slotsRow.slots; col++) {
                        int slotIndex = slotsRow.offset + col;
                        var slot = new ConfigSlot(
                                record,
                                slotIndex,
                                col * SLOT_SIZE + GUI_PADDING_X,
                                (i + 1) * SLOT_SIZE);
                        this.menu.slots.add(slot);
                    }
                } else if (row instanceof HeaderRow headerRow) {
                    var record = headerRow.record;
                    if (record.getGroup().icon() != null) {
                        var renderContext = new SimpleRenderContext(LytRect.empty(), guiGraphics);
                        renderContext.renderItem(
                                record.getGroup().icon().getReadOnlyStack(),
                                GUI_PADDING_X + PATTERN_PROVIDER_NAME_MARGIN_X,
                                GUI_PADDING_Y + GUI_HEADER_HEIGHT + i * ROW_HEIGHT,
                                8, 8);
                    }
                    var displayName = record.getGroup().name();
                    var text = Language.getInstance().getVisualOrder(
                            this.font.substrByWidth(displayName, TEXT_MAX_WIDTH - HIGHLIGHT_BUTTON_SIZE - 10));
                    guiGraphics.drawString(font, text,
                            GUI_PADDING_X + PATTERN_PROVIDER_NAME_MARGIN_X + 10,
                            GUI_PADDING_Y + GUI_HEADER_HEIGHT + i * ROW_HEIGHT, textColor, false);
                }
            }
        }
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        blit(guiGraphics, offsetX, offsetY, HEADER_BBOX);
        int scrollLevel = scrollbar.getCurrentScroll();
        int currentY = offsetY + GUI_HEADER_HEIGHT;
        blit(guiGraphics, offsetX, currentY + this.visibleRows * ROW_HEIGHT, FOOTER_BBOX);

        for (int i = 0; i < this.visibleRows; ++i) {
            boolean firstLine = i == 0;
            boolean lastLine = i == this.visibleRows - 1;
            Rect2i bbox = selectRowBackgroundBox(false, firstLine, lastLine);
            blit(guiGraphics, offsetX, currentY, bbox);
            if (scrollLevel + i < this.rows.size()) {
                var row = this.rows.get(scrollLevel + i);
                if (row instanceof SlotsRow slotsRow) {
                    bbox = selectRowBackgroundBox(true, firstLine, lastLine);
                    bbox.setWidth(GUI_PADDING_X + SLOT_SIZE * slotsRow.slots - 1);
                    blit(guiGraphics, offsetX, currentY, bbox);
                }
            }
            currentY += ROW_HEIGHT;
        }
    }

    private Rect2i selectRowBackgroundBox(boolean isInvLine, boolean firstLine, boolean lastLine) {
        if (isInvLine) {
            return firstLine ? ROW_INVENTORY_TOP_BBOX
                    : (lastLine ? ROW_INVENTORY_BOTTOM_BBOX : ROW_INVENTORY_MIDDLE_BBOX);
        }
        return firstLine ? ROW_TEXT_TOP_BBOX : (lastLine ? ROW_TEXT_BOTTOM_BBOX : ROW_TEXT_MIDDLE_BBOX);
    }

    @Override
    public boolean mouseClicked(double xCoord, double yCoord, int btn) {
        if (btn == 1 && this.searchField.isMouseOver(xCoord, yCoord)) {
            this.searchField.setValue("");
        }
        int scrollLevel = scrollbar.getCurrentScroll();
        int relY = (int) yCoord - topPos - SLOT_SIZE;
        if (relY >= 0 && relY < visibleRows * ROW_HEIGHT) {
            int rowIndex = scrollLevel + relY / ROW_HEIGHT;
            if (rowIndex >= 0 && rowIndex < rows.size()) {
                var row = rows.get(rowIndex);
                if (row instanceof HeaderRow headerRow) {
                    int relX = (int) xCoord - leftPos;
                    if (relX >= HIGHLIGHT_BUTTON_X_OFFSET && relX < HIGHLIGHT_BUTTON_X_OFFSET + HIGHLIGHT_BUTTON_SIZE) {
                        NetworkHandler.instance().sendToServer(
                                new HighlightPartRequestPacket(menu.containerId, headerRow.record.getServerId()));
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot == null) {
            int scrollLevel = scrollbar.getCurrentScroll();
            int relY = y - topPos - SLOT_SIZE;
            if (relY >= 0 && relY < visibleRows * ROW_HEIGHT) {
                int rowIndex = scrollLevel + relY / ROW_HEIGHT;
                if (rowIndex >= 0 && rowIndex < rows.size()) {
                    var row = rows.get(rowIndex);
                    if (row instanceof HeaderRow) {
                        int relX = x - leftPos;
                        if (relX >= HIGHLIGHT_BUTTON_X_OFFSET
                                && relX < HIGHLIGHT_BUTTON_X_OFFSET + HIGHLIGHT_BUTTON_SIZE) {
                            guiGraphics.renderTooltip(font,
                                    GuiText.ConfigurationTerminalHighlight.text(), x, y);
                            return;
                        }
                    }
                }
            }
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (slot instanceof ConfigSlot configSlot
                && (clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE)) {
            var carried = menu.getCarried();
            if (carried.isEmpty()) {
                menu.setConfigFilterFromClient(configSlot.getServerId(), configSlot.getConfigSlotIndex(), null, 0);
            } else {
                String itemId = BuiltInRegistries.ITEM.getKey(carried.getItem()).toString();
                int count = clickType == ClickType.QUICK_MOVE ? carried.getCount() : 1;
                menu.setConfigFilterFromClient(configSlot.getServerId(), configSlot.getConfigSlotIndex(), itemId,
                        count);
            }
            return;
        }
        super.slotClicked(slot, slotIdx, mouseButton, clickType);
    }

    public void clear() {
        this.byId.clear();
        this.cachedSearches.clear();
        this.refreshList();
    }

    public void postFullUpdate(long hostId, long sortBy, ConfigFilterGroup group, int configSize,
            Int2ObjectMap<GenericStack> slots, int capacityCards, boolean isInterface,
            int storageSize, Int2ObjectMap<GenericStack> storageSlots) {
        var record = byId.get(hostId);
        if (record == null) {
            record = new ConfigFilterRecord(hostId, configSize, sortBy, group, capacityCards, isInterface, storageSize);
            this.byId.put(hostId, record);
        }
        for (var entry : slots.int2ObjectEntrySet()) {
            record.getConfigInv().setStackFromPacket(entry.getIntKey(), entry.getValue());
        }
        if (record.getStorageInv() != null && storageSize > 0) {
            for (int i = 0; i < storageSize; i++) {
                GenericStack stack = storageSlots != null ? storageSlots.get(i) : null;
                record.getStorageInv().setStackFromPacket(i, stack);
            }
        }
        this.cachedSearches.clear();
        this.refreshList();
    }

    public void postIncrementalUpdate(long hostId, Int2ObjectMap<GenericStack> slots) {
        var record = byId.get(hostId);
        if (record == null) {
            LOGGER.warn("Ignoring incremental update for unknown host id {}", hostId);
            return;
        }
        for (var entry : slots.int2ObjectEntrySet()) {
            record.getConfigInv().setStackFromPacket(entry.getIntKey(), entry.getValue());
        }
    }

    private void refreshList() {
        String searchFilter = this.searchField.getValue().toLowerCase();
        Set<ConfigFilterRecord> cache = this.getCacheForSearchTerm(searchFilter);
        boolean rebuild = cache.isEmpty();

        records.clear();
        for (ConfigFilterRecord entry : this.byId.values()) {
            if (!rebuild && !cache.contains(entry)) {
                continue;
            }
            boolean found = searchFilter.isEmpty() || entry.getSearchName().contains(searchFilter);
            if (found) {
                records.add(entry);
                cache.add(entry);
            } else {
                cache.remove(entry);
            }
        }
        records.sort(RECORD_COMPARATOR);

        this.rows.clear();
        for (var record : this.records) {
            this.rows.add(new HeaderRow(record));
            var size = record.getConfigInv().size();
            for (int offset = 0; offset < size; offset += COLUMNS) {
                int slots = Math.min(size - offset, COLUMNS);
                this.rows.add(new SlotsRow(record, offset, slots));
            }
        }
        this.resetScrollbar();
    }

    private Set<ConfigFilterRecord> getCacheForSearchTerm(String searchTerm) {
        if (!this.cachedSearches.containsKey(searchTerm)) {
            this.cachedSearches.put(searchTerm, new HashSet<>());
        }
        Set<ConfigFilterRecord> cache = this.cachedSearches.get(searchTerm);
        if (cache.isEmpty() && searchTerm.length() > 1) {
            cache.addAll(this.getCacheForSearchTerm(searchTerm.substring(0, searchTerm.length() - 1)));
        }
        return cache;
    }

    private void resetScrollbar() {
        scrollbar.setHeight(this.visibleRows * ROW_HEIGHT - 2);
        scrollbar.setRange(0, Math.max(0, this.rows.size() - this.visibleRows), 2);
    }

    private void blit(GuiGraphics guiGraphics, int offsetX, int offsetY, Rect2i srcRect) {
        ResourceLocation texture = AppEng.makeId("textures/guis/patternaccessterminal.png"); // TODO: Make actual
                                                                                             // texture
        guiGraphics.blit(texture, offsetX, offsetY, srcRect.getX(), srcRect.getY(), srcRect.getWidth(),
                srcRect.getHeight());
    }

    sealed interface Row {
    }

    record HeaderRow(ConfigFilterRecord record) implements Row {
    }

    record SlotsRow(ConfigFilterRecord record, int offset, int slots) implements Row {
    }
}
