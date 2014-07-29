/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import net.minecraft.block.Block;

public class ItemBlockTable extends ItemBlockJaffas {
    public ItemBlockTable(Block block) {
        super(block);
        subNames = new String[]{"red", "green", "blue"};
        setUnlocalizedName("itemBlockJTable");
    }
}
