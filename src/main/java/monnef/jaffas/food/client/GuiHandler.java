/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.block.ContainerFridge;
import monnef.jaffas.food.block.TileFridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public enum GuiTypes {
        FRIDGE, BOARD, COMPOST, COBBLE_BREAKER, FERMENTER, RIPENING_BOX
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (ContainerRegistry.containsRegistration(tileEntity)) {
            return ContainerRegistry.createContainer(tileEntity, player.inventory);
        } else if (tileEntity instanceof TileFridge) {
            return new ContainerFridge(player.inventory, (TileFridge) tileEntity);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (ContainerRegistry.containsRegistration(tileEntity)) {
            return ContainerRegistry.createGui(tileEntity, player.inventory);
        }

        return null;
    }
}
