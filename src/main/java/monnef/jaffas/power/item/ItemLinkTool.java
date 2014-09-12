/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.item;

import monnef.core.block.TileMachine;
import monnef.core.utils.PlayerHelper;
import monnef.core.power.IMachineTool;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemLinkTool extends ItemPower implements IMachineTool {
    private static final String SOURCE_TAG_NAME = "source";

    public ItemLinkTool(int textureIndex) {
        super(textureIndex);
        removeFromCreativeTab();
    }

    @Override
    public boolean onMachineClick(TileMachine machine, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean renderPowerLabels(EntityPlayer p) {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return stack;
        MovingObjectPosition obj = PlayerHelper.rayTraceBlock(player, 20, player.getLookVec());
        boolean success = false;
        initNBT(stack);

        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = world.getBlock(obj.blockX, obj.blockY, obj.blockZ);
            SendMessage(player, String.format("hit: %s", block != null ? block.getUnlocalizedName() : "0"));
            if (stack.getTagCompound().hasKey(SOURCE_TAG_NAME)) {
                //PowerNodeCoordinates source = new PowerNodeCoordinates(NBTHelper.getCoords(stack, SOURCE_TAG_NAME));
                    /*PowerNodeCoordinates target =
                    PowerUtils.connect()*/
                SendMessage(player, "TODO");
            } else {
                SendMessage(player, "TODO2");
            }
        }

        if (!success) {
            SendMessage(player, "Invalid target.");
        }
        stack.getTagCompound().removeTag(SOURCE_TAG_NAME);

        return stack;
    }

    private void SendMessage(EntityPlayer player, String msg) {
        PlayerHelper.addMessage(player, "[\u00A79LinkTool\u00A7r] " + msg);
    }
}
