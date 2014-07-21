/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.xmas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemBlockPresent extends ItemBlockXmasMulti {
    public static final int count = 12;
    public static final String[] titles = {"White Present", "Blue Present", "Yellow Present", "Black Present", "Magenta Present", "Present",
            "Small White Present", "Small Blue Present", "Small Yellow Present", "Small Black Present", "Small Magenta Present", "Small Present"};

    public ItemBlockPresent() {
        super();
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
    public IIcon getIconFromDamage(int meta) {
        int iconNum = MathHelper.clamp_int(meta, 0, count);
        return getCustomIcon(iconNum % (count / 2));
    }
}
