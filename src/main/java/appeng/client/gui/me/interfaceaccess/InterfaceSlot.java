package appeng.client.gui.me.interfaceaccess;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.menu.slot.AppEngSlot;

class InterfaceSlot extends AppEngSlot {
    private final long serverId;
    private final int serverSlot;

    public InterfaceSlot(InterfaceAccessTermScreen.SimpleInventory inv, int invSlot, long serverId, int serverSlot,
            int x, int y) {
        super(inv, invSlot);
        this.serverId = serverId;
        this.serverSlot = serverSlot;
        this.x = x;
        this.y = y;
    }

    public long getServerId() {
        return serverId;
    }

    public int getServerSlot() {
        return serverSlot;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }
}
