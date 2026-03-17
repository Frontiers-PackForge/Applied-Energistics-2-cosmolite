package appeng.menu.me.crafting;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import appeng.api.config.CpuSelectionMode;
import appeng.api.stacks.GenericStack;

public record CraftingCpuListEntry(
        int serial,
        long storage,
        int coProcessors,
        Component name,
        CpuSelectionMode mode,
        GenericStack currentJob,
        float progress,
        long elapsedTimeNanos) {
    public static CraftingCpuListEntry readFromPacket(FriendlyByteBuf data) {
        return new CraftingCpuListEntry(
                data.readInt(),
                data.readLong(),
                data.readInt(),
                data.readBoolean() ? data.readComponent() : null,
                data.readEnum(CpuSelectionMode.class),
                GenericStack.readBuffer(data),
                data.readFloat(),
                data.readVarLong());
    }

    public void writeToPacket(FriendlyByteBuf data) {
        data.writeInt(serial);
        data.writeLong(storage);
        data.writeInt(coProcessors);
        data.writeBoolean(name != null);
        if (name != null) {
            data.writeComponent(name);
        }
        data.writeEnum(mode);
        GenericStack.writeBuffer(currentJob, data);
        data.writeFloat(progress);
        data.writeVarLong(elapsedTimeNanos);
    }
}
