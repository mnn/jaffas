/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.base.CustomIconHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockCompostCore extends BlockTechnic {
    private Icon blankIcon;

    public BlockCompostCore(int id, int textureID, Material material) {
        super(id, textureID, material);
        setHardness(5);
        setResistance(15);
        setIconsCount(2);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking())
            return false;

        TileCompostCore tileEntity = (TileCompostCore) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            if (!tileEntity.getIsValidMultiblock()) {
                if (tileEntity.checkIfProperlyFormed()) {
                    tileEntity.convertDummies();
                }
            }

            if (tileEntity.getIsValidMultiblock()) {
                player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.COMPOST.ordinal(), world, x, y, z);
            }
        }

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileCompostCore();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        TileCompostCore tileEntity = (TileCompostCore) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null)
            tileEntity.invalidateMultiblock();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public Icon getIcon(int side, int meta) {
        if (!isCompostTankFormed(meta)) {
            if (side == ForgeDirection.UP.ordinal()) return icons[0];
            return icons[1];
        }

        return blankIcon;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        blankIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this, 99));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return isCompostTankFormed(meta) ? 0 : 255;
    }

    public static boolean isCompostTankFormed(int meta) {
        return meta == 1;
    }
}
