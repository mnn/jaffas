package monnef.jaffas.food.common;

import monnef.jaffas.food.client.ClientTickHandler;
import monnef.jaffas.food.mod_jaffas_food;

import static monnef.jaffas.food.mod_jaffas_food.Log;

public class ThreadVersionCheck implements Runnable {
    @Override
    public void run() {
        if (mod_jaffas_food.debug) Log.printInfo("data filling started");
        synchronized (ClientTickHandler.lock) {
            ClientTickHandler.data = VersionHelper.GetVersionText(ClientTickHandler.name, ClientTickHandler.clientVersionString);
        }
        if (mod_jaffas_food.debug) Log.printInfo("data filled");
    }
}
