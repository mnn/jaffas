package monnef.jaffas.technic.block;

import monnef.core.utils.BitHelper;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHighPlant extends BlockTechnic {
    private static final int SLAVE_BIT = 3;

    public BlockHighPlant(int id, int textureID) {
        super(id, textureID, Material.plants);
    }

    public boolean isMaster(int meta) {
        return !BitHelper.isBitSet(meta, SLAVE_BIT);
    }

    public boolean isSlave(int meta) {
        return !isMaster(meta);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return isMaster(meta);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityHighPlant();
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
}
