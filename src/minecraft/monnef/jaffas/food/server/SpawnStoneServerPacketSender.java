/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.common.PacketHandler;
import monnef.jaffas.food.common.SpawnStonePacketType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class SpawnStoneServerPacketSender {
    public static void sendSyncPacket(EntityPlayer player, boolean openGUI) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        int CDRemaining = CoolDownRegistry.getRemainingCoolDownInSeconds(player.getEntityName(), SPAWN_STONE);
        try {
            outputStream.writeByte(SpawnStonePacketType.SYNC.ordinal());
            outputStream.writeInt(CDRemaining);
            outputStream.writeBoolean(openGUI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = PacketHandler.channelSpawnStone;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
    }
}
