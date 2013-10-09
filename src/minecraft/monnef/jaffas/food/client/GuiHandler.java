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
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ContainerRegistry.containsRegistration(tileEntity)) {
            return ContainerRegistry.createContainer(tileEntity, player.inventory);
        } else if (tileEntity instanceof TileFridge) {
            return new ContainerFridge(player.inventory, (TileFridge) tileEntity);
        } /*else if (tileEntity instanceof TileBoard) {
            return new ContainerBoard(player.inventory, (TileBoard) tileEntity);
        } else if (tileEntity instanceof TileCompostCore) {
            return new ContainerCompost(player.inventory, (TileCompostCore) tileEntity);
        } else if (tileEntity instanceof TileCobbleBreaker) {
            return new ContainerCobbleBreaker(player.inventory, (TileCobbleBreaker) tileEntity);
        } else if (tileEntity instanceof TileFermenter) {
            return new ContainerFermenter(player.inventory, (TileFermenter) tileEntity);
        } else if (tileEntity instanceof TileRipeningBox) {
            return new ContainerRipeningBox(player.inventory, (TileRipeningBox) tileEntity);
        } */

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ContainerRegistry.containsRegistration(tileEntity)) {
            return ContainerRegistry.createGui(tileEntity, player.inventory);
        }/* else {
            //if (tileEntity instanceof TileFridge) {
            //    return new GuiFridge(player.inventory, (TileFridge) tileEntity);
            //} else if (tileEntity instanceof TileBoard) {
            //   return new GuiBoard(player.inventory, (TileBoard) tileEntity);
            //} else if (tileEntity instanceof TileCompostCore) {
            //    return new GuiCompost(player.inventory, (TileCompostCore) tileEntity);
            //} else if (tileEntity instanceof TileCobbleBreaker) {
            //    return new GuiCobbleBreaker(player.inventory, (TileCobbleBreaker) tileEntity);
            //} else if (tileEntity instanceof TileFermenter) {
            //    return new GuiFermenter(player.inventory, (TileFermenter) tileEntity);
            //} else if (tileEntity instanceof TileRipeningBox) {
            //    return new GuiRipeningBox(player.inventory, (TileRipeningBox) tileEntity);
            //}
        }*/
        return null;
    }
}
