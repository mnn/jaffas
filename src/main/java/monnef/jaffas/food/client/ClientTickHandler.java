/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import monnef.core.common.ScheduledTicker;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.Reference;
import monnef.jaffas.food.common.ThreadVersionCheck;
import monnef.jaffas.food.common.VersionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import static monnef.jaffas.food.JaffasFood.Log;

public class ClientTickHandler extends ScheduledTicker {
    private static final String monnefUUID = "b208f35f-7358-4da8-a73e-d625fce4cc32";

    private boolean checked = false;
    private boolean first = true;
    private int tries = 0;
    private final static int aLotOfTicks = 20 * 60 * 60 * 24;

    public static String data = null;
    public static String playerUUID = "";
    public static String clientVersionString;
    public static Thread versionThread;

    public static Object lock = new Object();

    public static String cachedVersionString;

    public ClientTickHandler() {
    }

    @Override
    public void onClientTickEnd() {
        GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
        if (guiscreen != null) {
            onTickInGUI(guiscreen);
        } else {
            onTickInGame();
        }
    }

    public void onTickInGUI(GuiScreen guiscreen) {
    }

    public void onTickInGame() {
        if (!checked && tries < 2) {
            if (JaffasFood.debug) {
                Log.printInfo("Doing version check");
            }

            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                if (JaffasFood.debug) {
                    Log.printInfo("got null player O_0");
                }
                return;
            }

            synchronized (lock) {
                playerUUID = player.getUniqueID().toString();
                clientVersionString = JaffasFood.class.getAnnotation(Mod.class).version();
                if (data == null) {
                    if ((versionThread != null && !versionThread.isAlive()) || versionThread == null) {
                        if (JaffasFood.debug) Log.printInfo("starting version thread");
                        versionThread = new Thread(new ThreadVersionCheck(), "versionCheck");
                        versionThread.start();
                    } else {
                        if (JaffasFood.debug) Log.printInfo("version thread alive but data field is empty");
                    }
                }

                tries++;
                if (data != null) {
                    Integer[] remoteVersion = VersionHelper.GetVersionNumbers(data);
                    data = null; // reset data to signal they're no longer valid
                    if (remoteVersion != null) {
                        Integer[] thisVersion = VersionHelper.GetVersionNumbers(clientVersionString);
                        int cmp = VersionHelper.compareVersions(thisVersion, remoteVersion);
                        String versionString = VersionHelper.versionToString(remoteVersion);
                        cachedVersionString = versionString;
                        if (cmp == -1) {
                            if (!ConfigurationManager.lastVersionShown.equals(versionString)) {
                                Configuration config = JaffasFood.instance.config;
                                try {
                                    config.get(Configuration.CATEGORY_GENERAL, JaffasFood.LAST_VERSION_SHOWN, "").set(versionString);
                                    config.save();
                                } catch (Exception e) {
                                    Log.printSevere("Problem while writing new version to the config.");
                                    e.printStackTrace();
                                }
                                if (ConfigurationManager.showUpdateMessages) {
                                    showNewVersionMessage(player);
                                } else {
                                    Log.printInfo("New version available, but messages are disabled.");
                                }
                            } else {
                                Log.printInfo("New version available, but message was already once shown, skipping.");
                            }
                        } else if (cmp == 1) {
                            if (playerUUID.toLowerCase().equals(monnefUUID)) {
                                PlayerHelper.addMessage(player, String.format(
                                        "Local version - \u00A76%s\u00A7r is newer than remote - \u00A76%s\u00A7r, did you forget to update version file? ",
                                        VersionHelper.versionToString(thisVersion),
                                        VersionHelper.versionToString(remoteVersion)
                                ));
                            }
                            Log.printInfo("Remote version is older than yours, ignoring.");
                        } else {
                            Log.printInfo("Version check is OK.");
                        }

                        checked = true;
                    }
                }
            } // sync
        }
    }

    public static boolean isVersionStringReady() {
        return cachedVersionString != null;
    }

    private void showNewVersionMessage(EntityClientPlayerMP player) {
        PlayerHelper.addMessage(player, "New version \u00A7e" + cachedVersionString + "\u00A7r of \"\u00A75" + Reference.ModName + "\u00A7r\" is available.");
        PlayerHelper.addMessage(player, "This message will not be shown again, if you want more details use /jam command.");
    }

    @Override
    public int nextTickSpacing(Side side) {
        if (first) {
            first = Minecraft.getMinecraft().currentScreen != null;
            return 20 * 5;
        }

        if (tries >= 5) {
            return aLotOfTicks;
        }

        return checked ? aLotOfTicks : 20 * 10;
    }
}