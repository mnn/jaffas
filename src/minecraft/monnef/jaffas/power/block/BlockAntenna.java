package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockMachine;
import monnef.jaffas.power.block.common.WrenchAction;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAntenna extends BlockMachine {
    public BlockAntenna(int par1, int par2) {
        super(par1, par2, Material.rock, true);
        setBlockName("antenna");
        onWrench = WrenchAction.ROTATE;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityAntenna();
    }

    @Override
    public boolean supportRotation() {
        return true;
    }

    @Override
    public boolean useOwnRenderId() {
        return true;
    }
}
