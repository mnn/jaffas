/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.core.MonnefCorePlugin;
import monnef.core.power.PowerValues;
import monnef.core.utils.ColorEnum;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.DyeColor;
import monnef.core.utils.DyeHelper;
import monnef.jaffas.power.JaffasPower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ItemWindTurbine extends ItemPower {
    public static final int BASIC_COLOURS_COUNT = 16;
    private final int model;
    private static final String COLOR_TAG = "turbineColor";
    private boolean checkBack;
    private int radius;
    private boolean usesColoring;
    private float rotationSpeedPerTick;
    private float maximalEnergyPerRainyTick;
    private float normalStepSize;
    private float rainStepCoef;
    private float stormStepCoef;
    private float randomChangeChance;
    private int speedChangeCoolDownMin;
    private int speedChangeCoolDownMax;
    private int speedChangeInRainCoolDownMin;
    private int speedChangeInRainCoolDownMax;

    public ItemWindTurbine(int textureIndex, int durability, int model) {
        super(textureIndex);
        this.model = model;
        setMaxDamage(durability);
        setMaxStackSize(1);
    }

    public void configure(boolean checkBack, int radius, boolean usesColoring, float rotationSpeedPerTick, float maximalEnergyPerRainyTick) {
        this.checkBack = checkBack;
        this.radius = radius;
        this.usesColoring = usesColoring;
        this.rotationSpeedPerTick = rotationSpeedPerTick;
        this.maximalEnergyPerRainyTick = maximalEnergyPerRainyTick * PowerValues.totalPowerGenerationCoef();
    }

    public float getMaximalEnergyPerRainyTick() {
        return maximalEnergyPerRainyTick;
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
        if (!usesColoring) return ColorHelper.getInt(ColorEnum.WHITE);
        initNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(COLOR_TAG)) {
            return DyeHelper.getIntColor(DyeColor.WHITE);
        }
        return tag.getInteger(COLOR_TAG);
    }

    public void setTurbineColor(ItemStack stack, int color) {
        initNBT(stack);
        stack.getTagCompound().setInteger(COLOR_TAG, color);
    }

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        if (this != JaffasPower.windTurbineMill) result.add(BETA_WARNING_TEXT());
        result.add(String.format("Radius: \u00A7f%s.5m\u00A7r", getRadius()));
        if (usesColoring)
            result.add(String.format("Color: \u00A7f%s\u00A7r", ColorHelper.getColor(getTurbineColor(stack)).formatTextOrHex()));
    }

    public float getRotationSpeedPerTick() {
        return rotationSpeedPerTick;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        if (!usesColoring) {
            super.getSubItems(item, tab, list);
        } else {
            for (int i = 0; i < BASIC_COLOURS_COUNT; i++) {
                list.add(constructColoredTurbine(i));
                if (MonnefCorePlugin.debugEnv) {
                    ItemStack damagedTurb = constructColoredTurbine(i);
                    damagedTurb.setItemDamage(damagedTurb.getMaxDamage() - 2);
                    list.add(damagedTurb);
                }
            }
        }
    }

    public ItemStack constructColoredTurbine(int colorNumber) {
        ItemStack item = new ItemStack(this);
        setTurbineColor(item, DyeHelper.getIntColor(colorNumber));
        return item;
    }

    public void setupStep(float normalStepSize, float rainStepCoef, float stormStepCoef, float randomChangeChance, int speedChangeCoolDownMin, int speedChangeCoolDownMax, int speedChangeInRainCoolDownMin, int speedChangeInRainCoolDownMax) {
        this.normalStepSize = normalStepSize;
        this.rainStepCoef = rainStepCoef;
        this.stormStepCoef = stormStepCoef;
        this.randomChangeChance = randomChangeChance;
        this.speedChangeCoolDownMin = speedChangeCoolDownMin;
        this.speedChangeCoolDownMax = speedChangeCoolDownMax;
        this.speedChangeInRainCoolDownMin = speedChangeInRainCoolDownMin;
        this.speedChangeInRainCoolDownMax = speedChangeInRainCoolDownMax;
    }

    public float getNormalStepSize() {
        return normalStepSize;
    }

    public float getRainStepCoef() {
        return rainStepCoef;
    }

    public float getStormStepCoef() {
        return stormStepCoef;
    }

    public float getRandomChangeChance() {
        return randomChangeChance;
    }

    public int getSpeedChangeCoolDownMin() {
        return speedChangeCoolDownMin;
    }

    public int getSpeedChangeCoolDownMax() {
        return speedChangeCoolDownMax;
    }

    public int getSpeedChangeInRainCoolDownMin() {
        return speedChangeInRainCoolDownMin;
    }

    public int getSpeedChangeInRainCoolDownMax() {
        return speedChangeInRainCoolDownMax;
    }
}
