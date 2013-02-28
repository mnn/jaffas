package monnef.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class CloakHookHandler {
    private static ArrayList<ICloakHandler> listeners = new ArrayList<ICloakHandler>();

    public static void handleUpdateCloak(Entity entity) {
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        for (ICloakHandler handler : listeners) {
            handler.handleCloak(player, player.getEntityName());
        }
    }

    public static void registerCloakHandler(ICloakHandler handler) {
        listeners.add(handler);
    }
}
