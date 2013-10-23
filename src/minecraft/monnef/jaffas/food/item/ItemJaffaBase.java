/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.item.ItemMonnefCore;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.item.common.IItemJaffa;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemJaffaBase extends ItemMonnefCore implements IItemJaffa {

    public ItemJaffaBase(int id) {
        super(id);
        maxStackSize = 64;
        this.setCreativeTab(JaffasFood.instance.creativeTab);
        setCustomIconIndex(-1);
    }

    public ItemJaffaBase(int id, int textureIndex) {
        this(id);
        setCustomIconIndex(textureIndex);
    }

    @Override
    public String getCustomIconName() {
        if (getCustomIconIndex() < 0) {
            return "todo";
        } else {
            return null;
        }
    }

    @Override
    public String getDefaultModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 1;
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas." + par1Str);
    }
}
