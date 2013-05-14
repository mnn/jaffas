/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.item.common.IItemTool;
import net.minecraft.item.Item;

public class ItemJaffaRecipeTool extends ItemJaffaBase implements IItemTool {
    public ItemJaffaRecipeTool(int id) {
        super(id);
        initialize();
    }

    public ItemJaffaRecipeTool(int id, int usageCount) {
        super(id);
        setUsageCount(usageCount);
        initialize();
    }

    private void setUsageCount(int usageCount) {
        setMaxDamage(usageCount);
    }

    private void initialize() {
        setMaxStackSize(1);
        this.setCreativeTab(JaffasFood.instance.creativeTab);
    }

    @Override
    public Item Setup(int usageCount) {
        setUsageCount(usageCount);
        return this;
    }
}
