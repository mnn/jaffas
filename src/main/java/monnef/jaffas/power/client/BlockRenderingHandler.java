/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.block.TileMachine;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.block.TileAntenna;
import monnef.jaffas.power.block.TileGenerator;
import monnef.jaffas.power.block.TileJuiceMaker;
import monnef.jaffas.power.block.TileLightningConductor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static monnef.jaffas.power.JaffasPower.lightningConductor;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    public static final double POSITION_FIX = -0.5D;

    private static TileGenerator generator;
    private static TileLightningConductor conductor;
    private static TileAntenna antenna;
    private static TileJuiceMaker juiceMaker;

    static {
        TileMachine.enableDummyCreationPhase();
        generator = new TileGenerator();
        antenna = new TileAntenna();
        conductor = new TileLightningConductor();
        juiceMaker = new TileJuiceMaker();
        TileMachine.disableDummyCreationPhase();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        TileEntityRendererDispatcher entityRenderer = TileEntityRendererDispatcher.instance;
        if (modelID == JaffasPower.generator.getRenderType()) {
            entityRenderer.renderTileEntityAt(generator, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        } else if (modelID == JaffasPower.antenna.getRenderType()) {
            entityRenderer.renderTileEntityAt(antenna, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        } else if (JaffasPower.lightningConductorEnabled && modelID == lightningConductor.getRenderType()) {
            entityRenderer.renderTileEntityAt(conductor, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
        } else if (modelID == JaffasPower.juiceMaker.getRenderType()) {
            GL11.glPushMatrix();
            float s = 1.5f;
            GL11.glScalef(s, s, s);
            entityRenderer.renderTileEntityAt(juiceMaker, POSITION_FIX, POSITION_FIX, POSITION_FIX, 0.0F);
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return JaffasPower.generator.getRenderType();
    }
}
