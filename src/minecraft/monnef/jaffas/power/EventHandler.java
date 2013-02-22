package monnef.jaffas.power;

import monnef.core.EntityHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class EventHandler {
    @ForgeSubscribe
    public void HandleNewEntity(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityLightningBolt) {
            Log.printInfo("Lightning detected at - " + EntityHelper.formatCoordinates(event.entity));
        }
    }
}
