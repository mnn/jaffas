package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

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
        setMaxStackSize(16);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack) {
        return getItemName() + "." + subNames[itemstack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, BlockPie.textureIndexFromMeta.length);
        return BlockPie.textureIndexFromMeta[var2];
    }
}
