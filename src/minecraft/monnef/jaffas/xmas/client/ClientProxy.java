package monnef.jaffas.xmas.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.xmas.JaffasXmas;
import monnef.jaffas.xmas.common.CommonProxy;
import monnef.jaffas.xmas.block.TileEntityCandy;
import monnef.jaffas.xmas.block.TileEntityCandyRenderer;
import monnef.jaffas.xmas.block.TileEntityPresent;
import monnef.jaffas.xmas.block.TileEntityPresentRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(JaffasXmas.textureFile);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCandy.class, new TileEntityCandyRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, new TileEntityPresentRenderer());

        JaffasXmas.renderID = RenderingRegistry.getNextAvailableRenderId();

    }
}