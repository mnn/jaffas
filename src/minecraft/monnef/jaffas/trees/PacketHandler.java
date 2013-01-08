package monnef.jaffas.trees;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals(mod_jaffas_trees.channel)) {
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

        /*
        if (mod_jaffas_trees.debug)
            System.out.println("te: " + x + "," + y + "," + z + " - s: " + newState + " - i: " + ix + "," + iy + "," + iz);
        */

        TileEntityFruitCollector c = (TileEntityFruitCollector) p.worldObj.getBlockTileEntity(x, y, z);
        c.updateInnerState(newState, ix, iy, iz);
    }

}
