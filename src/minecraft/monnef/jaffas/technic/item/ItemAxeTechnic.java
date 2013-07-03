/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemAxeTechnic extends ItemTechnicTool {
    public ItemAxeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

    @Override
    protected float getCustomStrVsBlock(ItemStack stack, Block block, int meta) {
        return Item.axeDiamond.getStrVsBlock(stack, block, meta);
    }
}

