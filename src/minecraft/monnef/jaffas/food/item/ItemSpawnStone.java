package monnef.jaffas.food.item;

import cpw.mods.fml.common.FMLCommonHandler;
import monnef.core.MathHelper;
import monnef.jaffas.food.client.GuiSpawnStone;
import monnef.jaffas.food.common.CoolDownRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class ItemSpawnStone extends ItemJaffaBase {
    private int coolDown;

    public ItemSpawnStone(JaffaItemInfo info, int coolDown) {
        super(info.getId());
        this.coolDown = coolDown;
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
        FMLCommonHandler.instance().showGuiScreen(new GuiSpawnStone(player, coolDown));
        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        int cd = CoolDownRegistry.getRemainingCoolDownInSeconds(par2EntityPlayer.getEntityName(), SPAWN_STONE);
        StringBuilder info = new StringBuilder();

        if (cd == 0) {
            info.append("Stone is ready");
        } else {
            info.append("Cool-down remaining: ");
            info.append(MathHelper.oneDecimalPlace.format(cd / 60f));
            info.append("m");
        }

        par3List.add(info.toString());
    }

    public void doTeleportAndSetCD(EntityPlayerMP player, ItemStack stack) {
        //player.getBedLocation()
        // TODO
    }
}
