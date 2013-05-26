/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCompostCore extends BlockTechnic {
    public BlockCompostCore(int id, int textureID, Material material) {
        super(id, textureID, material);
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
                //player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
                player.addChatMessage("opening gui...");
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
}
