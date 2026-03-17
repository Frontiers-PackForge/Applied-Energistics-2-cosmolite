package appeng.menu.me.crafting;

/**
 * Implement this on a menu that should a CPU list.
 * <p>
 * Currently used by {@link appeng.menu.me.crafting.CraftConfirmMenu CraftConfirmMenu} and
 * {@link appeng.menu.me.crafting.CraftingStatusMenu CraftingStatusMenu}.
 */
public interface ICpuListMenu {
    CraftingCpuList getCpuList();

    int getSelectedCpuSerial();

    void selectCpu(int serial);

    default boolean isCpuValid(CraftingCpuListEntry cpu) {
        return true;
    }
}
