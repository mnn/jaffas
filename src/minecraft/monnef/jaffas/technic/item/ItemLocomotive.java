package monnef.jaffas.technic.item;

import monnef.jaffas.technic.entity.EntityLocomotive;
import monnef.jaffas.technic.mod_jaffas_technic;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemLocomotive extends Item {
    public int minecartType;

    public ItemLocomotive(int par1, int par2) {
        super(par1);
        this.maxStackSize = 1;
        this.minecartType = par2;
        this.setCreativeTab(mod_jaffas_technic.CreativeTab);
        this.setUnlocalizedName("locomotive");
    }

    /*
    @Override
    public String getTextureFile() {
        return "/jaffas_06.png"; // TODO move icon to the ores
    }
    */

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("\u00A7lbeta\u00A7r");
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
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
