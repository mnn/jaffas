package monnef.jaffas.power.block;

import monnef.core.BitHelper;
import monnef.jaffas.power.block.common.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntenna extends BlockMachine {
    private static final int LIT_BIT = 3;

    public BlockAntenna(int par1, int par2) {
        super(par1, par2, Material.rock, true);
        setBlockName("antenna");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityAntenna();
    }

    @Override
    public int getRotation(int meta) {
        return meta & 7;
    }

    @Override
    public boolean supportRotation() {
        return true;
    }

    public boolean isLit(int meta) {
        return BitHelper.isBitSet(meta, LIT_BIT);
    }

    public void setRotation(World world, int x, int y, int z, int rotation) {
        int meta = world.getBlockMetadata(x, y, z);
        int newMeta = (meta & 8) | (rotation & 7);
        world.setBlockMetadataWithNotify(x, y, z, newMeta);
    }
}
