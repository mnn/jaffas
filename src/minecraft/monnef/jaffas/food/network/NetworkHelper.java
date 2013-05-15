/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

public class NetworkHelper {

    public static void sendToServer(Packet packet) {
        PacketDispatcher.sendPacketToServer(packet);
    }

    public static void sendToAllAround(Entity entity, double range, Packet packet) {
        sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet);
    }

    public static void sendToAllAround(double x, double y, double z, int dim, double range, Packet packet) {
        PacketDispatcher.sendPacketToAllAround(x, y, z, range, dim, packet);
    }

    public static void sendToClient(Packet packet, EntityPlayer player) {
        PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
    }
}
