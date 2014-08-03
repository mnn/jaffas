/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.item;

import monnef.core.utils.MathHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.client.Sounds;
import monnef.jaffas.food.common.ConfigurationManager;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.network.HomeStonePacket;
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
        super(info.getIconIndex());
        this.coolDown = coolDown;
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            HomeStonePacket.sendSyncPacket(player, true);
        }

        return stack;
    }

    @Override
    public void addInformationCustom(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformationCustom(stack, player, result, par4);
        result.add(getCoolDownText(player));
    }

    public static String getCoolDownText(EntityPlayer player) {
        int cd = CoolDownRegistry.getRemainingCoolDownInSeconds(player.getUniqueID(), SPAWN_STONE);
        StringBuilder info = new StringBuilder();

        if (cd == 0) {
            info.append("Stone is ready");
        } else {
            info.append("Cool-down remaining: ");
            info.append(MathHelper.oneDecimalPlace().format(cd / 60f));
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
                PlayerHelper.addMessage(player, "Home stone works only in overworld.");
                return;
            }
        }

        ChunkCoordinates bed = player.getBedLocation(0);
        boolean success;
        if (bed == null) {
            PlayerHelper.addMessage(player, "You have no home.");
            success = false;
        } else {
            if (checkRoomForPlayer(player.worldObj, bed)) {
                success = true;
            } else {
                PlayerHelper.addMessage(player, "Cannot find free space.");
                success = false;
            }
        }

        if (success) {
            playWhooshEffect(player, world);

            Log.printInfo(player.getDisplayName() + " (" + player.getUniqueID() + ") used home stone, porting to: " + bed.posX + ", " + bed.posY + ", " + bed.posZ);

            ((WorldServer) player.worldObj).theChunkProviderServer.loadChunk((int) player.posX >> 4, (int) player.posZ >> 4);
            player.playerNetServerHandler.setPlayerLocation(bed.posX + 0.5f, bed.posY + 1.1f, bed.posZ + 0.5f, player.rotationYaw, player.rotationPitch);
            player.fallDistance = 0;
            player.motionX = player.motionZ = 0;
            player.motionY = 0.2;

            playWhooshEffect(player, world);
            CoolDownRegistry.setCoolDown(player.getUniqueID(), SPAWN_STONE, stone.getCoolDownInMinutes() * 60);
        }

        HomeStonePacket.sendSyncPacket(player, false);
    }

    private void playWhooshEffect(EntityPlayerMP player, World world) {
        world.playSoundEffect(player.posX, player.posY, player.posZ, Sounds.SoundsEnum.HOMESTONE.getSoundName(), 1f, 1f);
    }

    private boolean checkRoomForPlayer(World world, ChunkCoordinates spawn) {
        return world.isAirBlock(spawn.posX, spawn.posY + 1, spawn.posZ) && world.isAirBlock(spawn.posX, spawn.posY + 2, spawn.posZ);
    }
}
