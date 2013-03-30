package monnef.jaffas.trees.item;

import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaTreeDebugTool extends Item {
    public ItemJaffaTreeDebugTool(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(jaffasTrees.CreativeTab);
    }
}
