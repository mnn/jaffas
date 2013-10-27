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
        return ContainerRegistry.createContainer(tileEntity, player.inventory);
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        return ContainerRegistry.createGui(tileEntity, player.inventory);
    }
}
