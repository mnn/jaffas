/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.jaffas.food.block.ContainerBoard;
import monnef.jaffas.food.block.ContainerFridge;
import monnef.jaffas.food.block.ContainerRipeningBox;
import monnef.jaffas.food.block.TileEntityBoard;
import monnef.jaffas.food.block.TileEntityFridge;
import monnef.jaffas.food.block.TileRipeningBox;
import monnef.jaffas.technic.block.ContainerCobbleBreaker;
import monnef.jaffas.technic.block.ContainerCompost;
import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileEntityCobbleBreaker;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import monnef.jaffas.technic.block.TileEntityFermenter;
import monnef.jaffas.technic.client.GuiCobbleBreaker;
import monnef.jaffas.technic.client.GuiCompost;
import monnef.jaffas.technic.client.GuiFermenter;
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
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileEntityFridge) {
            return new ContainerFridge(player.inventory, (TileEntityFridge) tileEntity);
        } else if (tileEntity instanceof TileEntityBoard) {
            return new ContainerBoard(player.inventory, (TileEntityBoard) tileEntity);
        } else if (tileEntity instanceof TileEntityCompostCore) {
            return new ContainerCompost(player.inventory, (TileEntityCompostCore) tileEntity);
        } else if (tileEntity instanceof TileEntityCobbleBreaker) {
            return new ContainerCobbleBreaker(player.inventory, (TileEntityCobbleBreaker) tileEntity);
        } else if (tileEntity instanceof TileEntityFermenter) {
            return new ContainerFermenter(player.inventory, (TileEntityFermenter) tileEntity);
        } else if (tileEntity instanceof TileRipeningBox) {
            return new ContainerRipeningBox(player.inventory, (TileRipeningBox) tileEntity);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileEntityFridge) {
            return new GuiFridge(player.inventory, (TileEntityFridge) tileEntity);
        } else if (tileEntity instanceof TileEntityBoard) {
            return new GuiBoard(player.inventory, (TileEntityBoard) tileEntity);
        } else if (tileEntity instanceof TileEntityCompostCore) {
            return new GuiCompost(player.inventory, (TileEntityCompostCore) tileEntity);
        } else if (tileEntity instanceof TileEntityCobbleBreaker) {
            return new GuiCobbleBreaker(player.inventory, (TileEntityCobbleBreaker) tileEntity);
        } else if (tileEntity instanceof TileEntityFermenter) {
            return new GuiFermenter(player.inventory, (TileEntityFermenter) tileEntity);
        } else if (tileEntity instanceof TileRipeningBox) {
            return new GuiRipeningBox(player.inventory, (TileRipeningBox) tileEntity);
        }

        return null;
    }
}
