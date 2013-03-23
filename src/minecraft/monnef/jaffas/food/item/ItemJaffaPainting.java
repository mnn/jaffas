package monnef.jaffas.food.item;

import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.mod_jaffas_food;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemJaffaPainting extends Item {
    public ItemJaffaPainting(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setUnlocalizedName("jaffaPainting");
        //this.setIconIndex(1);
        this.setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_food.textureFile[0];
    }

    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 == 0) {
            return false;
        } else if (par7 == 1) {
            return false;
        } else {
            byte var11 = 0;

            if (par7 == 4) {
                var11 = 1;
            }

            if (par7 == 3) {
                var11 = 2;
            }

            if (par7 == 5) {
                var11 = 3;
            }

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
                return false;
            } else {
                EntityJaffaPainting var12 = new EntityJaffaPainting(par3World, par4, par5, par6, var11);

                if (var12.onValidSurface()) {
                    if (!par3World.isRemote) {
                        par3World.spawnEntityInWorld(var12);

                    }

                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }

}
