package monnef.jaffas.power;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.power.entity.EntityLocomotive;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_power.textureFile);

        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());
    }
}