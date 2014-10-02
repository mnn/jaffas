package monnef.jaffas.trees.compatibility;

import mcp.mobius.waila.api.IWailaRegistrar;
import monnef.jaffas.trees.block.BlockJaffaCrops;

public class WailaCallback {
    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new WailaCrops(), BlockJaffaCrops.class);
    }
}
