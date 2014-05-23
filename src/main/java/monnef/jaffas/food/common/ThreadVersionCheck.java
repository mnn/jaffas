/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.ClientTickHandler;

import static monnef.jaffas.food.JaffasFood.Log;

public class ThreadVersionCheck implements Runnable {
    @Override
    public void run() {
        if (JaffasFood.debug) Log.printInfo("data filling started");
        synchronized (ClientTickHandler.lock) {
            ClientTickHandler.data = VersionHelper.getVersionText(ClientTickHandler.name, ClientTickHandler.clientVersionString);
        }
        if (JaffasFood.debug) Log.printInfo("data filled");
    }
}
