/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block.redstone;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemBlockRedstone extends ItemBlock {
    public ItemBlockRedstone(Block b) {
        super(b);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{getCreativeTab(), CreativeTabs.tabRedstone};
    }
}
