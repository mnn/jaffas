package monnef.jaffas.power;

import monnef.core.event.LightningGeneratedEvent;
import net.minecraftforge.event.ForgeSubscribe;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class PowerEventHandler {
    @ForgeSubscribe
    public void HandleLightning(LightningGeneratedEvent event) {
        Log.printDebug(String.format("Lightning detected at %d, %d, %d.", event.x, event.y, event.z));
    }
}
