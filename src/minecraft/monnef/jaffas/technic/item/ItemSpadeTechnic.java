/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;

public class ItemSpadeTechnic extends ItemTechnicTool {
    public ItemSpadeTechnic(int id, int textureOffset, EnumToolMaterial material) {
        super(id, textureOffset, material);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return Item.shovelDiamond.canHarvestBlock(block);
    }
}
