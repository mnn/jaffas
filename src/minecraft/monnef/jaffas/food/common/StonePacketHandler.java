/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import monnef.jaffas.food.network.JaffasPacketHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class StonePacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        String channel = packet.channel;
        if (channel.equals(JaffasPacketHandler.CHANNEL_SpawnStone)) {
            SpawnStonePacketUtils.HandleSpawnStone(packet, player);
        }
    }


}
