package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import appeng.client.gui.me.interfaceaccess.InterfaceAccessTermScreen;
import appeng.core.sync.BasePacket;

public class InterfaceAccessTerminalPacket extends BasePacket {
    private boolean fullUpdate;
    private long inventoryId;
    private int configSize;
    private int storageSize;
    private ItemStack icon;
    private Int2ObjectMap<ItemStack> configSlots;
    private Int2ObjectMap<ItemStack> storageSlots;

    public InterfaceAccessTerminalPacket(FriendlyByteBuf stream) {
        inventoryId = stream.readVarLong();
        fullUpdate = stream.readBoolean();
        if (fullUpdate) {
            configSize = stream.readVarInt();
            storageSize = stream.readVarInt();
            icon = stream.readItem();
        }
        int c = stream.readVarInt();
        configSlots = new Int2ObjectArrayMap<>(c);
        for (int i = 0; i < c; i++) {
            int slot = stream.readVarInt();
            configSlots.put(slot, stream.readItem());
        }
        int s = stream.readVarInt();
        storageSlots = new Int2ObjectArrayMap<>(s);
        for (int i = 0; i < s; i++) {
            int slot = stream.readVarInt();
            storageSlots.put(slot, stream.readItem());
        }
    }

    private InterfaceAccessTerminalPacket(boolean fullUpdate,
            long inventoryId,
            int configSize,
            int storageSize,
            ItemStack icon,
            Int2ObjectMap<ItemStack> configSlots,
            Int2ObjectMap<ItemStack> storageSlots) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(2048));
        data.writeInt(this.getPacketID());
        data.writeVarLong(inventoryId);
        data.writeBoolean(fullUpdate);
        if (fullUpdate) {
            data.writeVarInt(configSize);
            data.writeVarInt(storageSize);
            data.writeItem(icon);
        }
        data.writeVarInt(configSlots.size());
        for (var e : configSlots.int2ObjectEntrySet()) {
            data.writeVarInt(e.getIntKey());
            data.writeItem(e.getValue());
        }
        data.writeVarInt(storageSlots.size());
        for (var e : storageSlots.int2ObjectEntrySet()) {
            data.writeVarInt(e.getIntKey());
            data.writeItem(e.getValue());
        }
        this.configureWrite(data);
    }

    public static InterfaceAccessTerminalPacket fullUpdate(long id, int configSize, int storageSize, ItemStack icon,
            Int2ObjectMap<ItemStack> config, Int2ObjectMap<ItemStack> storage) {
        return new InterfaceAccessTerminalPacket(true, id, configSize, storageSize, icon, config, storage);
    }

    public static InterfaceAccessTerminalPacket incrementalUpdate(long id,
            Int2ObjectMap<ItemStack> config, Int2ObjectMap<ItemStack> storage) {
        return new InterfaceAccessTerminalPacket(false, id, 0, 0, ItemStack.EMPTY, config, storage);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        var screen = Minecraft.getInstance().screen;
        if (screen instanceof InterfaceAccessTermScreen<?> s) {
            if (fullUpdate) {
                s.postFullUpdate(inventoryId, icon, configSize, storageSize, configSlots, storageSlots);
            } else {
                s.postIncrementalUpdate(inventoryId, configSlots, storageSlots);
            }
        }
    }
}
