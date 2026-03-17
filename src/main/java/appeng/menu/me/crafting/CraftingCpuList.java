package appeng.menu.me.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.FriendlyByteBuf;

import appeng.menu.guisync.PacketWritable;

public record CraftingCpuList(List<CraftingCpuListEntry> cpus) implements PacketWritable {
    public CraftingCpuList(FriendlyByteBuf data) {
        this(readFromPacket(data));
    }

    private static List<CraftingCpuListEntry> readFromPacket(FriendlyByteBuf data) {
        var count = data.readInt();
        var result = new ArrayList<CraftingCpuListEntry>(count);
        for (int i = 0; i < count; i++) {
            result.add(CraftingCpuListEntry.readFromPacket(data));
        }
        return result;
    }

    @Override
    public void writeToPacket(FriendlyByteBuf data) {
        data.writeInt(cpus.size());
        for (var entry : cpus) {
            entry.writeToPacket(data);
        }
    }
}
