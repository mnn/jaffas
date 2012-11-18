package monnef.jaffas.food;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
