package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.me.interfaceaccess.InterfaceAccessTermScreen;
import appeng.core.sync.BasePacket;

public class ClearInterfaceAccessTerminalPacket extends BasePacket {
    public ClearInterfaceAccessTerminalPacket(FriendlyByteBuf stream) {
    }

    public ClearInterfaceAccessTerminalPacket() {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(16));
        data.writeInt(this.getPacketID());
        this.configureWrite(data);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        if (Minecraft.getInstance().screen instanceof InterfaceAccessTermScreen<?> screen) {
            screen.clear();
        }
    }
}
