package monnef.jaffas.power.item;

import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ItemPipeWrench extends ItemPower implements IPipeWrench {
    public ItemPipeWrench(int id, int textureIndex) {
        super(id, textureIndex);
        setUnlocalizedName("pipeWrench");
    }

    @Override
    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean renderPowerLabels() {
        return true;
    }
}
