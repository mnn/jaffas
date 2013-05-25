/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.utils.BlockHelper;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFungiBox extends BlockTechnic {
    public static final MaterialFungiBox material = new MaterialFungiBox();

    public BlockFungiBox(int id, int textureID) {
        super(id, textureID, material);
        Block.setBurnProperties(id, 5, 20);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityFungiBox();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return JaffasFood.renderID;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y - 1, z).isSolid();
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
        boolean drop = false;
        if (!canBlockStay(world, x, y, z)) {
            drop = true;
        }

        if (drop) {
            dropBlockAsItem(world, x, y, z, 0, 0); // metadata, fortune
            BlockHelper.setBlock(world, x, y, z, 0);
        }
    }

    public static class MaterialFungiBox extends Material {
        public MaterialFungiBox() {
            super(MapColor.woodColor);
            setBurning();
        }

        @Override
        public boolean isOpaque() {
            return false;
        }

        @Override
        public boolean isSolid() {
            return false;
        }
    }
}
