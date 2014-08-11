package monnef.jaffas.technic.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import monnef.jaffas.technic.block.TileKeg;

public class TechnicPlayerTracker {
    @SubscribeEvent
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent evt) {
        TileKeg.init();
    }
}
