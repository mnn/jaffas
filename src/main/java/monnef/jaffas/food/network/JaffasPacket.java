/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.food.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import java.net.ProtocolException;

public abstract class JaffasPacket {
    private static final BiMap<Integer, Class<? extends JaffasPacket>> idMap;
    public static String CHANNEL = JaffasPacketHandler.CHANNEL_Generic;

    static {
        ImmutableBiMap.Builder<Integer, Class<? extends JaffasPacket>> builder = ImmutableBiMap.builder();

        builder.put(0, AchievementPacket.class);

        idMap = builder.build();
    }

    public abstract void write(ByteArrayDataOutput out);

    public abstract void read(ByteArrayDataInput in);

    public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;

    public static JaffasPacket constructPacket(int packetId) throws ProtocolException, IllegalAccessException, InstantiationException {
        Class<? extends JaffasPacket> clazz = idMap.get(Integer.valueOf(packetId));
        if (clazz == null) {
            throw new ProtocolException("Unknown Packet Id!");
        } else {
            return clazz.newInstance();
        }
    }

    public final int getPacketId() {
        if (idMap.inverse().containsKey(getClass())) {
            return idMap.inverse().get(getClass()).intValue();
        } else {
            throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
        }
    }

    public final Packet makePacket() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeByte(getPacketId());
        write(out);
        return PacketDispatcher.getPacket(CHANNEL, out.toByteArray());
    }
}
