package monnef.jaffas.power;

import monnef.jaffas.ores.mod_jaffas_ores;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_ores.textureFile);
    }
}