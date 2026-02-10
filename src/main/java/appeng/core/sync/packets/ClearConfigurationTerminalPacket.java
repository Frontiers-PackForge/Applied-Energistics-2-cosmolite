package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.me.config.ConfigTerminalScreen;
import appeng.core.sync.BasePacket;

/**
 * Clears all data from the configuration terminal before a full reset.
 */
public class ClearConfigurationTerminalPacket extends BasePacket {

    public ClearConfigurationTerminalPacket(FriendlyByteBuf stream) {
    }

    public ClearConfigurationTerminalPacket() {
        var data = new FriendlyByteBuf(Unpooled.buffer(16));
        data.writeInt(this.getPacketID());
        this.configureWrite(data);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        if (Minecraft.getInstance().screen instanceof ConfigTerminalScreen<?> screen) {
            screen.clear();
        }
    }
}
