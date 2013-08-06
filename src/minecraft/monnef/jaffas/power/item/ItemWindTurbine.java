/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.DyeHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemWindTurbine extends ItemPower {
    private final int model;
    private static final String COLOR_TAG = "turbineColor";
    private boolean checkBack;
    private int radius;
    private boolean usesColoring;
    private float rotationSpeedPerTick;

    public ItemWindTurbine(int id, int textureIndex, int durability, int model) {
        super(id, textureIndex);
        this.model = model;
        setMaxDamage(durability);
        setMaxStackSize(1);
    }

    public void configure(boolean checkBack, int radius, boolean usesColoring, float rotationSpeedPerTick) {
        this.checkBack = checkBack;
        this.radius = radius;
        this.usesColoring = usesColoring;
        this.rotationSpeedPerTick = rotationSpeedPerTick;
    }

    public int getModel() {
        return model;
    }

    public boolean doesCheckBack() {
        return checkBack;
    }

    public int getRadius() {
        return radius;
    }

    public boolean doesUseColoring() {
        return usesColoring;
    }

    public int getTurbineColor(ItemStack stack) {
        if (!usesColoring) return ColorHelper.WHITE_INT;
        initNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(COLOR_TAG)) {
            return ColorHelper.WHITE_INT;
        }
        return tag.getInteger(COLOR_TAG);
    }

    public void setTurbineColor(ItemStack stack, int color) {
        initNBT(stack);
        stack.getTagCompound().setInteger(COLOR_TAG, color);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(String.format("Radius: §f%s.5m§r", getRadius()));
        if (usesColoring)
            list.add(String.format("Color: §f%sm§r", ColorHelper.getColor(getTurbineColor(stack)).formatHex()));
    }

    public float getRotationSpeedPerTick() {
        return rotationSpeedPerTick;
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        super.getSubItems(id, tab, list);
        if (!usesColoring) return;
        for (int i = 1; i < 16; i++) {
            ItemStack item = new ItemStack(id, 1, 0);
            setTurbineColor(item, DyeHelper.getIntColor(16 - i));
            list.add(item);
        }
    }
}
