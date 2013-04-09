package monnef.jaffas.trees.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.base.CustomIconHelper;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

public class ItemFruitSeeds extends ItemBlockTrees {
    public int serialNumber = -1;
    private int subCount;
    private boolean firstInSequence = false;
    private Icon[] icons;

    public ItemFruitSeeds(int itemID, int blockID, int textureOffset, int subCount) {
        super(itemID, blockID);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        setCustomIconIndex(textureOffset);
        this.subCount = subCount;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        return icons[par1];
    }

    @Override
    public void updateIcons(IconRegister register) {
        icons = new Icon[subCount];
        for (int i = 0; i < icons.length; i++) {
            if (firstInSequence && i == 0) continue;
            icons[i] = register.registerIcon(CustomIconHelper.generateShiftedId(this, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int dmg = par1ItemStack.getItemDamage();

        if (dmg < 0 || dmg >= JaffasTrees.treeTypes.length) {
            dmg = 0;
        }

        String name = super.getUnlocalizedName() + "." + dmg;
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