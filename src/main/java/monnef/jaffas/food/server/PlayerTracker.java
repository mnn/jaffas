/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.server;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import monnef.jaffas.food.network.HomeStonePacket;

public class PlayerTracker {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent evt) {
        HomeStonePacket.sendSyncPacket(evt.player, false);
    }

    @SubscribeEvent
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent evt){

    }
}
