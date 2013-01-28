package monnef.jaffas.food.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.food.item.ItemSpawnStone;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static cpw.mods.fml.relauncher.Side.CLIENT;
import static cpw.mods.fml.relauncher.Side.SERVER;
import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        String channel = packet.channel;
        if (channel.equals("jaffas-01-sstone")) {
            HandleSpawnStone(packet, player);
        }
    }

    private enum SpawnStonePacketType {SYNC, PORT}

    private void HandleSpawnStone(Packet250CustomPayload packet, Player player) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        // SYNC server -> client [type, cs]
        // PORT client -> server [type]

        SpawnStonePacketType packetType;
        int CDend = -1;

        try {
            byte typeNumber = inputStream.readByte();
            if (typeNumber < 0 || typeNumber >= SpawnStonePacketType.values().length) return;
            packetType = SpawnStonePacketType.values()[typeNumber];
            if (packetType == SpawnStonePacketType.SYNC) {
                inputStream.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == CLIENT && packetType == SpawnStonePacketType.SYNC) {
            EntityClientPlayerMP p = (EntityClientPlayerMP) player;
            CoolDownRegistry.synchronizeCoolDown(p.getEntityName(), SPAWN_STONE, CDend);
        } else if (side == SERVER && packetType == SpawnStonePacketType.PORT) {
            EntityPlayerMP p = (EntityPlayerMP) player;
            if (CoolDownRegistry.isCoolDownActive(p.getEntityName(), SPAWN_STONE)) {
                System.out.println("Possible hacker&|cheater (uses stone on CD): " + p.getEntityName());
                return;
            }

            ItemStack stack = p.getCurrentEquippedItem();
            if (stack == null) {
                System.out.println("Possible hacker&|cheater (uses stone without stone): " + p.getEntityName());
            }

            Item itemInHand = stack.getItem();
            if(!(itemInHand instanceof ItemSpawnStone)){
                System.out.println("Possible hacker&|cheater (uses stone without stone [di]): " + p.getEntityName());
            }

            ItemSpawnStone stone= (ItemSpawnStone) itemInHand;
            stone.doTeleportAndSetCD(p, stack);
        } else {
            return;
        }
    }
}
