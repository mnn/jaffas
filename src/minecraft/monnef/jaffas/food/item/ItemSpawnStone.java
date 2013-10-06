/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.core.utils.MathHelper;
import monnef.jaffas.food.client.Sounds;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.server.SpawnStoneServerPacketSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class ItemSpawnStone extends ItemJaffaBase {
    private int coolDown;

    public int getCoolDownInMinutes() {
        return coolDown;
    }

    public ItemSpawnStone(JaffaItemInfo info, int coolDown) {
        super(info.getId());
        this.coolDown = coolDown;
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            SpawnStoneServerPacketSender.sendSyncPacket(player, true);
        }

        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformation(stack, player, result, par4);
        result.add(getCoolDownText(player));
    }

    public static String getCoolDownText(EntityPlayer par2EntityPlayer) {
        int cd = CoolDownRegistry.getRemainingCoolDownInSeconds(par2EntityPlayer.getEntityName(), SPAWN_STONE);
        StringBuilder info = new StringBuilder();

        if (cd == 0) {
            info.append("Stone is ready");
        } else {
            info.append("Cool-down remaining: ");
            info.append(MathHelper.oneDecimalPlace.format(cd / 60f));
            info.append("m");
        }
        return info.toString();
    }

    public void doTeleportAndSetCD(EntityPlayerMP player, ItemSpawnStone stone) {
        World world = player.worldObj;
        if (world.isRemote) return;

        boolean homeWorld = player.dimension == 0;
        if (!homeWorld) {
            if (ConfigurationManager.spawnStoneMultidimensional) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, 0);
            } else {
                player.addChatMessage("Home stone works only in overworld.");
                return;
            }
        }

        ChunkCoordinates bed = player.getBedLocation(0);
        boolean success;
        if (bed == null) {
            player.addChatMessage("You have no home.");
            success = false;
        } else {
            if (checkRoomForPlayer(player.worldObj, bed)) {
                success = true;
            } else {
                player.addChatMessage("Cannot find free space.");
                success = false;
            }
        }

        if (success) {
            playWhooshEffect(player, world);

            Log.printInfo(player.getEntityName() + " used home stone, porting to: " + bed.posX + ", " + bed.posY + ", " + bed.posZ);

            ((WorldServer) player.worldObj).theChunkProviderServer.loadChunk((int) player.posX >> 4, (int) player.posZ >> 4);
            player.playerNetServerHandler.setPlayerLocation(bed.posX + 0.5f, bed.posY + 1.1f, bed.posZ + 0.5f, player.rotationYaw, player.rotationPitch);
            player.fallDistance = 0;
            player.motionX = player.motionZ = 0;
            player.motionY = 0.2;

            playWhooshEffect(player, world);
            CoolDownRegistry.setCoolDown(player.getEntityName(), SPAWN_STONE, stone.getCoolDownInMinutes() * 60);
        }

        SpawnStoneServerPacketSender.sendSyncPacket(player, false);
    }

    private void playWhooshEffect(EntityPlayerMP player, World world) {
        world.playSoundEffect(player.posX, player.posY, player.posZ, Sounds.SoundsEnum.HOMESTONE.getSoundName(), 1f, 1f);
    }

    private boolean checkRoomForPlayer(World world, ChunkCoordinates spawn) {
        return world.getBlockId(spawn.posX, spawn.posY + 1, spawn.posZ) == 0 && world.getBlockId(spawn.posX, spawn.posY + 2, spawn.posZ) == 0;
    }
}
