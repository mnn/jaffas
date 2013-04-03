package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityLightningConductor;
import monnef.jaffas.power.jaffasPower;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture(jaffasPower.textureFile);
        jaffasPower.renderID = RenderingRegistry.getNextAvailableRenderId();
        //RenderingRegistry.registerBlockHandler(new BlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenerator.class, new TileEntityGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new TileEntityAntennaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLightningConductor.class, new TileEntityLightningConductorRenderer());

        BlockRenderingHandler handler = new BlockRenderingHandler();
        RenderingRegistry.registerBlockHandler(handler);
        RenderingRegistry.registerBlockHandler(jaffasPower.antenna.getRenderType(), handler);
        if (jaffasPower.lightningConductorEnabled) {
            RenderingRegistry.registerBlockHandler(jaffasPower.lightningConductor.getRenderType(), handler);
        }
    }
}