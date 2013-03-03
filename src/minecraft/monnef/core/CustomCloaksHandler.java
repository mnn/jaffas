package monnef.core;

import monnef.core.utils.UrlHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomCloaksHandler implements ICloakHandler {

    private static final String JAFFA_CLOAK_URL = Reference.URL + "/skin/cloak/_get.php?name=%s";

    @Override
    public void handleCloak(EntityPlayer player, String name) {
        if (MonnefCorePlugin.debugEnv){
            name = "monnef";
        }
        String url = String.format(JAFFA_CLOAK_URL, name);
        if (UrlHelper.pageExists(url)) {
            player.cloakUrl = player.playerCloakUrl = url;
        }
        //trySetHideCape(player);
    }

    private void trySetHideCape(EntityPlayer player) {
        try {
            Method m = EntityPlayer.class.getDeclaredMethod("setHideCape", int.class, boolean.class);
            m.setAccessible(true);
            m.invoke(player, 1, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            MonnefCorePlugin.Log.printSevere("Unable to access protected method in EntityPlayer.");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            MonnefCorePlugin.Log.printSevere("Unable to access protected method in EntityPlayer.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            MonnefCorePlugin.Log.printSevere("Unable to access protected method in EntityPlayer.");
        }
    }
}
