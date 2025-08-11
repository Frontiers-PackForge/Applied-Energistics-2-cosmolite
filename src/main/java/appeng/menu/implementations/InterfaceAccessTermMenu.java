/*
 * This file is part of Applied Energistics 2.
 */
package appeng.menu.implementations;

import java.util.IdentityHashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import appeng.api.config.Settings;
import appeng.api.config.TerminalStyle;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.IConfigurableObject;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.core.sync.packets.ClearInterfaceAccessTerminalPacket;
import appeng.core.sync.packets.InterfaceAccessTerminalPacket;
import appeng.helpers.InterfaceLogicHost;
import appeng.helpers.InventoryAction;
import appeng.menu.AEBaseMenu;
import appeng.menu.guisync.GuiSync;
import appeng.menu.locator.MenuLocators;
import appeng.parts.misc.InterfacePart;
import appeng.parts.reporting.InterfaceAccessTerminalPart;
import appeng.util.inv.AppEngInternalInventory;

public class InterfaceAccessTermMenu extends AEBaseMenu {
    public static final MenuType<InterfaceAccessTermMenu> TYPE = MenuTypeBuilder
            .create(InterfaceAccessTermMenu::new, InterfaceAccessTerminalPart.class)
            .build("interfaceaccessterminal");

    private final IConfigurableObject host;

    @GuiSync(1)
    public TerminalStyle terminalStyle = TerminalStyle.MEDIUM;

    private static long inventorySerial = Long.MIN_VALUE;
    private final Map<InterfaceLogicHost, ContainerTracker> byHost = new IdentityHashMap<>();
    private final Long2ObjectOpenHashMap<ContainerTracker> byId = new Long2ObjectOpenHashMap<>();

    public InterfaceAccessTermMenu(int id, Inventory ip, InterfaceAccessTerminalPart anchor) {
        this(TYPE, id, ip, anchor, true);
    }

    public InterfaceAccessTermMenu(MenuType<?> menuType, int id, Inventory ip, IConfigurableObject host,
            boolean bindInventory) {
        super(menuType, id, ip, host);
        this.host = host;
        if (bindInventory) {
            this.createPlayerInventorySlots(ip);
        }
    }

    @Override
    public void broadcastChanges() {
        if (isClientSide()) {
            return;
        }

        terminalStyle = this.host.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
        super.broadcastChanges();

        var grid = getGrid();
        boolean forceFull = false;
        if (grid == null) {
            if (!byHost.isEmpty()) {
                byHost.clear();
                byId.clear();
            }
            return;
        }

        // Collect all Interface hosts visible in the terminal
        var newMap = new IdentityHashMap<InterfaceLogicHost, ContainerTracker>();
        for (var machineClass : grid.getMachineClasses()) {
            if (InterfaceBlockEntity.class.isAssignableFrom(machineClass)) {
                for (Object obj : grid.getActiveMachines(machineClass.asSubclass(InterfaceBlockEntity.class))) {
                    var be = (InterfaceBlockEntity) obj;
                    if (be.isVisibleInInterfaceTerminal()) {
                        var existing = byHost.get(be);
                        if (existing == null)
                            forceFull = true;
                        newMap.put(be, existing == null ? new ContainerTracker(be) : existing);
                    }
                }
            } else if (InterfacePart.class.isAssignableFrom(machineClass)) {
                for (var obj : grid.getActiveMachines(machineClass.asSubclass(InterfacePart.class))) {
                    var part = (InterfacePart) obj;
                    if (part.isVisibleInInterfaceTerminal()) {
                        var existing = byHost.get(part);
                        if (existing == null)
                            forceFull = true;
                        newMap.put(part, existing == null ? new ContainerTracker(part) : existing);
                    }
                }
            }
        }

        if (forceFull || newMap.size() != byHost.size()) {
            // Full resend
            byHost.clear();
            byId.clear();
            byHost.putAll(newMap);
            sendPacketToClient(new ClearInterfaceAccessTerminalPacket());
            for (var ct : byHost.values()) {
                byId.put(ct.serverId, ct);
                sendPacketToClient(ct.createFullPacket());
            }
        } else {
            for (var ct : byHost.values()) {
                var pkt = ct.createUpdatePacket();
                if (pkt != null)
                    sendPacketToClient(pkt);
            }
        }
    }

