package monnef.jaffas.power.block.common;

import monnef.core.utils.InventoryUtils;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMachineWithInventory extends BlockMachine {
    public BlockMachineWithInventory(int par1, int par2, Material par3Material, boolean customRenderer) {
        super(par1, par2, par3Material, customRenderer);
    }

    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (!super.onBlockActivated(par1World, x, y, z, player, side, par7, par8, par9)) {
            TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);
            if (tileEntity == null || player.isSneaking()) {
                return false;
            }

            player.openGui(mod_jaffas_power.instance, getGuiId(), par1World, x, y, z);
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
