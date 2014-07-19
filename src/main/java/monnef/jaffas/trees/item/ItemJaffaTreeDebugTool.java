/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.item;

import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;

public class ItemJaffaTreeDebugTool extends ItemTrees {
    public ItemJaffaTreeDebugTool() {
        super();
        this.setCreativeTab(CreativeTabs.tabTools);
        setCreativeTab(JaffasTrees.instance.creativeTab);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = Items.sugar.getIconFromDamage(0);
    }
}
