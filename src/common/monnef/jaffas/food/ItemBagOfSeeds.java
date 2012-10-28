package monnef.jaffas.food;

import monnef.jaffas.trees.ItemFruitSeeds;
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
                        // choosing from our seeds (tree, bush)
                        if (rand.nextBoolean()) {
                            int type = rand.nextInt(mod_jaffas_trees.leavesTypesCount) + 1;
                            ItemFruitSeeds item = mod_jaffas_trees.leavesList.get(type / 4).seedsItem;
                            int meta = type % 4;

                            seed = new ItemStack(item, 1, meta);
                        } else {
                            int type = rand.nextInt(mod_jaffas_trees.BushesList.size());
                            seed = new ItemStack(mod_jaffas_trees.BushesList.get(mod_jaffas_trees.bushType.values()[type]).itemSeeds);
                        }
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
