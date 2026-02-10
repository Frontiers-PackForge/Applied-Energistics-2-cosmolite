package appeng.api.implementations.blockentities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import appeng.api.stacks.AEItemKey;

/**
 * Display group for a filter-configurable host in the configuration terminal (name and icon).
 */
public record ConfigFilterGroup(@Nullable AEItemKey icon, Component name) {

    public void writeToPacket(FriendlyByteBuf buffer) {
        buffer.writeBoolean(icon != null);
        if (icon != null) {
            icon.writeToPacket(buffer);
        }
        buffer.writeComponent(name);
    }

    public static ConfigFilterGroup readFromPacket(FriendlyByteBuf buffer) {
        var icon = buffer.readBoolean() ? AEItemKey.fromPacket(buffer) : null;
        var name = buffer.readComponent();
        return new ConfigFilterGroup(icon, name);
    }
}
