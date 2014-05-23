/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.food.common.SpawnStonePacketUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.net.ProtocolException;

import static monnef.jaffas.food.JaffasFood.Log;

public class JaffasPacketHandler implements IPacketHandler {
    public static final String CHANNEL_SpawnStone = "jaffas-01-sstone";
    public static final String CHANNEL_Generic = "jaffas-02-gen";

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload rawPacket, Player player) {
        if (rawPacket.channel.equals(CHANNEL_SpawnStone)) {
            SpawnStonePacketUtils.HandleSpawnStone(rawPacket, player);
        } else {
            // generic packet
            try {
                EntityPlayer entityPlayer = (EntityPlayer) player;
                ByteArrayDataInput in = ByteStreams.newDataInput(rawPacket.data);
                int packetId = in.readUnsignedByte();
                JaffasPacket jaffasPacket = JaffasPacket.constructPacket(packetId);
                jaffasPacket.read(in);
                jaffasPacket.execute(entityPlayer, entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
            } catch (ProtocolException e) {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Protocol Exception!");
                    Log.printWarning("Player " + ((EntityPlayer) player).username + " caused a Protocol Exception!");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
            }
        }
    }
}
