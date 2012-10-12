package monnef.jaffas.food;

import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.src.*;

import java.util.Random;

public class ItemBagOfSeeds extends Item {
    private static Random rand = new Random();

    protected ItemBagOfSeeds(int par1) {
        super(par1);

        setMaxStackSize(1);
        this.setTabToDisplayOn(CreativeTabs.tabMaterials);
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        //par3EntityPlayer.inventory.addItemStackToInventory(content.copy());

        if (!par2World.isRemote) {

            for (int i = 0; i < 7; i++) {
                if (rand.nextInt(10) < 8) {
                    ItemStack seed;
                    if (rand.nextInt(2) == 0) {
                        seed = new ItemStack(Item.seeds);
                    } else {
                        int type = rand.nextInt(6) + 1;
                        seed = new ItemStack(mod_jaffas_trees.itemFruitSeeds, 1, type);
                    }

                    Entity ent = new EntityItem(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY + 0.5, par3EntityPlayer.posZ, seed);
                    par2World.spawnEntityInWorld(ent);
                }
            }
        }

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }
}
