/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemXmasMulti extends ItemXmas {
    protected String[] subNames;
    protected String[] subTitles;

    public ItemXmasMulti(int textureIndex) {
        super(textureIndex);
        setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, subNames.length);
        return null;
        //return this.iconIndex + var2;
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, subNames.length);
        return super.getUnlocalizedName() + "." + subNames[var2];
    }

    public void registerNames() {
        for (int i = 0; i < subNames.length; i++) {
            LanguageRegistry.instance().addStringLocalization(this.getUnlocalizedName() + "." + subNames[i] + ".name", subTitles[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subNames.length; i++) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }
}
