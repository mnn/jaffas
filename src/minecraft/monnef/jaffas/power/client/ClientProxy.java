/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.power.block.TileToaster;
import monnef.jaffas.power.block.TileWebHarvester;
import monnef.jaffas.power.block.TileWindGenerator;
import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        JaffasPower.renderID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileGenerator.class, new TileGeneratorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntenna.class, new TileAntennaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileLightningConductor.class, new TileLightningConductorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileToaster.class, new TileToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGrinder.class, new TileGrinderRenderer());

        BlockRenderingHandler handler = new BlockRenderingHandler();
        RenderingRegistry.registerBlockHandler(handler);
        RenderingRegistry.registerBlockHandler(JaffasPower.antenna.getRenderType(), handler);
        if (JaffasPower.lightningConductorEnabled) {
            RenderingRegistry.registerBlockHandler(JaffasPower.lightningConductor.getRenderType(), handler);
        }

        MinecraftForgeClient.registerItemRenderer(JaffasPower.kitchenUnit.blockID, new CustomBlockRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityWindTurbine.class, new RenderWindTurbine());
    }
}