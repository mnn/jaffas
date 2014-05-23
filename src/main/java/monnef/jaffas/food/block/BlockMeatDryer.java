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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMeatDryer extends BlockJaffas {
    public BlockMeatDryer(int id, int texture) {
        super(id, texture, Material.wood);
        setBurnProperties(id, 15, 100);
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
        TileMeatDryer tile = (TileMeatDryer) world.getBlockTileEntity(x, y, z);
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
    public int idDropped(int par1, Random par2Random, int par3) {
        return JaffasFood.getItem(JaffaItem.meatDryer).itemID;
    }

    @Override
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return JaffasFood.getItem(JaffaItem.meatDryer).itemID;
    }
}
