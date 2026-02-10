package appeng.client.gui.me.config;

import appeng.api.implementations.blockentities.ConfigFilterGroup;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.util.ConfigMenuInventory;

/**
 * Client-side record for a filter host in the configuration terminal.
 */
public class ConfigFilterRecord implements Comparable<ConfigFilterRecord> {

    private final long serverId;
    private final ConfigFilterGroup group;
    private final String searchName;
    private final long order;
    private final ClientSyncableConfigInv configInv;
    private final int capacityCards;
    private final boolean isInterface;
    private final ClientSyncableConfigInv storageInv;

    public ConfigFilterRecord(long serverId, int configSize, long order, ConfigFilterGroup group,
                              int capacityCards, boolean isInterface, int storageSize) {
        this.serverId = serverId;
        this.group = group;
        this.searchName = group.name().getString().toLowerCase();
        this.order = order;
        this.capacityCards = capacityCards;
        this.isInterface = isInterface;
        this.configInv = new ClientSyncableConfigInv(configSize, isInterface ? GenericStackInv.Mode.CONFIG_STACKS : GenericStackInv.Mode.CONFIG_TYPES);
        this.storageInv = storageSize > 0 ? new ClientSyncableConfigInv(storageSize, GenericStackInv.Mode.STORAGE) : null;
    }

    public ConfigFilterGroup getGroup() {
        return group;
    }

    public String getSearchName() {
        return searchName;
    }

    public long getServerId() {
        return serverId;
    }

    public ClientSyncableConfigInv getConfigInv() {
        return configInv;
    }

    public ConfigMenuInventory getConfigWrapper() {
        return configInv.createMenuWrapper();
    }

    public boolean isSlotEnabled(int slotIndex) {
        if (slotIndex < 18) {
            return true;
        }
        int optionalRow = slotIndex / 9 - 2;
        return capacityCards > optionalRow;
    }

    public int getCapacityCards() {
        return capacityCards;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public ClientSyncableConfigInv getStorageInv() {
        return storageInv;
    }

    public ConfigMenuInventory getStorageWrapper() {
        return storageInv != null ? storageInv.createMenuWrapper() : null;
    }

    @Override
    public int compareTo(ConfigFilterRecord o) {
        return Long.compare(this.order, o.order);
    }
}
