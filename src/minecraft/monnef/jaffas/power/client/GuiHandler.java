/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.jaffas.power.block.ContainerGenerator;
import monnef.jaffas.power.block.ContainerGrinder;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.TileEntityGrinder;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public enum GuiId {
        GENERATOR,
        GRINDER
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGenerator) {
            return new ContainerGenerator(player.inventory, (TileEntityGenerator) tileEntity);
        } else if (tileEntity instanceof TileEntityGrinder) {
            return new ContainerGrinder(player.inventory, (TileEntityMachineWithInventory) tileEntity);
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
            return new GuiContainerGrinder(player.inventory, (TileEntityBasicProcessingMachine) tileEntity, new ContainerGrinder(player.inventory, (TileEntityMachineWithInventory) tileEntity));
        }
        return null;
    }
}