    @Override
    public void doAction(ServerPlayer player, InventoryAction action, int slot, long id) {
        var ct = byId.get(id);
        if (ct == null)
            return;
        ct.handleAction(this, player, action, slot);
    }

    @Nullable
    private IGrid getGrid() {
        IActionHost host = this.getActionHost();
        if (host != null) {
            final IGridNode agn = host.getActionableNode();
            if (agn != null && agn.isActive()) {
                return agn.getGrid();
            }
        }
        return null;
    }

    private static class ContainerTracker {
        private final InterfaceLogicHost host;
        private final long serverId = inventorySerial++;
        private final InternalInventory configServer;
        private final InternalInventory storageServer;
        private final InternalInventory configClient;
        private final InternalInventory storageClient;

        ContainerTracker(InterfaceLogicHost host) {
            this.host = host;
            this.configServer = host.getConfig().createMenuWrapper();
            this.storageServer = host.getStorage().createMenuWrapper();
            this.configClient = new AppEngInternalInventory(configServer.size());
            this.storageClient = new AppEngInternalInventory(storageServer.size());
        }

        public InterfaceAccessTerminalPacket createFullPacket() {
            var config = new Int2ObjectArrayMap<ItemStack>(configServer.size());
            for (int i = 0; i < configServer.size(); i++) {
                var st = configServer.getStackInSlot(i);
                if (!st.isEmpty())
                    config.put(i, st);
            }
            var storage = new Int2ObjectArrayMap<ItemStack>(storageServer.size());
            for (int i = 0; i < storageServer.size(); i++) {
                var st = storageServer.getStackInSlot(i);
                if (!st.isEmpty())
                    storage.put(i, st);
            }
            return InterfaceAccessTerminalPacket.fullUpdate(serverId,
                    configServer.size(), storageServer.size(), host.getMainMenuIcon(), config, storage);
        }

        @Nullable
        public InterfaceAccessTerminalPacket createUpdatePacket() {
            Int2ObjectMap<ItemStack> config = detectChanges(configServer, configClient);
            Int2ObjectMap<ItemStack> storage = detectChanges(storageServer, storageClient);
            if ((config == null || config.isEmpty()) && (storage == null || storage.isEmpty()))
                return null;
            if (config == null)
                config = new Int2ObjectArrayMap<>();
            if (storage == null)
                storage = new Int2ObjectArrayMap<>();
            return InterfaceAccessTerminalPacket.incrementalUpdate(serverId, config, storage);
        }

        private static Int2ObjectMap<ItemStack> detectChanges(InternalInventory server, InternalInventory client) {
            Int2ObjectMap<ItemStack> changed = null;
            for (int i = 0; i < server.size(); i++) {
                var a = server.getStackInSlot(i);
                var b = client.getStackInSlot(i);
                if (!ItemStack.matches(a, b)) {
                    if (changed == null)
                        changed = new Int2ObjectArrayMap<>();
                    client.setItemDirect(i, a.isEmpty() ? ItemStack.EMPTY : a.copy());
                    changed.put(i, a);
                }
            }
            return changed;
        }

        public long getServerId() {
            return serverId;
        }

