/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.api;

import monnef.core.block.TileMachine;
import net.minecraft.entity.player.EntityPlayer;

public interface IMachineTool {
    public boolean onMachineClick(TileMachine machine, EntityPlayer player, int side);

    public boolean renderPowerLabels(EntityPlayer player);
}
