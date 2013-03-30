package monnef.jaffas.food.common;

import monnef.jaffas.food.client.ClientTickHandler;
import monnef.jaffas.food.jaffasFood;

import static monnef.jaffas.food.jaffasFood.Log;

public class ThreadVersionCheck implements Runnable {
    @Override
    public void run() {
        if (jaffasFood.debug) Log.printInfo("data filling started");
        synchronized (ClientTickHandler.lock) {
            ClientTickHandler.data = VersionHelper.GetVersionText(ClientTickHandler.name, ClientTickHandler.clientVersionString);
        }
        if (jaffasFood.debug) Log.printInfo("data filled");
    }
}
