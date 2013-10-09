/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.core.common.ContainerRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerTrees implements IGuiHandler {
    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (ContainerRegistry.containsRegistration(tile)) {
            return ContainerRegistry.createContainer(tile, player.inventory);
        }
        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (ContainerRegistry.containsRegistration(tile)) {
            return ContainerRegistry.createGui(tile, player.inventory);
        }
        return null;
    }
}

