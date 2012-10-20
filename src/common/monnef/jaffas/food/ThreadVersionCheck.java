package monnef.jaffas.food;

public class ThreadVersionCheck implements Runnable {
    @Override
    public void run() {
        if (mod_jaffas.debug) System.out.println("data filling started");
        synchronized (ClientTickHandler.lock) {
            ClientTickHandler.data = VersionHelper.GetVersionText(ClientTickHandler.name, ClientTickHandler.clientVersionString);
        }
        if (mod_jaffas.debug) System.out.println("data filled");
    }
}
