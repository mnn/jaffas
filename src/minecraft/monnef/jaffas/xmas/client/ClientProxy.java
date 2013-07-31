/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.block.TileCandy;
import monnef.jaffas.xmas.block.TileCandyRenderer;
import monnef.jaffas.xmas.block.TilePresent;
import monnef.jaffas.xmas.block.TilePresentRenderer;
import monnef.jaffas.xmas.common.CommonProxy;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCandy.class, new TileCandyRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePresent.class, new TilePresentRenderer());

        JaffasXmas.renderID = RenderingRegistry.getNextAvailableRenderId();
    }
}