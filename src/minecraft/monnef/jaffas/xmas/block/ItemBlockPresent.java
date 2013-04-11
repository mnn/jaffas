/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.xmas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemBlockPresent extends ItemBlockXmasMulti {
    public static final int count = 12;
    public static final String[] titles = {"White Present", "Blue Present", "Yellow Present", "Black Present", "Magenta Present", "Present",
            "Small White Present", "Small Blue Present", "Small Yellow Present", "Small Black Present", "Small Magenta Present", "Small Present"};

    public ItemBlockPresent(int id) {
        super(id);
        setCustomIconIndex(0);
    }

    @Override
    public String[] getSubNames() {
        return new String[]{"white", "blue", "yellow", "black", "magenta", "question",
                "whiteSmall", "blueSmall", "yellowSmall", "blackSmall", "magentaSmall", "questionSmall"};
    }

    @Override
    public String[] getSubTitles() {
        return titles;
    }

    @Override
    public BlockXmasMulti getParentBlock() {
        return JaffasXmas.BlockPresent;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, count);
        return icons[(var2 % (count / 2))];
    }
}
