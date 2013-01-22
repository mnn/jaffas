package monnef.core;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityHelper {
    public static boolean AnimalIsAdult(EntityLiving animal) {
        return animal instanceof EntityAnimal ? ((EntityAnimal) animal).getGrowingAge() >= 0 : true;
    }

}
