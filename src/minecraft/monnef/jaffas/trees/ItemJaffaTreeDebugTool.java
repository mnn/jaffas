package monnef.jaffas.trees;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaTreeDebugTool extends Item {
    protected ItemJaffaTreeDebugTool(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(mod_jaffas_trees.CreativeTab);
    }
}
