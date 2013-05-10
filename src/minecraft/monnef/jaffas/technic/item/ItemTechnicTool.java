/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import monnef.core.MonnefCorePlugin;
import monnef.jaffas.food.item.ItemJaffaTool;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemTechnicTool extends ItemJaffaTool {
    public ItemTechnicTool(int id, int textureIndex, EnumToolMaterial material) {
        super(id, textureIndex, material);
        setCreativeTab(JaffasTechnic.CreativeTab);
        setSheetNumber(3);
        durabilityLossOnEntityHit = 2;
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
