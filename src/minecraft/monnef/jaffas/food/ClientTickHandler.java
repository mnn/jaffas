package monnef.jaffas.food;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GuiScreen;

import java.util.EnumSet;

public class ClientTickHandler implements IScheduledTickHandler {
    private boolean checked = false;
    private boolean first = true;
    private int tries = 0;
    private final static int aLotOfTicks = 20 * 60 * 60 * 24;

    public static String data = null;
    public static String name = "";
    public static String clientVersionString;
    public static Thread versionThread;

    public static Object lock = new Object();

    public ClientTickHandler() {
        if (!mod_jaffas.instance.checkUpdates) {
            checked = true;
        }
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.equals(EnumSet.of(TickType.RENDER))) {
            onRenderTick();
        } else if (type.equals(EnumSet.of(TickType.CLIENT))) {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            if (guiscreen != null) {
                onTickInGUI(guiscreen);
            } else {
                onTickInGame();
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
        // In my testing only RENDER, CLIENT, & PLAYER did anything on the client side.
        // Read 'cpw.mods.fml.common.TickType.java' for a full list and description of available types
    }

    @Override
    public String getLabel() {
        return null;
    }

    public void onRenderTick() {
    }

    public void onTickInGUI(GuiScreen guiscreen) {
    }

    public void onTickInGame() {
        if (!checked && tries < 2) {
            if (mod_jaffas.debug) {
                System.out.println("Doing version check");
            }

            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                if (mod_jaffas.debug) {
                    System.out.println("got null player O_0");
                }
                return;
            }

            synchronized (lock) {
                name = player.username;
                clientVersionString = mod_jaffas.class.getAnnotation(Mod.class).version();
                if (data == null) {
                    if ((versionThread != null && !versionThread.isAlive()) || versionThread == null) {
                        if (mod_jaffas.debug) System.out.println("starting version thread");
                        versionThread = new Thread(new ThreadVersionCheck(), "versionCheck");
                        versionThread.start();
                    } else {
                        if (mod_jaffas.debug) System.out.println("version thread alive but data field is empty");
                    }
                }

                tries++;
                if (data != null) {
                    Integer[] remoteVersion = VersionHelper.GetVersionNumbers(data);
                    data = null; // reset data to signal they're no longer valid
                    if (remoteVersion != null) {
                        Integer[] thisVersion = VersionHelper.GetVersionNumbers(clientVersionString);
                        int cmp = VersionHelper.CompareVersions(thisVersion, remoteVersion);
                        if (cmp == -1) {
                            player.addChatMessage("New version (" + VersionHelper.VersionToString(remoteVersion) + ") of \"Jaffas and more!\" is available.");
                        } else if (cmp == 1) {
                            player.addChatMessage("Local version is newer than remote, did you forget to update version file?");
                        } else {
                            if (mod_jaffas.debug) {
                                player.addChatMessage("[DEBUG] Version check is OK.");
                            }
                        }

                        checked = true;
                    }
                }
            } // sync
        }
    }


    @Override
    public int nextTickSpacing() {
        if (first) {
            first = Minecraft.getMinecraft().currentScreen != null ? true : false;
            return 20 * 5;
        }

        if (tries >= 5) {
            return aLotOfTicks;
        }

        return checked ? aLotOfTicks : 20 * 10;
    }
}