package monnef.jaffas.food.item;


import monnef.core.base.ItemMonnefCore;
import monnef.jaffas.food.Reference;
import monnef.jaffas.food.item.common.IItemJaffa;
import monnef.jaffas.food.jaffasFood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemJaffaBase extends ItemMonnefCore implements IItemJaffa {
    private String info = null;
    private CreativeTabs secondCreativeTab;

    public ItemJaffaBase(int id) {
        super(id);
        maxStackSize = 64;
        this.setCreativeTab(jaffasFood.CreativeTab);
    }

    @Override
    public String customIconName() {
        return "todo";
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
    public String getModId() {
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
}
