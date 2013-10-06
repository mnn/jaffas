/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.core.common.ContainerRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static monnef.jaffas.power.JaffasPower.proxy;

public class GuiHandler implements IGuiHandler {
    public enum GuiId {
        GENERATOR,
        GRINDER,
        TOASTER,
        WIND_GENERATOR, WEB_HARVESTER
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        /*
        if (tileEntity instanceof TileGenerator) {
            return new ContainerMachine(player.inventory, (TileGenerator) tileEntity);
        } else if (tileEntity instanceof TileEntityBasicProcessingMachine) {
            return ContainerRegistry.createContainer((TileEntityBasicProcessingMachine) tileEntity, player.inventory);
        } else if (tileEntity instanceof TileWebHarvester) {
            return new ContainerMachine(player.inventory, (TileWebHarvester) tileEntity);
        } else if (tileEntity instanceof TileWindGenerator) {
            return new ContainerMachine(player.inventory, (TileWindGenerator) tileEntity);
        }
        return null;
        */
        return ContainerRegistry.createContainer(tileEntity, player.inventory);
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        /*
        if (tileEntity instanceof TileGenerator) {
            return new GuiContainerGenerator(player.inventory, (TileGenerator) tileEntity, new ContainerMachine(player.inventory, (TileEntityMachineWithInventory) tileEntity));
        } else if (tileEntity instanceof TileEntityBasicProcessingMachine) {
            return proxy.createGuiFromProcessingMachineRegistry((TileEntityBasicProcessingMachine) tileEntity, player.inventory);
        } else if (tileEntity instanceof TileWebHarvester) {
            GuiContainerMachine gui = new GuiContainerMachine(player.inventory, (TileWebHarvester) tileEntity, new ContainerMachine(player.inventory, (TileWebHarvester) tileEntity));
            gui.setBackgroundTexture("guiwebharvester.png");
            return gui;
        } else if (tileEntity instanceof TileWindGenerator) {
            return new GuiContainerWindGenerator(player.inventory, (TileWindGenerator) tileEntity, new ContainerMachine(player.inventory, (TileWindGenerator) tileEntity));
        }
        return null;
        */
        return proxy.createGuiFromProcessingMachineRegistry(tileEntity, player.inventory);
    }
}