        public void handleAction(InterfaceAccessTermMenu menu, ServerPlayer player,
                InventoryAction action, int slot) {
            // Combined slot space: first config, then storage
            boolean isConfig = slot < configServer.size();
            var inv = isConfig ? configServer : storageServer;
            int localSlot = isConfig ? slot : (slot - configServer.size());
            var carried = menu.getCarried();
            switch (action) {
                case PICKUP_OR_SET_DOWN -> {
                    if (!carried.isEmpty()) {
                        if (isConfig) {
                            // Do not consume from player, target the chosen slot only
                            var slotInv = inv.getSlotInv(localSlot);
                            slotInv.addItems(carried.copy());
                            // Leave carried unchanged
                        } else {
                            var inSlot = inv.getStackInSlot(localSlot);
                            if (inSlot.isEmpty()) {
                                menu.setCarried(inv.addItems(carried));
                            } else {
                                var inHand = carried.copy();
                                inv.setItemDirect(localSlot, ItemStack.EMPTY);
                                menu.setCarried(ItemStack.EMPTY);
                                menu.setCarried(inv.addItems(inHand.copy()));
                                if (menu.getCarried().isEmpty()) {
                                    menu.setCarried(inSlot);
                                } else {
                                    menu.setCarried(inHand);
                                    inv.setItemDirect(localSlot, inSlot);
                                }
                            }
                        }
                    } else {
                        if (isConfig) {
                            // Clear the filter slot without picking up the item
                            inv.setItemDirect(localSlot, ItemStack.EMPTY);
                        } else {
                            menu.setCarried(inv.getStackInSlot(localSlot));
                            inv.setItemDirect(localSlot, ItemStack.EMPTY);
                        }
                    }
                }
                case SPLIT_OR_PLACE_SINGLE -> {
                    if (!carried.isEmpty()) {
                        if (isConfig) {
                            // Place exactly 1 into chosen slot without consuming from hand
                            var one = carried.copy();
                            one.setCount(1);
                            inv.getSlotInv(localSlot).addItems(one);
                        } else {
                            ItemStack extra = carried.split(1);
                            if (!extra.isEmpty()) {
                                extra = inv.addItems(extra);
                            }
                            if (!extra.isEmpty()) {
                                carried.grow(extra.getCount());
                            }
                        }
                    } else {
                        if (isConfig) {
                            // Clear the filter slot without picking up the item
                            inv.setItemDirect(localSlot, ItemStack.EMPTY);
                        } else {
                            var is = inv.getStackInSlot(localSlot);
                            if (!is.isEmpty()) {
                                menu.setCarried(inv.extractItem(localSlot, (is.getCount() + 1) / 2, false));
                            }
                        }
                    }
                }
                case SHIFT_CLICK -> {
                    if (isConfig) {
                        // Don't allow shift-clicking on config slots - they should only be cleared
                        return;
                    }
                    var stack = inv.getStackInSlot(localSlot).copy();
                    if (!player.getInventory().add(stack)) {
                        inv.setItemDirect(localSlot, stack);
                    } else {
                        inv.setItemDirect(localSlot, ItemStack.EMPTY);
                    }
                }
                case MOVE_REGION -> {
                    if (isConfig) {
                        // Don't allow moving config slots
                        return;
                    }
                    for (int x = 0; x < inv.size(); x++) {
                        var stack = inv.getStackInSlot(x);
                        if (!player.getInventory().add(stack)) {
                            inv.setItemDirect(x, stack);
                        } else {
                            inv.setItemDirect(x, ItemStack.EMPTY);
                        }
                    }
                }
                case CREATIVE_DUPLICATE -> {
                    // Treat middle-click on config slots as "open set amount"
                    if (isConfig) {
                        var stack = configServer.getStackInSlot(localSlot);
                        var gs = appeng.api.stacks.GenericStack.fromItemStack(stack);
                        if (gs != null) {
                            // Create a custom locator that will return to the terminal instead of the interface
                            if (menu.host instanceof InterfaceAccessTerminalPart terminalPart) {
                                var terminalLocator = MenuLocators.forPart(terminalPart);
                                SetStockAmountMenu.open(player, terminalLocator, localSlot, gs.what(),
                                        (int) gs.amount());
                            }
                        }
                    } else if (player.getAbilities().instabuild && menu.getCarried().isEmpty()) {
                        var is = inv.getStackInSlot(localSlot);
                        menu.setCarried(is.isEmpty() ? ItemStack.EMPTY : is.copy());
                    }
                }
                default -> {
                }
            }
        }
    }
}
