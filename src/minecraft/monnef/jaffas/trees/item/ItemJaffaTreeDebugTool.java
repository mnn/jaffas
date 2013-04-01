package monnef.jaffas.trees.item;

import monnef.jaffas.trees.jaffasTrees;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaTreeDebugTool extends ItemTrees {
    public ItemJaffaTreeDebugTool(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(jaffasTrees.CreativeTab);
    }

    @Override
    public void updateIcons(IconRegister iconRegister) {
        this.iconIndex = Item.sugar.getIconFromDamage(0);
    }
}
