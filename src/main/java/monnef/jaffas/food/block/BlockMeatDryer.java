/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.item.JaffaItem;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMeatDryer extends BlockJaffas {
    public BlockMeatDryer(int texture) {
        super(texture, Material.wood);
        setBurnProperties(15, 100);
        setHardness(0.25f);
        setCreativeTab(null);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileMeatDryer();
    }

    @Override
    public int getRenderType() {
        return ContentHolder.renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        TileMeatDryer tile = (TileMeatDryer) world.getTileEntity(x, y, z);
        ItemStack hand = player.getCurrentEquippedItem();

        if (!world.isRemote && MonnefCorePlugin.debugEnv) tile.printDebugInfo(player);

        if (!tile.isStackValidInput(hand)) {
            // collect meat
            if (!tile.hasFinishedMeat()) {
                return false;
            }
            if (!world.isRemote) {
                ItemStack res = tile.harvestMeat();
                PlayerHelper.giveItemToPlayer(player, res);
            }
            return true;
        } else {
            if (!tile.hasFreeSpace()) {
                return false;
            }
            if (world.isRemote) {
                return true;
            } else {
                return tile.tryPlaceMeat(hand, true);
            }
        }
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return JaffasFood.getItem(JaffaItem.meatDryer);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(JaffasFood.getItem(JaffaItem.meatDryer));
    }
}
