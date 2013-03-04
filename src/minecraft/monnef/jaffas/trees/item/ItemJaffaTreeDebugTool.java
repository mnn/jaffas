package monnef.jaffas.trees.item;

import monnef.jaffas.trees.mod_jaffas_trees;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaTreeDebugTool extends Item {
    public ItemJaffaTreeDebugTool(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }
}
