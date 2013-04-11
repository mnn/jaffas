/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemBlockPie extends ItemBlockJaffas {
    public ItemBlockPie(int par1) {
        super(par1);

        subNames = new String[TileEntityPie.PieType.values().length];
        for (int i = 0; i < subNames.length; i++) {
            subNames[i] = TileEntityPie.PieType.values()[i].toString().toLowerCase();
        }

        setUnlocalizedName("itemBlockJPie");
        setMaxStackSize(16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, BlockPie.textureIndexFromMeta.length);
        return BlockPie.icons[var2];
    }
}
