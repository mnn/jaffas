/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemJaffaTreeDebugTool extends ItemTrees {
    public ItemJaffaTreeDebugTool(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(JaffasTrees.instance.CreativeTab);
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        this.itemIcon = Item.sugar.getIconFromDamage(0);
    }
}
