/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.food.block.common.TileEntityMachine;
import monnef.jaffas.power.JaffasPower;
import monnef.jaffas.power.client.ModelGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

// TODO: remove?

public class BlockRenderer implements ISimpleBlockRenderingHandler {
    private ModelGenerator generator;

    public BlockRenderer() {
        generator = new ModelGenerator();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        // TODO
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntityMachine machine = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
        int rotation = machine.getRotation().ordinal();

        float angle;
        switch (rotation) {
            case 0:
                angle = 0;
                break;

            case 1:
                angle = 90;
                break;

            case 2:
                angle = 180;
                break;

            case 3:
                angle = -90;
                break;

            default:
                angle = 45;
                break;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F - 1F, 0.5F);
        //bindTextureByName("/jaffas_board.png");
        GL11.glRotatef(angle, 0, 1.0f, 0);

        generator.render(0.0625F, false);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return JaffasPower.renderID;
    }
}
