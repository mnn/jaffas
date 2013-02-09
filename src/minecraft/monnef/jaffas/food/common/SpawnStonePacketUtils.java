package monnef.jaffas.food.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.food.Log;
import monnef.jaffas.food.item.ItemSpawnStone;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.*;

import static cpw.mods.fml.relauncher.Side.CLIENT;
import static cpw.mods.fml.relauncher.Side.SERVER;
import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;
import static monnef.jaffas.food.common.SpawnStonePacketType.PORT;
import static monnef.jaffas.food.common.SpawnStonePacketType.SYNC;

public class SpawnStonePacketUtils {

    static void HandleSpawnStone(Packet250CustomPayload packet, Player player) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        // SYNC server -> client [type, timeRemaining, openGUI?]
        // PORT client -> server [type]

        SpawnStonePacketType packetType;
        int secondsRemaining = -1;
        boolean openGUI = false;

        try {
            byte typeNumber = inputStream.readByte();
            if (typeNumber < 0 || typeNumber >= SpawnStonePacketType.values().length) return;
            packetType = SpawnStonePacketType.values()[typeNumber];
            if (packetType == SYNC) {
                secondsRemaining = inputStream.readInt();
                openGUI = inputStream.readBoolean();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == CLIENT && packetType == SYNC) {
            mod_jaffas.proxy.handleSyncPacket(player, secondsRemaining, openGUI);
        } else if (side == SERVER && packetType == PORT) {
            EntityPlayerMP p = (EntityPlayerMP) player;
            if (CoolDownRegistry.isCoolDownActive(p.getEntityName(), SPAWN_STONE)) {
                Log.printWarning("Possible hacker&|cheater (uses stone on CD): " + p.getEntityName());
                return;
            }

            ItemSpawnStone stone = getSpawnStone(p);
            if (stone != null) {
                stone.doTeleportAndSetCD(p, stone);
            }
        } else {
            return;
        }
    }


    public static ItemSpawnStone getSpawnStone(EntityPlayer player) {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null) {
            Log.printWarning("Possible hacker&|cheater (uses stone without stone): " + player.getEntityName());
            return null;
        }

        Item itemInHand = stack.getItem();
        if (!(itemInHand instanceof ItemSpawnStone)) {
            Log.printWarning("Possible hacker&|cheater (uses stone without stone [di]): " + player.getEntityName());
            return null;
        }

        return (ItemSpawnStone) itemInHand;
    }

    public static void sendPortPacket() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeByte(PORT.ordinal());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = PacketHandler.channelSpawnStone;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        PacketDispatcher.sendPacketToServer(packet);
    }
}
