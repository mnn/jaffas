package monnef.jaffas.food.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockJaffas extends ItemBlock {
    protected String[] subNames;

    public ItemBlockJaffas(int par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemDisplayName(ItemStack itemstack) {
        String subName = itemstack.getItemDamage() >= subNames.length ? "STRING NOT FOUND" : subNames[itemstack.getItemDamage()];
        return getUnlocalizedName() + "." + subName;
    }
}
