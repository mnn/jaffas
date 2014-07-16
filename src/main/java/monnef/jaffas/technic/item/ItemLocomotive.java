/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemLocomotive extends ItemTechnic {
    public int minecartType;

    public ItemLocomotive(int texture) {
        super(texture);
        this.maxStackSize = 1;
        this.minecartType = 0;
        this.setCreativeTab(JaffasTechnic.instance.creativeTab);
        this.setUnlocalizedName("locomotive");
        markAsBeta();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        Block targetBlock = world.getBlock(x, y, z);

        if (BlockRail.func_150051_a(targetBlock)) {
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityLocomotive(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F)));
            }

            --stack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
