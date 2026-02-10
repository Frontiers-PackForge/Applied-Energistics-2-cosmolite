package appeng.menu.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import appeng.api.implementations.blockentities.ConfigFilterGroup;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.parts.IPart;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.util.DimensionalBlockPos;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.client.render.BlockHighlightHandler;
import appeng.core.definitions.AEItems;
import appeng.core.sync.packets.BlockHighlightPacket;
import appeng.core.sync.packets.ClearConfigurationTerminalPacket;
import appeng.core.sync.packets.ConfigurationTerminalPacket;
import appeng.helpers.InterfaceLogicHost;
import appeng.helpers.externalstorage.GenericStackInv;
import appeng.menu.AEBaseMenu;
import appeng.parts.AEBasePart;
import appeng.parts.automation.ExportBusPart;
import appeng.parts.automation.FormationPlanePart;
import appeng.parts.automation.IOBusPart;
import appeng.parts.automation.ImportBusPart;
import appeng.parts.automation.StorageLevelEmitterPart;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.misc.InterfacePart;
import appeng.parts.reporting.ConfigurationTerminalPart;
import appeng.parts.storagebus.StorageBusPart;
import appeng.util.ConfigMenuInventory;

/**
 * Menu for the Configuration Terminal. Lists all filter-configurable hosts on the grid (storage busses, import/export
 * busses, interfaces, formation planes, level emitters) and allows editing their config and highlighting them in-world.
 * 
 * @see appeng.client.gui.me.config.ConfigTerminalScreen
 */
public class ConfigurationTerminalMenu extends AEBaseMenu {
    public static final String ACTION_SET_CONFIG_FILTER = "setConfigFilter";
    public static final String ACTION_SET_CONFIG_AMOUNT = "setConfigAmount";

    public static final MenuType<ConfigurationTerminalMenu> TYPE = MenuTypeBuilder
            .create(ConfigurationTerminalMenu::new, ConfigurationTerminalPart.class)
            .build("configuration_terminal");

    private static long hostSerial = Long.MIN_VALUE;
    private final Map<Object, HostTracker> hostMap = new IdentityHashMap<>();
    private final Long2ObjectOpenHashMap<HostTracker> byId = new Long2ObjectOpenHashMap<>();

    public ConfigurationTerminalMenu(int id, Inventory playerInventory, ConfigurationTerminalPart anchor) {
        super(TYPE, id, playerInventory, anchor);
        this.createPlayerInventorySlots(playerInventory);

        registerClientAction(ACTION_SET_CONFIG_FILTER, SetConfigFilterPayload.class, this::handleSetConfigFilter);
        registerClientAction(ACTION_SET_CONFIG_AMOUNT, SetConfigAmountPayload.class, this::handleSetConfigAmount);
    }

    @Nullable
    private IGrid getGrid() {
        IActionHost host = getActionHost();
        if (host != null) {
            IGridNode node = host.getActionableNode();
            if (node != null && node.isActive()) {
                return node.getGrid();
            }
        }
        return null;
    }

    private static ConfigFilterGroup groupForPart(IPart part) {
        var item = part.getPartItem();
        var icon = AEItemKey.of(item);
        var name = new ItemStack(item).getHoverName();
        return new ConfigFilterGroup(icon, name);
    }

    private static ConfigFilterGroup groupForBlockEntity(InterfaceBlockEntity blockEntity) {
        var block = blockEntity.getBlockState().getBlock();
        var icon = AEItemKey.of(block.asItem());
        var name = block.getName();
        return new ConfigFilterGroup(icon, name);
    }

    private static int getCapacityCards(Object machine) {
        if (machine instanceof UpgradeablePart up) {
            return up.getInstalledUpgrades(AEItems.CAPACITY_CARD);
        }
        return 5; // show all slots for non-upgradeable (e.g. interface, level emitter)
    }

