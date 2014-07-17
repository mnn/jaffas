/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemSpadeTechnic extends ItemTechnicTool {
    public ItemSpadeTechnic(int textureOffset, ToolMaterial material) {
        super(textureOffset, material);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_shovel.canHarvestBlock(block, stack);
    }
}
