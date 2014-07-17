/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPickaxeTechnic extends ItemTechnicTool {
    public ItemPickaxeTechnic(int textureOffset, ToolMaterial material) {
        super(textureOffset, material);
    }

    @Override
    protected float getCustomStrVsBlock(ItemStack stack, Block block) {
        return Items.diamond_pickaxe.func_150893_a(stack, block);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return Items.diamond_pickaxe.canHarvestBlock(block, stack);
    }
}
