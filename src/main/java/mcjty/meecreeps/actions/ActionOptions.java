package mcjty.meecreeps.actions;

import io.netty.buffer.ByteBuf;
import mcjty.meecreeps.network.NetworkTools;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionOptions {

    private final List<MeeCreepActionType> actionOptions;
    private final BlockPos pos;
    private final int dimension;
    private final UUID playerId;

    private int timeout1;
    private int timeout2;

    public ActionOptions(List<MeeCreepActionType> actionOptions, BlockPos pos, int dimension, UUID playerId) {
        this.actionOptions = actionOptions;
        this.pos = pos;
        this.dimension = dimension;
        this.playerId = playerId;
        timeout1 = 20;
        timeout2 = 20;
    }

    public ActionOptions(ByteBuf buf) {
        int size = buf.readInt();
        actionOptions = new ArrayList<>();
        while (size > 0) {
            actionOptions.add(MeeCreepActionType.VALUES[buf.readByte()]);
            size--;
        }
        pos = NetworkTools.readPos(buf);
        dimension = buf.readInt();
        playerId = new UUID(buf.readLong(), buf.readLong());
    }

    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(actionOptions.size());
        for (MeeCreepActionType option : actionOptions) {
            buf.writeByte(option.ordinal());
        }
        NetworkTools.writePos(buf, pos);
        buf.writeInt(dimension);
        buf.writeLong(playerId.getMostSignificantBits());
        buf.writeLong(playerId.getLeastSignificantBits());
    }

    public List<MeeCreepActionType> getActionOptions() {
        return actionOptions;
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getDimension() {
        return dimension;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public boolean tick1() {
        timeout1--;
        return timeout1 == 0;
    }

    public boolean tick2() {
        if (timeout1 > 0) {
            return false;
        }
        timeout2--;
        return timeout2 <= 0;
    }
}
