/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileLightningConductor;
import monnef.jaffas.food.block.common.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;

import static monnef.jaffas.power.JaffasPower.lightningConductor;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    public static final double POSITION_FIX = -0.5D;

    private static TileGenerator generator;
    private static TileLightningConductor conductor;
    private static TileAntenna antenna;

    static {
        TileEntityMachine.enableDummyCreationPhase();
        generator = new TileGenerator();
        antenna = new TileAntenna();
        conductor = new TileLightningConductor();
        TileEntityMachine.disableDummyCreationPhase();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        TileEntityRenderer entityRenderer = TileEntityRenderer.instance;
        if (modelID == JaffasPower.generator.getRenderType()) {
            entityRenderer.renderTileEntityAt(generator, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        } else if (modelID == JaffasPower.antenna.getRenderType()) {
            entityRenderer.renderTileEntityAt(antenna, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        } else if (JaffasPower.lightningConductorEnabled && modelID == lightningConductor.getRenderType()) {
            entityRenderer.renderTileEntityAt(conductor, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return JaffasPower.generator.getRenderType();
    }
}
