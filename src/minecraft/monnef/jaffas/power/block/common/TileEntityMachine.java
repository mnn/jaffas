package monnef.jaffas.power.block.common;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityMachine extends TileEntity {
    public abstract String getMachineTitle();

    public BlockMachine getMachineBlock() {
        return (BlockMachine) this.getBlockType();
    }
}
