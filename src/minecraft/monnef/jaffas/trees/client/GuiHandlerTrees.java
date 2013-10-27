/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.trees.item.ContainerBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerTrees implements IGuiHandler {
    public enum GuiId {
        BAG_PLANTING,
        BAG_COLLECTING
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (ContainerRegistry.containsRegistration(tile)) {
            return ContainerRegistry.createContainer(tile, player.inventory);
        } else if (id == GuiId.BAG_PLANTING.ordinal()) {
            return new ContainerBag(player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (ContainerRegistry.containsRegistration(tile)) {
            return ContainerRegistry.createGui(tile, player.inventory);
        } else if (id == GuiId.BAG_PLANTING.ordinal()) {
            return new GuiBag(new ContainerBag(player));
        }
        return null;
    }
}

