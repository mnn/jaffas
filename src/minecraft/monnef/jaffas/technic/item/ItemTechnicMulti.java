package monnef.jaffas.technic.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.food.item.ItemJaffaBase;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import java.util.List;

public abstract class ItemTechnicMulti extends ItemTechnic {
    public abstract String[] getSubNames();

    public abstract String[] getSubTitles();

    public ItemTechnicMulti(int id, int textureIndex) {
        super(id, textureIndex);
        setMaxDamage(0);
        setHasSubtypes(true);
        setIconsCount(getSubNames().length);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, getSubNames().length);
        return getCustomIcon(var2);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, getSubNames().length);
        return super.getUnlocalizedName() + "." + getSubNames()[var2];
    }

    public void registerNames() {
        for (int i = 0; i < getSubNames().length; i++) {
            LanguageRegistry.instance().addStringLocalization(this.getUnlocalizedName() + "." + getSubNames()[i] + ".name", getSubTitles()[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < getSubNames().length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
