/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.MonnefCorePlugin;
import monnef.jaffas.food.item.ItemJaffaSword;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemSwordTechnic extends ItemJaffaSword {
    public ItemSwordTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
        setCreativeTab(JaffasTechnic.instance.CreativeTab);
        setSheetNumber(3);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        if (MonnefCorePlugin.debugEnv) par3List.add(new ItemStack(par1, 1, getMaxDamage() - 10));
    }
}
