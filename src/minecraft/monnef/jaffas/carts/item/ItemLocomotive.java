package monnef.jaffas.carts.item;

import monnef.jaffas.carts.entity.EntityLocomotive;
import monnef.jaffas.carts.mod_jaffas_carts;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLocomotive extends Item {
    public int minecartType;

    public ItemLocomotive(int par1, int par2) {
        super(par1);
        this.maxStackSize = 1;
        this.minecartType = par2;
        this.setCreativeTab(mod_jaffas_carts.CreativeTab);
        this.setItemName("locomotive");
    }

    @Override
    public String getTextureFile() {
        return "/jaffas_06.png";
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        int var11 = par3World.getBlockId(par4, par5, par6);

        if (BlockRail.isRailBlock(var11)) {
            if (!par3World.isRemote) {
                par3World.spawnEntityInWorld(new EntityLocomotive(par3World, (double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), this.minecartType));
            }

            --par1ItemStack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
