package monnef.jaffas.xmas;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemXmasMulti extends ItemXmas {
    protected String[] subNames;
    protected String[] subTitles;

    public ItemXmasMulti(int id, int textureIndex) {
        super(id, textureIndex);
        setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, subNames.length);
        return this.iconIndex + var2;
    }

    public String getItemNameIS(ItemStack par1ItemStack) {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, subNames.length);
        return super.getItemName() + "." + subNames[var2];
    }

    public void registerNames() {
        for (int i = 0; i < subNames.length; i++) {
            LanguageRegistry.instance().addStringLocalization(this.getItemName() + "." + subNames[i] + ".name", subTitles[i]);
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subNames.length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