    @Override
    public void broadcastChanges() {
        if (isClientSide()) {
            super.broadcastChanges();
            return;
        }
        super.broadcastChanges();

        IGrid grid = getGrid();
        List<HostEntry> currentHosts = new ArrayList<>();
        if (grid != null) {
            collectPartHosts(grid, currentHosts, StorageBusPart.class, StorageBusPart::getConfig, false);
            collectPartHosts(grid, currentHosts, ImportBusPart.class, IOBusPart::getConfig, false);
            collectPartHosts(grid, currentHosts, ExportBusPart.class, IOBusPart::getConfig, false);
            collectPartHosts(grid, currentHosts, FormationPlanePart.class, FormationPlanePart::getConfig, false);
            collectPartHosts(grid, currentHosts, StorageLevelEmitterPart.class, StorageLevelEmitterPart::getConfig,
                    false);
            collectPartHosts(grid, currentHosts, InterfacePart.class, InterfaceLogicHost::getConfig, true);
            collectBlockInterfaceHosts(grid, currentHosts);
        }

        boolean forceFull = currentHosts.size() != hostMap.size()
                || !currentHosts.stream().allMatch(e -> hostMap.containsKey(e.machine));
        if (forceFull) {
            sendFullUpdate(currentHosts);
        } else {
            sendIncrementalUpdate();
        }
    }

    private record HostEntry(Object machine, GenericStackInv config, DimensionalBlockPos location,
            ConfigFilterGroup group, int capacityCards, boolean isInterface, GenericStackInv storage) {
    }

    private <T> void collectPartHosts(IGrid grid, List<HostEntry> out, Class<T> clazz,
            Function<T, GenericStackInv> configGetter, boolean isInterface) {
        for (T machine : grid.getActiveMachines(clazz)) {
            if (machine instanceof IPart part && part instanceof AEBasePart basePart) {
                GenericStackInv config = configGetter.apply(machine);
                DimensionalBlockPos location = basePart.getHost().getLocation();
                int capacity = getCapacityCards(machine);
                GenericStackInv storage = isInterface && machine instanceof InterfaceLogicHost ih
                        ? ih.getInterfaceLogic().getStorage()
                        : null;
                out.add(new HostEntry(machine, config, location, groupForPart(part), capacity, isInterface, storage));
            }
        }
    }

    private void collectBlockInterfaceHosts(IGrid grid, List<HostEntry> out) {
        for (InterfaceBlockEntity blockEntity : grid.getActiveMachines(InterfaceBlockEntity.class)) {
            var logic = blockEntity.getInterfaceLogic();
            var config = logic.getConfig();
            var storage = logic.getStorage();
            var location = new DimensionalBlockPos(blockEntity.getLevel(), blockEntity.getBlockPos());
            var group = groupForBlockEntity(blockEntity);
            out.add(new HostEntry(blockEntity, config, location, group, 5, true, storage));
        }
    }

    private void sendFullUpdate(List<HostEntry> entries) {
        byId.clear();
        hostMap.clear();
        sendPacketToClient(new ClearConfigurationTerminalPacket());
        for (HostEntry entry : entries) {
            var tracker = new HostTracker(entry);
            tracker.hostId = hostSerial++;
            hostMap.put(entry.machine(), tracker);
            byId.put(tracker.hostId, tracker);
            sendPacketToClient(tracker.createFullPacket());
        }
    }

    private void sendIncrementalUpdate() {
        for (HostTracker t : hostMap.values()) {
            var packet = t.createUpdatePacket();
            if (packet != null) {
                sendPacketToClient(packet);
            }
        }
    }

    public void handleHighlightRequest(long hostId) {
        HostTracker tracker = byId.get(hostId);
        if (tracker == null) {
            return;
        }
        var dimension = tracker.location.getLevel().dimension();
        var positions = Collections.singletonList(tracker.location.getPos());
        for (var position : positions) {
            var packet = new BlockHighlightPacket(
                    position, dimension,
                    BlockHighlightHandler.getTime(position, this.getPlayer().getOnPos())); // TODO maybe bulk send all
                                                                                           // in a list
            sendPacketToClient(packet);
        }
    }

    private static class HostTracker {
        final GenericStackInv config;
        final DimensionalBlockPos location;
        final ConfigFilterGroup group;
        final int capacityCards;
        final boolean isInterface;
        final GenericStackInv storage;
        long hostId;
        final GenericStack[] clientCopy;

        HostTracker(HostEntry entry) {
            this.config = entry.config();
            this.location = entry.location();
            this.group = entry.group();
            this.capacityCards = entry.capacityCards();
            this.isInterface = entry.isInterface();
            this.storage = entry.storage();
            this.clientCopy = new GenericStack[config.size()];
            for (int i = 0; i < config.size(); i++) {
                this.clientCopy[i] = config.getStack(i);
            }
        }

