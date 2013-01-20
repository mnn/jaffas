package monnef.jaffas.carts.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.carts.CommonProxy;
import monnef.jaffas.carts.entity.EntityLocomotive;
import monnef.jaffas.carts.mod_jaffas_carts;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_carts.textureFile);

        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());
    }
}