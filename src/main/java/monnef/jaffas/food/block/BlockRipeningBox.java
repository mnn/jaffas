package monnef.jaffas.food.block;

import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRipeningBox extends BlockContainerJaffas {
    public BlockRipeningBox(int index, Material material) {
        super(index, material);
        setIconsCount(3);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileRipeningBox();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int idk, float what, float these, float are) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }

        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.RIPENING_BOX.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        switch (dir) {
            case UP:
                return getCustomIcon(1);
            case DOWN:
                return getCustomIcon(2);
            default:
                return getCustomIcon(0);
        }
    }
}
