package monnef.core.base;

public interface ICustomIcon {
    String getModId();

    int getDefaultSheetNumber();

    void setCustomIconIndex(int index);

    int getCustomIconIndex();

    void setSheetNumber(int index);

    int getSheetNumber();

    String customIconName();
}
