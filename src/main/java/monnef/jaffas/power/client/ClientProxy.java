/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.client.ClientUtils;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.power.block.TileJuiceMaker;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.power.block.TileToaster;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileGenerator.class, new TileGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntenna.class, new TileAntennaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileLightningConductor.class, new TileLightningConductorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileToaster.class, new TileToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class, new TileGrinderRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileJuiceMaker.class, new TileJuiceMakerRenderer());

        BlockRenderingHandler handler = new BlockRenderingHandler();
        RenderingRegistry.registerBlockHandler(handler);
        RenderingRegistry.registerBlockHandler(JaffasPower.antenna.getRenderType(), handler);
        if (JaffasPower.lightningConductorEnabled) {
            RenderingRegistry.registerBlockHandler(JaffasPower.lightningConductor.getRenderType(), handler);
        }
        RenderingRegistry.registerBlockHandler(JaffasPower.juiceMaker.getRenderType(), handler);

        ClientUtils.registerItemRendererOfBlock(JaffasPower.kitchenUnit, new CustomBlockRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityWindTurbine.class, new RenderWindTurbine());
    }
}