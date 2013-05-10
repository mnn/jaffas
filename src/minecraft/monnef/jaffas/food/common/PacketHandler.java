/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {
    public static final String channelSpawnStone = "jaffas-01-sstone";

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        String channel = packet.channel;
        if (channel.equals(channelSpawnStone)) {
            SpawnStonePacketUtils.HandleSpawnStone(packet, player);
        }
    }


}
