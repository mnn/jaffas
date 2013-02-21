package monnef.jaffas.food.common;

import monnef.jaffas.food.Log;
import monnef.jaffas.food.client.ClientTickHandler;
import monnef.jaffas.food.mod_jaffas_food;

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
