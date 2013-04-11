/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.jaffas.power.block.ContainerGenerator;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGenerator) {
            return new ContainerGenerator(player.inventory, (TileEntityGenerator) tileEntity);
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
        }
        return null;

    }
}
