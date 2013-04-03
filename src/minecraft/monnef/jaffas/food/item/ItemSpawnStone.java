package monnef.jaffas.food.item;

import monnef.core.utils.MathHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.server.SpawnStoneServerPacketSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

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
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
        //FMLCommonHandler.instance().showGuiScreen(new GuiSpawnStone(player, coolDown));

        if (!par2World.isRemote) {
            SpawnStoneServerPacketSender.sendSyncPacket((EntityPlayerMP) player, true);
        }

        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(getCoolDownText(par2EntityPlayer));
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
            if (JaffasFood.spawnStoneMultidimensional) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, 0);
            } else {
                player.addChatMessage("Home stone works only in overworld.");
                return;
            }
        }

        /*
        ChunkCoordinates bed = player.getBedLocation();
        ChunkCoordinates spawn = bed;
        String type = "b";
        if (spawn == null) {
            spawn = world.getSpawnPoint();
            type += "S";
        } else {
            if (homeWorld) { // bed spawn position method malfunctions after transfer from nether, ignore it
                spawn = Block.bed.getBedSpawnPosition(world, spawn.posX, spawn.posY, spawn.posZ, player);
                type += "B";
                if (spawn == null) {
                    spawn = bed;
                    type += "b";
                } else {
                    spawn.posY += .5f;
                }
            } else {
                spawn.posY += 2f;
            }
        }
        */

        ChunkCoordinates bed = player.getBedLocation();
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
            world.playSoundEffect(player.posX, player.posY, player.posZ, "whoosh", 1f, 1f);

            Log.printInfo(player.getEntityName() + " used home stone, porting to: " + bed.posX + ", " + bed.posY + ", " + bed.posZ);

            player.playerNetServerHandler.setPlayerLocation(bed.posX + 0.5f, bed.posY + 1.1f, bed.posZ + 0.5f, player.rotationYaw, player.rotationPitch);

            player.motionX = player.motionZ = 0;
            player.motionY = 0.1;

            world.playSoundEffect(player.posX, player.posY, player.posZ, "whoosh", 1f, 1f);
            CoolDownRegistry.setCoolDown(player.getEntityName(), SPAWN_STONE, stone.getCoolDownInMinutes() * 60);
        }

        SpawnStoneServerPacketSender.sendSyncPacket(player, false);
    }

    private boolean checkRoomForPlayer(World world, ChunkCoordinates spawn) {
        return world.getBlockId(spawn.posX, spawn.posY + 1, spawn.posZ) == 0 && world.getBlockId(spawn.posX, spawn.posY + 2, spawn.posZ) == 0;
    }
}
