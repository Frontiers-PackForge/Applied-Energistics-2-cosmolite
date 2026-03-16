package appeng.menu.implementations;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.menu.AEBaseMenu;

public class ColorizerMenu extends AEBaseMenu {

    public static final MenuType<ColorizerMenu> TYPE = MenuTypeBuilder
            .create(ColorizerMenu::new, ItemMenuHost.class)
            .build("colorizer");

    public ColorizerMenu(int id, Inventory ip, ItemMenuHost host) {
        super(TYPE, id, ip, host);
        this.createPlayerInventorySlots(ip);
    }
}
