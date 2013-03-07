package monnef.jaffas.power.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.power.block.common.BlockPower;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLightningConductor extends BlockPower {
    private final int renderID;
    private static final float border = 5f * 1f / 16f;
    private static final float borderComplement = 1f - border;

    public BlockLightningConductor(int par1, int par2) {
        super(par1, par2, Material.iron);
        setBlockName("lightningConductor");
        renderID = RenderingRegistry.getNextAvailableRenderId();
        setBlockBounds(border, 0, border, borderComplement, 1, borderComplement);
    }

    @Override
    public int getRenderType() {
        return renderID;
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
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityLightningConductor();
    }
}
