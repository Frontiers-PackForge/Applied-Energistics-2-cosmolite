package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import appeng.api.implementations.blockentities.ConfigFilterGroup;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.config.ConfigTerminalScreen;
import appeng.core.sync.BasePacket;

/**
 * Sends the content for a single filter host shown in the configuration terminal.
 */
public class ConfigurationTerminalPacket extends BasePacket {

    private boolean fullUpdate;
    private long hostId;
    private int configSize;
    private long sortBy;
    private ConfigFilterGroup group;
    private Int2ObjectMap<GenericStack> slots;
    private int capacityCards;
    private boolean isInterface;
    private int storageSize;
    private Int2ObjectMap<GenericStack> storageSlots;

    public ConfigurationTerminalPacket(FriendlyByteBuf stream) {
        hostId = stream.readVarLong();
        fullUpdate = stream.readBoolean();
        if (fullUpdate) {
            configSize = stream.readVarInt();
            sortBy = stream.readVarLong();
            group = ConfigFilterGroup.readFromPacket(stream);
            capacityCards = stream.readVarInt();
            isInterface = stream.readBoolean();
            if (isInterface) {
                storageSize = stream.readVarInt();
                int storageCount = stream.readVarInt();
                storageSlots = new Int2ObjectArrayMap<>(storageCount);
                for (int i = 0; i < storageCount; i++) {
                    int slot = stream.readVarInt();
                    var stack = GenericStack.readBuffer(stream);
                    if (stack != null) {
                        storageSlots.put(slot, stack);
                    }
                }
            } else {
                storageSize = 0;
                storageSlots = new Int2ObjectArrayMap<>(0);
            }
        }
        int slotsCount = stream.readVarInt();
        slots = new Int2ObjectArrayMap<>(slotsCount);
        for (int i = 0; i < slotsCount; i++) {
            int slot = stream.readVarInt();
            var stack = GenericStack.readBuffer(stream);
            if (stack != null) {
                slots.put(slot, stack);
            }
        }
    }

    private ConfigurationTerminalPacket(boolean fullUpdate, long hostId, int configSize, long sortBy,
            ConfigFilterGroup group, Int2ObjectMap<GenericStack> slots, int capacityCards, boolean isInterface,
            int storageSize, Int2ObjectMap<GenericStack> storageSlots) {
        this.fullUpdate = fullUpdate;
        this.hostId = hostId;
        this.configSize = configSize;
        this.sortBy = sortBy;
        this.group = group;
        this.slots = slots;
        this.capacityCards = capacityCards;
        this.isInterface = isInterface;
        this.storageSize = storageSize;
        this.storageSlots = storageSlots != null ? storageSlots : new Int2ObjectArrayMap<>(0);
        var data = new FriendlyByteBuf(Unpooled.buffer(2048));
        data.writeInt(this.getPacketID());
        data.writeVarLong(hostId);
        data.writeBoolean(fullUpdate);
        if (fullUpdate) {
            data.writeVarInt(configSize);
            data.writeVarLong(sortBy);
            group.writeToPacket(data);
            data.writeVarInt(capacityCards);
            data.writeBoolean(isInterface);
            if (isInterface) {
                data.writeVarInt(storageSize);
                data.writeVarInt(this.storageSlots.size());
                for (var entry : this.storageSlots.int2ObjectEntrySet()) {
                    data.writeVarInt(entry.getIntKey());
                    GenericStack.writeBuffer(entry.getValue(), data);
                }
            }
        }
        data.writeVarInt(slots.size());
        for (var entry : slots.int2ObjectEntrySet()) {
            data.writeVarInt(entry.getIntKey());
            GenericStack.writeBuffer(entry.getValue(), data);
        }
        this.configureWrite(data);
    }

    public static ConfigurationTerminalPacket fullUpdate(long hostId, int configSize, long sortBy,
            ConfigFilterGroup group, Int2ObjectMap<GenericStack> slots, int capacityCards, boolean isInterface,
            int storageSize, Int2ObjectMap<GenericStack> storageSlots) {
        return new ConfigurationTerminalPacket(true, hostId, configSize, sortBy, group, slots, capacityCards,
                isInterface, storageSize, storageSlots);
    }

    public static ConfigurationTerminalPacket incrementalUpdate(long hostId, Int2ObjectMap<GenericStack> slots) {
        return new ConfigurationTerminalPacket(false, hostId, 0, 0, null, slots, 0, false, 0, null);
    }

    public int getStorageSize() {
        return storageSize;
    }

    public int getCapacityCards() {
        return capacityCards;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public Int2ObjectMap<GenericStack> getStorageSlots() {
        return storageSlots != null ? storageSlots : new Int2ObjectArrayMap<>(0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        if (Minecraft.getInstance().screen instanceof ConfigTerminalScreen<?> screen) {
            if (fullUpdate) {
                screen.postFullUpdate(hostId, sortBy, group, configSize, slots, capacityCards, isInterface,
                        getStorageSize(), getStorageSlots());
            } else {
                screen.postIncrementalUpdate(hostId, slots);
            }
        }
    }
}
