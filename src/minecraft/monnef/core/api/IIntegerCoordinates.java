package monnef.core.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IIntegerCoordinates {
    TileEntity getTile();

    World getWorld();

    int getX();

    int getY();

    int getZ();

    void saveTo(NBTTagCompound tag, String tagName);

    void loadFrom(NBTTagCompound tag, String tagName);
}
