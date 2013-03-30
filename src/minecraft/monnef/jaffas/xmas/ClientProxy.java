package monnef.jaffas.xmas;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(jaffasXmas.textureFile);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCandy.class, new TileEntityCandyRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, new TileEntityPresentRenderer());

        jaffasXmas.renderID = RenderingRegistry.getNextAvailableRenderId();

    }
}