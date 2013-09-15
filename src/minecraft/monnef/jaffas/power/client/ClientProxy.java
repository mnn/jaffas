/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileGrinder;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.power.block.TileToaster;
import monnef.jaffas.power.block.common.ProcessingMachineRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import monnef.jaffas.power.common.CommonProxy;
import monnef.jaffas.power.entity.EntityWindTurbine;
import net.minecraft.entity.player.InventoryPlayer;
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

    @Override
    public Object createGuiFromProcessingMachineRegistry(TileEntityBasicProcessingMachine tile, InventoryPlayer inventory) {
        try {

            return ProcessingMachineRegistry.getItem(tile.getClass()).getGuiConstructor().newInstance(inventory, tile, ProcessingMachineRegistry.createContainer(tile, inventory));
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create new GUI for container for tile class: " + tile.getClass().getSimpleName());
        }
    }

    @Override
    public void assertClassInheritsFromGuiContainerBasicProcessingMachine(Class<?> aClass) {
        if (!GuiContainerBasicProcessingMachine.class.isAssignableFrom(aClass)) {
            throw new RuntimeException("Class doesn't inherit from proper ancestor!");
        }
    }

    @Override
    public void registerGUIsOfProcessingMachines() {
        ProcessingMachineRegistry.registerOnClient(TileGrinder.class, GuiContainerBasicProcessingMachine.class);
        ProcessingMachineRegistry.registerOnClient(TileToaster.class, GuiContainerBasicProcessingMachine.class);
    }
}