package monnef.jaffas.food.item;

import monnef.core.MathHelper;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.server.SpawnStoneServerPacketSender;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.List;

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
            //MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, 0);
            player.addChatMessage("Home stone works only in overworld.");
            return;
        }

        ChunkCoordinates spawn = player.getBedLocation();
        if (spawn == null) {
            spawn = world.getSpawnPoint();
        } else {
            if (homeWorld) { // bed spawn position method malfunctions after transfer from nether, ignore it
                spawn = Block.bed.getBedSpawnPosition(world, spawn.posX, spawn.posY, spawn.posZ, player);
            } else {
                spawn.posY += 1;
            }

            if (spawn == null) {
                spawn = world.getSpawnPoint();
            }
        }

        player.setPositionAndUpdate(spawn.posX, spawn.posY, spawn.posZ);

        CoolDownRegistry.setCoolDown(player.getEntityName(), SPAWN_STONE, stone.getCoolDownInMinutes() * 60);
        SpawnStoneServerPacketSender.sendSyncPacket(player, false);
    }
}
