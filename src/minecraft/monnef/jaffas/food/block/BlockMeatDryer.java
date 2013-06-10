/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMeatDryer extends BlockJaffas {
    public BlockMeatDryer(int id, int texture) {
        super(id, texture, Material.wood);
        setBurnProperties(id, 15, 100);
        setHardness(0.25f);
    }


    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityMeatDryer();
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }
}
