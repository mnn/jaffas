/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.core.block.TileMachine;
import monnef.core.power.IPipeWrench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemPipeWrench extends ItemPower implements IPipeWrench {
    public ItemPipeWrench(int textureIndex) {
        super(textureIndex);
        setUnlocalizedName("pipeWrench");
        setMaxStackSize(1);
        setSecondCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onMachineClick(TileMachine machine, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean renderPowerLabels(EntityPlayer player) {
        return player.isSneaking();
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
