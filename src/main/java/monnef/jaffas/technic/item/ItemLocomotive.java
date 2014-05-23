/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemLocomotive extends ItemTechnic {
    public int minecartType;

    public ItemLocomotive(int id, int texture) {
        super(id, texture);
        this.maxStackSize = 1;
        this.minecartType = 0;
        this.setCreativeTab(JaffasTechnic.instance.creativeTab);
        this.setUnlocalizedName("locomotive");
        markAsBeta();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformation(stack, player, result, par4);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        int var11 = world.getBlockId(par4, par5, par6);

        if (BlockRail.isRailBlock(var11)) {
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityLocomotive(world, (double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F)));
            }

            --stack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
