/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemAxeTechnic extends ItemTechnicTool {
    public ItemAxeTechnic(int textureOffset, ToolMaterial material) {
        super(textureOffset, material);
        this.damageVsEntity += 3;
    }

    @Override
    protected float getCustomStrVsBlock(ItemStack stack, Block block) {
        return Items.diamond_axe.func_150893_a(stack, block);
    }
}

