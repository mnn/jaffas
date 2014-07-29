/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import net.minecraft.block.Block;

public class ItemBlockSwitchgrass extends ItemBlockJaffas {
    public ItemBlockSwitchgrass(Block block) {
        super(block);
        subNames = new String[16];
        for (int i = 0; i < subNames.length; i++) {
            subNames[i] = "sg" + i;
        }
        subNames[15] = "top";

        setUnlocalizedName("itemBlockJSwitchgrass");
    }
}