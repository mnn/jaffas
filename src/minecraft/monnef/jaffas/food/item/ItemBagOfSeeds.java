package monnef.jaffas.food.item;

import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.common.SeedsHelper;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class ItemBagOfSeeds extends ItemJaffaBase {
    private static Random rand = new Random();

    public ItemBagOfSeeds(int par1) {
        super(par1);

        setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setCreativeTab(jaffasFood.CreativeTab);
    }

    public String getTextureFile() {
        return jaffasFood.textureFile[0];
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {

            for (int i = 0; i < 7; i++) {
                if (rand.nextInt(10) < 8) {
                    ItemStack seed;
                    seed = SeedsHelper.getRandomSeed();

                    PlayerHelper.giveItemToPlayer(par3EntityPlayer, seed);
                }
            }
        }

        par1ItemStack.stackSize--;
        return par1ItemStack;
    }
}
