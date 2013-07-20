/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.jaffas.power.block.ContainerGenerator;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityGrinder;
import monnef.jaffas.power.block.TileEntityToaster;
import monnef.jaffas.power.block.common.ContainerBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public enum GuiId {
        GENERATOR,
        GRINDER,
        TOASTER,
        WEB_HARVESTER
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGenerator) {
            return new ContainerGenerator(player.inventory, (TileEntityGenerator) tileEntity);
        } else if (tileEntity instanceof TileEntityGrinder) {
            return new ContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity);
        } else if (tileEntity instanceof TileEntityToaster) {
            return new ContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGenerator) {
            return new GuiContainerGenerator(player.inventory, (TileEntityGenerator) tileEntity, new ContainerGenerator(player.inventory, (TileEntityMachineWithInventory) tileEntity));
        } else if (tileEntity instanceof TileEntityGrinder) {
            return new GuiContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity, new ContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity));
        } else if (tileEntity instanceof TileEntityToaster) {
            return new GuiContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity, new ContainerBasicProcessingMachine(player.inventory, (TileEntityBasicProcessingMachine) tileEntity));
        }
        return null;
    }
}
