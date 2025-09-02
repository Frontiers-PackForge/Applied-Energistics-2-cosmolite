package appeng.api.crafting;

import appeng.api.config.CpuSelectionMode;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.stacks.GenericStack;
import appeng.menu.guisync.PacketWritable;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public interface ICPUSelectionListProvider {
    CraftingCpuList EMPTY_CPU_LIST = new CraftingCpuList(Collections.emptyList());
    CraftingCpuList cpuList = EMPTY_CPU_LIST;
    WeakHashMap<ICraftingCPU, Integer> cpuSerialMap = new WeakHashMap<>();

    void selectCpu(int serial);

    CraftingCpuList getCpuList();

    int getSelectedCpuSerial();

    Comparator<CraftingCpuListEntry> CPU_COMPARATOR = Comparator
            .comparing((CraftingCpuListEntry e) -> e.name() == null)
            .thenComparing(e -> e.name() != null ? e.name().getString() : "")
            .thenComparingInt(CraftingCpuListEntry::serial);

    default int getOrAssignCpuSerial(ICraftingCPU cpu) {
        return cpuSerialMap.computeIfAbsent(cpu, ignored -> incrNextCpuSerial());
    }

    int incrNextCpuSerial();

    default CraftingCpuList createCpuList() {
        var entries = new ArrayList<CraftingCpuListEntry>(getLastCpuSet().size());
        for (var cpu : getLastCpuSet()) {
            var serial = getOrAssignCpuSerial(cpu);
            var status = cpu.getJobStatus();
            var progress = 0f;
            if (status != null && status.totalItems() > 0) {
                progress = (float) (status.progress() / (double) status.totalItems());
            }
            entries.add(new CraftingCpuListEntry(
                    serial,
                    cpu.getAvailableStorage(),
                    cpu.getCoProcessors(),
                    cpu.getName(),
                    cpu.getSelectionMode(),
                    status != null ? status.crafting() : null,
                    progress,
                    status != null ? status.elapsedTimeNanos() : 0));
        }
        entries.sort(CPU_COMPARATOR);
        return new CraftingCpuList(entries);
    }

    ImmutableSet<ICraftingCPU> getLastCpuSet();

    record CraftingCpuList(List<CraftingCpuListEntry> cpus) implements PacketWritable {
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

    record CraftingCpuListEntry(
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
}
