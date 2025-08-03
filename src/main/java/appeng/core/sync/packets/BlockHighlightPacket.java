package appeng.core.sync.packets;

import io.netty.buffer.Unpooled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import appeng.client.render.BlockHighlightHandler;
import appeng.core.sync.BasePacket;

public class BlockHighlightPacket extends BasePacket {
    private BlockPos pos;
    private ResourceKey<Level> level;
    private long time;

    public BlockHighlightPacket(FriendlyByteBuf stream) {
        this.pos = stream.readBlockPos();
        this.level = stream.readResourceKey(Registries.DIMENSION);
        this.time = stream.readLong();
    }

    public BlockHighlightPacket(BlockPos pos, ResourceKey<Level> level, long time) {
        this.pos = pos;
        this.level = level;
        this.time = time;

        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeInt(this.getPacketID());
        data.writeBlockPos(pos);
        data.writeResourceKey(level);
        data.writeLong(time);
        this.configureWrite(data);
    }

    @Override
    public void clientPacketData(Player player) {
        BlockHighlightHandler.highlight(pos, level, time);
    }
}
