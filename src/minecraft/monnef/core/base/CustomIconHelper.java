package monnef.core.base;

import monnef.core.Reference;

public class CustomIconHelper {
    public static String generateId(ICustomIcon obj) {
        if(obj.getModId().equals(Reference.ModId)){
            throw new RuntimeException("wrong import");
        }

        StringBuilder sb = new StringBuilder(obj.getModId());
        sb.append(":");

        if (obj.customIconName() == null) {
            sb.append(String.format("%2d_%3d", obj.getSheetNumber(), obj.getCustomIconIndex()));
        } else {
            sb.append(String.format("%s", obj.customIconName()));
        }

        return sb.toString();
    }
}
