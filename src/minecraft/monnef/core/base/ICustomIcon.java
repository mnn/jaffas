package monnef.core.base;

import net.minecraft.util.Icon;

public interface ICustomIcon {
    String getModName();

    int getDefaultSheetNumber();

    void setCustomIconIndex(int index);

    int getCustomIconIndex();

    void setSheetNumber(int index);

    int getSheetNumber();

    String getCustomIconName();

    int getIconsCount();

    void setIconsCount(int iconsCount);

    Icon getCustomIcon(int index);
}
