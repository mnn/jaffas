package monnef.core.base;

public interface ICustomIcon {
    String getModName();

    int getDefaultSheetNumber();

    void setCustomIconIndex(int index);

    int getCustomIconIndex();

    void setSheetNumber(int index);

    int getSheetNumber();

    String getCustomIconName();
}
