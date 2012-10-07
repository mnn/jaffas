package jaffas.common;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;

/**
 * Created with IntelliJ IDEA.
 * User: moen
 * Date: 07/10/12
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
