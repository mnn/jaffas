package monnef.jaffas.food.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPie extends ItemBlock {
    private static final String[] subNames;

    static {
        subNames = new String[TileEntityPie.PieType.values().length];
        for (int i = 0; i < subNames.length; i++) {
            subNames[i] = TileEntityPie.PieType.values()[i].toString().toLowerCase();
        }
    }

    public ItemBlockPie(int par1) {
        super(par1);
        setHasSubtypes(true);
        setItemName("itemBlockJPie");
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack) {
        return getItemName() + "." + subNames[itemstack.getItemDamage()];
    }
}
