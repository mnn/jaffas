package monnef.jaffas.xmas.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
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
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, subNames.length);
        return null;
        //return this.iconIndex + var2;
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack) {
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
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < subNames.length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
