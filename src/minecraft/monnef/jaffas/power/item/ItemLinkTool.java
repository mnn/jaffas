package monnef.jaffas.power.item;

import monnef.core.NBTHelper;
import monnef.jaffas.power.PowerNodeCoordinates;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.block.common.TileEntityMachine;
import monnef.jaffas.power.item.ItemPower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemLinkTool extends ItemPower implements IMachineTool {
    private static final String SOURCE_TAG_NAME = "source";

    public ItemLinkTool(int id, int textureIndex) {
        super(id, textureIndex);
    }

    @Override
    public boolean onMachineClick(TileEntityMachine machine, EntityPlayer player, int side) {
        return false;
    }

    @Override
    public boolean renderPowerLabels() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition obj = player.rayTrace(20, 1);
        boolean success = false;

        if (obj.typeOfHit == EnumMovingObjectType.TILE) {
            int blockId = world.getBlockId(obj.blockX, obj.blockY, obj.blockZ);
            if (blockId != 0) {
                if (stack.getTagCompound().hasKey(SOURCE_TAG_NAME)) {
                    PowerNodeCoordinates source = new PowerNodeCoordinates(NBTHelper.getCoords(stack, SOURCE_TAG_NAME));
                    /*PowerNodeCoordinates target =
                    PowerUtils.connect()*/
                    SendMessage(player, "TODO");
                }
            }
        }

        if (!success) {
            SendMessage(player, "Invalid target.");
        }
        stack.getTagCompound().removeTag(SOURCE_TAG_NAME);

        return stack;
    }

    private void SendMessage(EntityPlayer player, String msg) {
        player.addChatMessage("[LinkTool] " + msg);
    }
}
