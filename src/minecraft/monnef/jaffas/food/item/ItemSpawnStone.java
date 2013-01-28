package monnef.jaffas.food.item;

import cpw.mods.fml.common.FMLCommonHandler;
import monnef.jaffas.food.client.GuiSpawnStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpawnStone extends ItemJaffaBase {
    private int cooldown;

    public ItemSpawnStone(JaffaItemInfo info, int cooldown) {
        super(info.getId());
        this.cooldown = cooldown;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
        FMLCommonHandler.instance().showGuiScreen(new GuiSpawnStone(player, cooldown));
        return par1ItemStack;
    }
}
