/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemBlockPie extends ItemBlockJaffas {
    public ItemBlockPie(Block block) {
        super(block);

        subNames = new String[TilePie.PieType.values().length];
        for (int i = 0; i < subNames.length; i++) {
            subNames[i] = TilePie.PieType.values()[i].toString().toLowerCase();
        }

        setUnlocalizedName("itemBlockJPie");
        setMaxStackSize(16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        int var2 = MathHelper.clamp_int(meta, 0, BlockPie.textureIndexFromMeta.length);
        return BlockPie.icons[var2];
    }
}
