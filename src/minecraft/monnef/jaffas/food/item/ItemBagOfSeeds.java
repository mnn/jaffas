package monnef.jaffas.food.item;

import monnef.core.PlayerHelper;
import monnef.jaffas.food.mod_jaffas;
import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class ItemBagOfSeeds extends Item {
    private static Random rand = new Random();

    public ItemBagOfSeeds(int par1) {
        super(par1);

        setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setCreativeTab(mod_jaffas.CreativeTab);
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
                            seed = mod_jaffas_trees.getTreeSeeds(type);
                        } else {
                            int type = rand.nextInt(mod_jaffas_trees.BushesList.size());
                            seed = new ItemStack(mod_jaffas_trees.BushesList.get(mod_jaffas_trees.bushType.values()[type]).itemSeeds);
                        }
                    }

                    PlayerHelper.giveItemToPlayer(par3EntityPlayer, seed);
                }
            }
        }

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }


}
