package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import appeng.core.sync.BasePacket;
import appeng.menu.implementations.ConfigurationTerminalMenu;

/**
 * Packet sent by client to request in-world highlight of a configuration terminal host (e.g., storage bus).
 */
public class HighlightPartRequestPacket extends BasePacket {

    private final int containerId;
    private final long hostId;

    public HighlightPartRequestPacket(FriendlyByteBuf buffer) {
        this.containerId = buffer.readInt();
        this.hostId = buffer.readVarLong();
    }

    public HighlightPartRequestPacket(int containerId, long hostId) {
        this.containerId = containerId;
        this.hostId = hostId;
        var data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeInt(this.getPacketID());
        data.writeInt(containerId);
        data.writeVarLong(hostId);
        this.configureWrite(data);
    }

    @Override
    public void serverPacketData(ServerPlayer player) {
        if (player.containerMenu instanceof ConfigurationTerminalMenu menu) {
            if (player.containerMenu.containerId != containerId) {
                return;
            }
            menu.handleHighlightRequest(hostId);
        }
    }
}
