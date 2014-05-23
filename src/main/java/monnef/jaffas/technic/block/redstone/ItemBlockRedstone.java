/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemBlockRedstone extends ItemBlock {
    public ItemBlockRedstone(int id) {
        super(id);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{getCreativeTab(), CreativeTabs.tabRedstone};
    }
}
