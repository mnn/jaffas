package monnef.jaffas.trees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemFruitSeeds extends ItemBlockEx {
    public int serialNumber = -1;
    private int texture;
    private int subCount;
    private boolean firstInSequence = false;

    public ItemFruitSeeds(int itemID, int blockID, int textureOffset, int subCount) {
        super(itemID, blockID);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
        this.setIconIndex(textureOffset);
        this.isDefaultTexture = true;
        this.texture = textureOffset;
        this.subCount = subCount;
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
        //return 2 * 16 + par1;
        return texture + par1;
    }

    public String getItemNameIS(ItemStack par1ItemStack) {
        int dmg = par1ItemStack.getItemDamage();

        if (dmg < 0 || dmg >= mod_jaffas_trees.treeTypes.length) {
            dmg = 0;
        }

        String name = super.getItemName() + "." + dmg;
        return name;
    }

    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subCount; i++) {
            if (firstInSequence && i == 0) continue;
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public void setFirstInSequence() {
        this.firstInSequence = true;
    }
}