        ConfigurationTerminalPacket createFullPacket() {
            var slots = new Int2ObjectArrayMap<GenericStack>(config.size());
            for (int i = 0; i < config.size(); i++) {
                var stack = config.getStack(i);
                if (stack != null) {
                    slots.put(i, stack);
                    clientCopy[i] = stack;
                }
            }
            int storageCapacity = 0;
            Int2ObjectArrayMap<GenericStack> storageMap = new Int2ObjectArrayMap<>(0);
            if (isInterface && storage != null) {
                storageCapacity = storage.size();
                for (int i = 0; i < storage.size(); i++) {
                    var stack = storage.getStack(i);
                    if (stack != null) {
                        storageMap.put(i, stack);
                    }
                }
            }
            return ConfigurationTerminalPacket.fullUpdate(hostId, config.size(), 0, group, slots, capacityCards,
                    isInterface, storageCapacity, storageMap);
        }

        @Nullable
        ConfigurationTerminalPacket createUpdatePacket() {
            IntList changed = null;
            for (int i = 0; i < config.size(); i++) {
                var current = config.getStack(i);
                if (!java.util.Objects.equals(current, clientCopy[i])) {
                    if (changed == null) {
                        changed = new IntArrayList();
                    }
                    changed.add(i);
                    clientCopy[i] = current;
                }
            }
            if (changed == null) {
                return null;
            }
            var slots = new Int2ObjectArrayMap<GenericStack>(changed.size());
            for (int i = 0; i < changed.size(); i++) {
                int slot = changed.getInt(i);
                var stack = config.getStack(slot);
                slots.put(slot, stack);
            }
            return ConfigurationTerminalPacket.incrementalUpdate(hostId, slots);
        }

        boolean isSlotEnabled(int slotIndex) {
            if (slotIndex < 18) {
                return true;
            }
            int optionalRow = slotIndex / 9 - 2;
            return capacityCards > optionalRow;
        }
    }

    public void handleSetConfigFilter(SetConfigFilterPayload p) {
        if (isClientSide() || p == null) {
            sendClientAction(ACTION_SET_CONFIG_FILTER, p);
            return;
        }
        HostTracker tracker = byId.get(p.hostId);
        if (tracker == null || p.slotIndex < 0 || p.slotIndex >= tracker.config.size()) {
            return;
        }
        if (!tracker.isSlotEnabled(p.slotIndex)) {
            return;
        }
        var configWrapper = new ConfigMenuInventory(tracker.config);
        if (p.itemId == null || p.itemId.isEmpty() || p.count <= 0) {
            configWrapper.setItemDirect(p.slotIndex, ItemStack.EMPTY);
            return;
        }
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(p.itemId));
        if (item == Items.AIR) {
            return;
        }
        ItemStack stack = new ItemStack(item, Math.max(1, Math.min(p.count, item.getMaxStackSize())));
        var converted = configWrapper.convertToSuitableStack(stack);
        if (converted != null) {
            stack.setCount(Math.min(stack.getCount(), (int) converted.amount()));
            configWrapper.setItemDirect(p.slotIndex, stack);
        }
    }

    public void handleSetConfigAmount(SetConfigAmountPayload p) {
        if (isClientSide() || p == null) {
            sendClientAction(ACTION_SET_CONFIG_AMOUNT, p);
            return;
        }
        HostTracker tracker = byId.get(p.hostId);
        if (tracker == null || !tracker.isInterface || p.configSlot < 0
                || p.configSlot >= tracker.config.size() || p.amount < 0) {
            return;
        }
        var current = tracker.config.getStack(p.configSlot);
        if (current != null) {
            tracker.config.setStack(p.configSlot, new GenericStack(current.what(), p.amount));
        }
    }

    /**
     * Single-argument payload for setConfigFilter itemId null or empty = clear slot; otherwise item registry id and
     * count.
     */
    public static final class SetConfigFilterPayload {
        public long hostId;
        public int slotIndex;
        public String itemId;
        public int count;
    }

    /**
     * Single-argument payload for setConfigAmount
     */
    public static final class SetConfigAmountPayload {
        public long hostId;
        public int configSlot;
        public long amount;
    }

    /** Called from the client screen to set a filter slot (fake-only; does not consume carried item). */
    public void setConfigFilterFromClient(long hostId, int slotIndex, String itemId, int count) {
        if (!isClientSide()) {
            return;
        }
        var p = new SetConfigFilterPayload();
        p.hostId = hostId;
        p.slotIndex = slotIndex;
        p.itemId = itemId;
        p.count = count;
        sendClientAction(ACTION_SET_CONFIG_FILTER, p);
    }
}
