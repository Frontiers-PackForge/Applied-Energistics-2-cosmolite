package appeng.client.gui.me.config;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.GenericStack;
import appeng.helpers.externalstorage.GenericStackInv;

/**
 * Client-side config inventory that can be updated from packets without triggering listeners.
 */
public class ClientSyncableConfigInv extends GenericStackInv {

    public ClientSyncableConfigInv(int size, Mode mode) {
        super(null, mode, size);
    }

    /**
     * Update a slot from a packet without triggering onChange.
     */
    public void setStackFromPacket(int slot, @Nullable GenericStack stack) {
        if (slot >= 0 && slot < stacks.length) {
            stacks[slot] = stack;
        }
    }
}
