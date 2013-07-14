/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityGrinder;
import monnef.jaffas.power.block.TileEntityLightningConductor;
import monnef.jaffas.power.block.TileEntityToaster;
import monnef.jaffas.power.common.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        JaffasPower.renderID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenerator.class, new TileEntityGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new TileEntityAntennaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLightningConductor.class, new TileEntityLightningConductorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToaster.class, new TileEntityToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrinder.class, new TileEntityGrinderRenderer());

        BlockRenderingHandler handler = new BlockRenderingHandler();
        RenderingRegistry.registerBlockHandler(handler);
        RenderingRegistry.registerBlockHandler(JaffasPower.antenna.getRenderType(), handler);
        if (JaffasPower.lightningConductorEnabled) {
            RenderingRegistry.registerBlockHandler(JaffasPower.lightningConductor.getRenderType(), handler);
        }

        MinecraftForgeClient.registerItemRenderer(JaffasPower.kitchenUnit.blockID, new CustomBlockRenderer());
    }
}