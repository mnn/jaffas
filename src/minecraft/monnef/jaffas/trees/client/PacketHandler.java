package monnef.jaffas.trees.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.trees.TileEntityFruitCollector;
import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals(jaffasTrees.channel)) {
            handleCollectorState(packet, player);
        }
    }

    private void handleCollectorState(Packet250CustomPayload packet, Player player) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side != Side.CLIENT) {
            return;
        }

        EntityClientPlayerMP p = (EntityClientPlayerMP) player;

        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        byte newState;
        int x, y, z;
        double ix = 0, iy = 0, iz = 0;

        try {
            x = inputStream.readInt();
            y = inputStream.readInt();
            z = inputStream.readInt();
            newState = inputStream.readByte();
            ix = inputStream.readDouble();
            iy = inputStream.readDouble();
            iz = inputStream.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        TileEntityFruitCollector c = (TileEntityFruitCollector) p.worldObj.getBlockTileEntity(x, y, z);
        c.updateInnerState(newState, ix, iy, iz);
    }

}
