package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.common.ModuleManager;
import monnef.jaffas.food.common.ModulesEnum;
import monnef.jaffas.food.mod_jaffas_food;
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
        this.setCreativeTab(mod_jaffas_food.CreativeTab);
    }

    public String getTextureFile() {
        return mod_jaffas_food.textureFile[0];
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {

            for (int i = 0; i < 7; i++) {
                if (rand.nextInt(10) < 8) {
                    ItemStack seed;
                    seed = getRandomSeed();

                    PlayerHelper.giveItemToPlayer(par3EntityPlayer, seed);
                }
            }
        }

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }

    public static ItemStack getRandomSeed() {
        ItemStack seed;
        if (rand.nextInt(2) == 0 || !ModuleManager.IsModuleEnabled(ModulesEnum.trees)) {
            seed = new ItemStack(Item.seeds);
        } else {
            seed = getRandomJaffaSeed();
        }
        return seed;
    }

    public static ItemStack getRandomJaffaSeed() {
        ItemStack seed;// choosing from our seeds (tree, bush)
        if (rand.nextBoolean()) {
            int type = rand.nextInt(mod_jaffas_trees.leavesTypesCount) + 1;
            seed = mod_jaffas_trees.getTreeSeeds(type);
        } else {
            int type = rand.nextInt(mod_jaffas_trees.BushesList.size());
            seed = new ItemStack(mod_jaffas_trees.BushesList.get(mod_jaffas_trees.bushType.values()[type]).itemSeeds);
        }
        return seed;
    }
}
