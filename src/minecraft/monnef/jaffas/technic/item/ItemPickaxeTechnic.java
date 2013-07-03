/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPickaxeTechnic extends ItemTechnicTool {
    public ItemPickaxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

    @Override
    public float getCustomStrVsBlock(ItemStack stack, Block block, int metadata) {
        return Item.pickaxeDiamond.getStrVsBlock(stack, block, metadata);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return Item.pickaxeDiamond.canHarvestBlock(block);
    }
}
