/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.jaffas.power.api.IPipeWrench;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemPipeWrench extends ItemPower implements IPipeWrench {
    public ItemPipeWrench(int id, int textureIndex) {
        super(id, textureIndex);
        setUnlocalizedName("pipeWrench");
        setMaxStackSize(1);
    }

    @Override
    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean renderPowerLabels(EntityPlayer player) {
        return player.isSneaking();
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
