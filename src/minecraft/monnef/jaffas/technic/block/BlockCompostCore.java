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

public class BlockCompostCore extends BlockTechnic {
    private Icon blankIcon;

    public BlockCompostCore(int id, int textureID, Material material) {
        super(id, textureID, material);
        setHardness(5);
        setResistance(15);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking())
            return false;

        TileEntityCompostCore tileEntity = (TileEntityCompostCore) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null) {
            if (!tileEntity.getIsValidMultiblock()) {
                if (tileEntity.checkIfProperlyFormed()) {
                    tileEntity.convertDummies();
                    if (world.isRemote)
                        player.sendChatToPlayer("Multi-Block Furnace Created!");
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
        return new TileEntityCompostCore();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        TileEntityCompostCore tileEntity = (TileEntityCompostCore) world.getBlockTileEntity(x, y, z);

        if (tileEntity != null)
            tileEntity.invalidateMultiblock();

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public Icon getIcon(int side, int meta) {
        return meta == 0 ? blockIcon : blankIcon;
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
}
