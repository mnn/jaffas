/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCobbleBreaker extends BlockDirectionalTechnic {
    public BlockCobbleBreaker(int textureStart, int texturesCountPerSet, Material material, TextureMappingType type) {
        super(textureStart, texturesCountPerSet, material, type);
        setHardness(2f);
        setResistance(10f);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileCobbleBreaker();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (player.isSneaking()) return false;

        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.COBBLE_BREAKER.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    protected int processSideToIconIndexMapping(int side) {
        switch (ForgeDirection.getOrientation(side)) {
            case UP:
                return 1;

            default:
                return 0;
        }
    }

    @Override
    public int getInventoryRenderRotation() {
        return 1;
    }
}
