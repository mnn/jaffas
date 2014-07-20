/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.utils.InventoryUtils;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.client.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMachineWithInventory extends BlockPowerMachine {
    public BlockMachineWithInventory(int index, Material material, boolean customRenderer, boolean useCustomRenderingId) {
        super(index, material, customRenderer, useCustomRenderingId);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9)) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity == null || player.isSneaking()) {
                return false;
            }

            player.openGui(JaffasPower.instance, getGuiId().ordinal(), world, x, y, z);
            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        InventoryUtils.dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    public abstract GuiHandler.GuiId getGuiId();
}
