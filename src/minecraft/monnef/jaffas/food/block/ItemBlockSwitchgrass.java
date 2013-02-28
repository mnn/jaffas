package monnef.jaffas.food.block;

import net.minecraft.item.ItemStack;

public class ItemBlockSwitchgrass extends ItemBlockJaffas {
    public ItemBlockSwitchgrass(int par1) {
        super(par1);
        subNames = new String[]{"top"};
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack) {
        return getItemName() + "." + subNames[0];
    }
}