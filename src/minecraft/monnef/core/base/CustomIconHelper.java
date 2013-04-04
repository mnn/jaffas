package monnef.core.base;

import monnef.core.Reference;
import net.minecraft.util.Icon;

public class CustomIconHelper {
    public static String generateShiftedId(ICustomIcon obj, int shift) {
        return generateId(obj, obj.getCustomIconIndex() + shift);
    }

    public static String generateId(ICustomIcon obj) {
        return generateId(obj, obj.getCustomIconIndex());
    }

    public static String generateId(ICustomIcon obj, int index) {
        if (obj.getModName().equals(Reference.ModId)) {
            throw new RuntimeException("wrong import, class: " + obj.getClass().getSimpleName());
        }

        StringBuilder sb = new StringBuilder(obj.getModName());
        sb.append(":");

        if (obj.getCustomIconName() == null) {
            sb.append(String.format("%02d_%03d", obj.getSheetNumber(), index));
        } else {
            sb.append(String.format("%s", obj.getCustomIconName()));
        }

        return sb.toString();
    }

    public static String generateId(String modName, int sheetNumber, int customIconIndex) {
        return generateId(new DummyCustomIconProvider(modName, sheetNumber, customIconIndex));
    }

    public static String generateCustomId(String customIconName) {
        return generateId(new DummyCustomIconProvider(customIconName));
    }

    private static class DummyCustomIconProvider implements ICustomIcon {
        private String modName;
        private int sheetNumber;
        private int customIconIndex;
        private String customIconName;

        public DummyCustomIconProvider(String modName, int sheetNumber, int customIconIndex) {
            this.modName = modName;
            this.sheetNumber = sheetNumber;
            this.customIconIndex = customIconIndex;
        }

        public DummyCustomIconProvider(String customIconName) {
            this.customIconName = customIconName;
        }

        @Override
        public String getModName() {
            return modName;
        }

        @Override
        public int getDefaultSheetNumber() {
            return -1;
        }

        @Override
        public int getSheetNumber() {
            return sheetNumber;
        }

        @Override
        public void setSheetNumber(int sheetNumber) {
            this.sheetNumber = sheetNumber;
        }

        @Override
        public int getCustomIconIndex() {
            return customIconIndex;
        }

        @Override
        public void setCustomIconIndex(int customIconIndex) {
            this.customIconIndex = customIconIndex;
        }

        @Override
        public String getCustomIconName() {
            return customIconName;
        }

        @Override
        public int getIconsCount() {
            return 1;
        }

        @Override
        public void setIconsCount(int iconsCount) {
        }

        @Override
        public Icon getCustomIcon(int index) {
            return null;
        }
    }
}
