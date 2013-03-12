package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.power.CommonProxy;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityLightningConductor;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(mod_jaffas_power.textureFile);
        mod_jaffas_power.renderID = RenderingRegistry.getNextAvailableRenderId();
        //RenderingRegistry.registerBlockHandler(new BlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenerator.class, new TileEntityGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new TileEntityAntennaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLightningConductor.class, new TileEntityLightningConductorRenderer());

        BlockRenderingHandler handler = new BlockRenderingHandler();
        RenderingRegistry.registerBlockHandler(handler);
        RenderingRegistry.registerBlockHandler(mod_jaffas_power.antenna.getRenderType(), handler);
        if (mod_jaffas_power.lightningConductorEnabled) {
            RenderingRegistry.registerBlockHandler(mod_jaffas_power.lightningConductor.getRenderType(), handler);
        }
    }
}