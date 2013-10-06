/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemTurbineBlade extends ItemPowerMulti {
    private static String[] subNames;
    private static String[] subTitles;

    public enum TurbineBladeEnum {
        millBlade("Mill Blade"),
        woodenBlade("Wooden Blade"),
        ironBlade("Iron Blade"),
        jaffarrolBlade("Jaffarrol Blade");

        private String title;

        TurbineBladeEnum(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    static {
        generateTextArrays();
    }

    private static void generateTextArrays() {
        int length = TurbineBladeEnum.values().length;
        subNames = new String[length];
        subTitles = new String[length];
        TurbineBladeEnum[] values = TurbineBladeEnum.values();
        for (int i = 0; i < length; i++) {
            TurbineBladeEnum curr = values[i];
            subNames[i] = curr.toString();
            subTitles[i] = curr.getTitle();
        }
    }

    @Override
    public String[] getSubNames() {
        return subNames;
    }

    @Override
    public String[] getSubTitles() {
        return subTitles;
    }

    public ItemTurbineBlade(int id, int textureIndex) {
        super(id, textureIndex);
        setUnlocalizedName("turbineBlade");
        setMaxStackSize(16);
    }

    public ItemStack constructBlade(TurbineBladeEnum type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformation(stack, player, result, par4);
        if (stack.getItemDamage() > 0) result.add(BETA_WARNING_TEXT);
    }
}
