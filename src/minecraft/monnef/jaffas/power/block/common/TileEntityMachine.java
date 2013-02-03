package monnef.jaffas.power.block.common;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityMachine extends TileEntity {
    public abstract String getMachineTitle();

    private int tickCounter = 0;

    public BlockMachine getMachineBlock() {
        return (BlockMachine) this.getBlockType();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (tickCounter < 10) {
            tickCounter++;
            onTick(tickCounter);
        }
    }

    protected void onTick(int number) {
    }
}
