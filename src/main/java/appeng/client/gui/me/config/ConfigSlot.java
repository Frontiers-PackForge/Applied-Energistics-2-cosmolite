package appeng.client.gui.me.config;

import appeng.menu.slot.FakeSlot;

/**
 * Slot used in the configuration terminal to interact with a filter host's config inventory.
 * Interactions are sent to the server via InventoryActionPacket with config slot index and host id.
 */
public class ConfigSlot extends FakeSlot {

    private final ConfigFilterRecord record;
    private final int configSlotIndex;

    public ConfigSlot(ConfigFilterRecord record, int configSlotIndex, int x, int y) {
        super(record.getConfigWrapper(), configSlotIndex);
        this.record = record;
        this.configSlotIndex = configSlotIndex;
        this.x = x;
        this.y = y;
        this.setSlotEnabled(record.isSlotEnabled(configSlotIndex));
    }

    public ConfigFilterRecord getRecord() {
        return record;
    }

    public long getServerId() {
        return record.getServerId();
    }

    public int getConfigSlotIndex() {
        return configSlotIndex;
    }
}
