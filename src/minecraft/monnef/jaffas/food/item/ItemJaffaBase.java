package monnef.jaffas.food.item;


import monnef.core.base.ItemMonnefCore;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.item.common.IItemJaffa;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemJaffaBase extends ItemMonnefCore implements IItemJaffa {
    private String info = null;
    private CreativeTabs secondCreativeTab;

    public ItemJaffaBase(int id) {
        super(id);
        maxStackSize = 64;
        this.setCreativeTab(JaffasFood.CreativeTab);
        setCustomIconIndex(-1);
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
    public void setInfo(String text) {
        this.info = text;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (info != null) par3List.add(info);
    }

    @Override
    public String getModName() {
        return Reference.ModName;
    }

    @Override
    public int getDefaultSheetNumber() {
        return 1;
    }

    public void setSecondCreativeTab(CreativeTabs tab) {
        this.secondCreativeTab = tab;
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        if (secondCreativeTab != null) {
            return new CreativeTabs[]{secondCreativeTab, getCreativeTab()};
        } else {
            return super.getCreativeTabs();
        }
    }

    @Override
    public Item setUnlocalizedName(String par1Str) {
        return super.setUnlocalizedName("jaffas." + par1Str);
    }
}
