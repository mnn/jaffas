package monnef.core.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class NBTHelper {
    public static ItemStack init(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack;
    }

    public static IntegerCoordinates getCoords(ItemStack stack, String compoundName) {
        init(stack);
        NBTTagCompound data = stack.getTagCompound();

        return getCoords(data, compoundName);
    }

    private static IntegerCoordinates getCoords(NBTTagCompound data, String compoundName) {
        if (!data.hasKey(compoundName)) {
            return null;
        }

        try {
            NBTTagCompound compound = data.getCompoundTag(compoundName);
            int cx = compound.getInteger("x");
            int cy = compound.getInteger("y");
            int cz = compound.getInteger("z");
            int dimm = compound.getInteger("dimm");
            World world = DimensionManager.getWorld(dimm);

            return new IntegerCoordinates(cx, cy, cz, world);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack setCoords(ItemStack stack, IntegerCoordinates coords, String compoundName) {
        init(stack);
        NBTTagCompound data = stack.getTagCompound();

        serCoords(data, coords, compoundName);
        return stack;
    }

    private static void serCoords(NBTTagCompound data, IntegerCoordinates coords, String compoundName) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("x", coords.getX());
        compound.setInteger("y", coords.getY());
        compound.setInteger("z", coords.getZ());
        compound.setInteger("dimm", coords.getWorld().provider.dimensionId);
        data.setCompoundTag(compoundName, compound);
    }

    public static boolean hasKey(ItemStack stack, String keyName) {
        init(stack);
        return stack.getTagCompound().hasKey(keyName);
    }
}
