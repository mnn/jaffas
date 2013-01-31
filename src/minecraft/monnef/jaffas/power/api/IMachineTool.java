package monnef.jaffas.power.api;

import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public interface IMachineTool {
    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side);

    public boolean renderPowerLabels();
}
