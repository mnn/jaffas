/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.core.utils.InventoryUtils;
import monnef.jaffas.power.JaffasPower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMachineWithInventory extends BlockMachine {
    public BlockMachineWithInventory(int id, int index, Material material, boolean customRenderer) {
        super(id, index, material, customRenderer);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (!super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9)) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (tileEntity == null || player.isSneaking()) {
                return false;
            }

            player.openGui(JaffasPower.instance, getGuiId(), world, x, y, z);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        InventoryUtils.dropItems(par1World, par2, par3, par4);
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public abstract int getGuiId();
}
