package monnef.jaffas.xmas;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_xmas.textureFile);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCandy.class, new TileEntityCandyRenderer());

        mod_jaffas_xmas.renderID = RenderingRegistry.getNextAvailableRenderId();

    }
}