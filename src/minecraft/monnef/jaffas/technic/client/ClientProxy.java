package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_technic.textureFile);

        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());
    }
}