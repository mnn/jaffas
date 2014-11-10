/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import cofh.api.energy.EnergyStorage;
import monnef.core.block.TileMachine;
import monnef.core.power.IMachineTool;
import monnef.core.utils.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;

public class ItemDebug extends ItemPower implements IMachineTool {
    private EntityPlayer player;

    public ItemDebug(int textureIndex) {
        super(textureIndex);
        setUnlocalizedName("debugPower");
    }

    @Override
    public boolean onMachineClick(TileMachine machine, EntityPlayer player, int side) {
        this.player = player;

        if (machine == null) {
            print("TE is null");
        } else {
            EnergyStorage energyStorage = machine.getEnergyStorage();
            print(String.format("%s: %d/%d(%d)", machine.getPosition().format(), Math.round(energyStorage.getEnergyStored()), (int) energyStorage.getMaxEnergyStored(), machine.getPowerNeeded()));

            if (machine.getRotation() != null) {
                print("dir: [" + machine.getRotation().ordinal() + "] " + machine.getRotation().toString() + (machine.getMachineBlock().supportRotation() ? "" : "(doesn't support rotation)"));
            }

            machine.onItemDebug(player);
        }

        return true;
    }

    @Override
    public boolean renderPowerLabels(EntityPlayer p) {
        return true;
    }

    private void print(String message) {
        PlayerHelper.addMessage(player, message);
    }
}
