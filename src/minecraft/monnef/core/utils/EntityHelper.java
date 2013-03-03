package monnef.core.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityHelper {
    public static boolean animalIsAdult(EntityLiving animal) {
        return animal instanceof EntityAnimal ? ((EntityAnimal) animal).getGrowingAge() >= 0 : true;
    }

    public static String formatCoordinates(Entity e) {
        StringBuilder s = new StringBuilder();
        s.append(e.posX);
        s.append(", ");
        s.append(e.posY);
        s.append(", ");
        s.append(e.posZ);
        return s.toString();
    }
}
