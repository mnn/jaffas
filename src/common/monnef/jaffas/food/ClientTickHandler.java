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
        //System.out.println("onRenderTick");
        //TODO: Your Code Here
    }

    public void onTickInGUI(GuiScreen guiscreen) {
        //System.out.println("onTickInGUI");
        //TODO: Your Code Here
    }

    public void onTickInGame() {
        if (!checked && tries < 2) {
            if (mod_jaffas.debug) {
                System.out.println("Doing version check");
            }

            String data = "";
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                if (mod_jaffas.debug) {
                    System.out.println("got null player O_0");
                }
                return;
            }

            String name = player.username;

            String clientVersionString = mod_jaffas.class.getAnnotation(Mod.class).version();
            data = VersionHelper.GetVersionText(name, clientVersionString);
            tries++;
            if (data != null) {
                Integer[] remoteVersion = VersionHelper.GetVersionNumbers(data);
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
        }
    }


    @Override
    public int nextTickSpacing() {
        if (first) {
            first = Minecraft.getMinecraft().currentScreen != null ? true : false;
            return 20 * 2;
        }

        if (tries >= 2) {
            return aLotOfTicks;
        }

        return checked ? aLotOfTicks : 20 * 60 * 1;
    }
}