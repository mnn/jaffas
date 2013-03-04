package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockPower;
import net.minecraft.block.material.Material;

public class BlockLightningConductor extends BlockPower {
    public BlockLightningConductor(int par1, int par2) {
        super(par1, par2, Material.iron);
        setBlockName("lightningConductor");
    }
}
