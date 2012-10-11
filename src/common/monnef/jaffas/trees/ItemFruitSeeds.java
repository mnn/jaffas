package monnef.jaffas.trees;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

import java.util.List;

public class ItemFruitSeeds extends ItemBlockEx {
    public ItemFruitSeeds(int itemID, int blockID, int textureOffset) {
        super(itemID, blockID);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setTabToDisplayOn(CreativeTabs.tabDeco);
        this.setIconIndex(textureOffset);
        this.isDefaultTexture = true;
    }

    public String getTextureFile() {
        return "/jaffas_02.png";
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1) {
        return par1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1) {
        //return Block.sapling.getBlockTextureFromSideAndMetadata(0, par1);
        return 2 * 16 - 1 + par1;
    }

    public String getItemNameIS(ItemStack par1ItemStack) {
        int dmg = par1ItemStack.getItemDamage();

        if (dmg < 0 || dmg >= mod_jaffas_trees.blockFruitLeaves.treeTypes.length) {
            dmg = 0;
        }

        String name = super.getItemName() + "." + mod_jaffas_trees.blockFruitLeaves.treeTypes[dmg];

        //System.out.println("returning name: {" + name + "}");

        return name;
    }

    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }

}
