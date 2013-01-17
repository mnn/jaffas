package monnef.jaffas.power.api;

import monnef.jaffas.power.block.machine.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public interface IMachineTool {
    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player);
}
