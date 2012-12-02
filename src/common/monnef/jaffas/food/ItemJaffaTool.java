package monnef.jaffas.food;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemJaffaTool extends Item implements IItemTool {
    public ItemJaffaTool(int id) {
        super(id);
        initialize();
    }

    public ItemJaffaTool(int id, int usageCount) {
        super(id);
        setUsageCount(usageCount);
        initialize();
    }

    private void setUsageCount(int usageCount) {
        setMaxDamage(usageCount);
    }

    private void initialize() {
        setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setCreativeTab(mod_jaffas.CreativeTab);
    }

    public String getTextureFile() {
        return "/jaffas_01.png";
    }

    @Override
    public Item Setup(int usageCount) {
        setUsageCount(usageCount);
        return this;
    }
}
