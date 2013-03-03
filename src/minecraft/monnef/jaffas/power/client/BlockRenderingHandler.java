package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.power.block.TileEntityAntenna;
import monnef.jaffas.power.block.TileEntityGenerator;
import monnef.jaffas.power.mod_jaffas_power;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        //TODO: cache TEs?
        if (modelID == mod_jaffas_power.generator.getRenderType()) {
            TileEntityRenderer.instance.renderTileEntityAt(new TileEntityGenerator(), 0.0D, -0.08D, 0.0D, 0.0F);
        } else if (modelID == mod_jaffas_power.antenna.getRenderType()) {
            TileEntityRenderer.instance.renderTileEntityAt(new TileEntityAntenna(), 0.0D, -0.08D, 0.0D, 0.0F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return mod_jaffas_power.generator.getRenderType();
    }
}
