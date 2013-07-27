/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainerJaffas extends BlockJaffas implements ITileEntityProvider {
    public BlockContainerJaffas(int id, int index, Material material) {
        super(id, index, material);
        init();
    }

    protected void init() {
        setCreativeTab(JaffasFood.instance.creativeTab);
        this.isBlockContainer = true;
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
        par1World.removeBlockTileEntity(par2, par3, par4);
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
    }
}
