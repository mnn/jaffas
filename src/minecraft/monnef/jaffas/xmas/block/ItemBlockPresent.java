package monnef.jaffas.xmas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.jaffas.xmas.JaffasXmas;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemBlockPresent extends ItemBlockXmasMulti {
    public static final int count = 12;

    public ItemBlockPresent(int id) {
        super(id);
    }

    @Override
    protected String[] getSubNames() {
        return new String[]{"white", "blue", "yellow", "black", "magenta", "question",
                "whiteSmall", "blueSmall", "yellowSmall", "blackSmall", "magentaSmall", "questionSmall"};
    }

    @Override
    protected String[] getSubTitles() {
        return new String[]{"White Present", "Blue Present", "Yellow Present", "Black Present", "Magenta Present", "Present",
                "Small White Present", "Small Blue Present", "Small Yellow Present", "Small Black Present", "Small Magenta Present", "Small Present"};
    }

    @Override
    protected BlockXmasMulti getParentBlock() {
        return JaffasXmas.BlockPresent;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1) {
        int var2 = MathHelper.clamp_int(par1, 0, count);
        return null;
        //return this.iconIndex + (var2 % (count / 2));
    }
}
