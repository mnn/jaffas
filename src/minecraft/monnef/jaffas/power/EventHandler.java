package monnef.jaffas.power;

import monnef.core.EntityHelper;
import monnef.jaffas.food.Log;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHandler {
    @ForgeSubscribe
    public void HandleNewEntity(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityLightningBolt) {
            Log.printInfo("Lightning detected at - " + EntityHelper.formatCoordinates(event.entity));
        }
    }
}
