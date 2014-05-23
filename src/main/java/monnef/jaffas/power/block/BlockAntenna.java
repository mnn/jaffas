/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.block.WrenchAction;
import monnef.jaffas.power.block.common.BlockPowerMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockAntenna extends BlockPowerMachine {
    public BlockAntenna(int par1, int par2) {
        super(par1, par2, Material.rock, true, true);
        setUnlocalizedName("antenna");
        onWrench = WrenchAction.ROTATE;
        useDefaultDirection = true;
        defaultDirection = ForgeDirection.NORTH;
        setHardness(0.5f);
        setResistance(3);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileAntenna();
    }

    @Override
    public boolean supportRotation() {
        return true;
    }
}
