/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.client.CustomBlockRenderingHelper;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.BlockLamp;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class LampBlockRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {
    private static final float slightlyBigger = 1.001f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        CustomBlockRenderingHelper.render(JaffasTechnic.lampDummy, metadata, 1, renderer, ((BlockLamp) block).shouldForceInventoryColoring());
        GL11.glScalef(slightlyBigger, slightlyBigger, slightlyBigger);
        CustomBlockRenderingHelper.render(JaffasTechnic.lamp, 0, 1, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // orig:
        // renderer.renderStandardBlock(JaffasTechnic.lampDummy, x, y, z); // colored core

        renderer.renderStandardBlock(JaffasTechnic.lampDummy, x, y, z); // colored core

        /*
        GL11.glPushMatrix();
        Tessellator tes = Tessellator.instance;
        boolean fix = false;
        if (tes.isDrawing) {
            tes.draw();
            fix = true;
        }
        GL11.glColor4f(1, 1, 1, .5f);
        CustomBlockRenderingHelper.Render(block, 1, 1, renderer);
        if (fix) {
            tes.startDrawingQuads();
        }
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
        */

        renderer.renderStandardBlock(JaffasTechnic.lamp, x, y, z);      // frame
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return JaffasTechnic.lampRenderID;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return isMyType(type);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.EQUIPPED_BLOCK;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (isMyType(type)) {
            RenderBlocks render = (RenderBlocks) data[0];
            EntityLivingBase entity = (EntityLivingBase) data[1];
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            CustomBlockRenderingHelper.render(JaffasTechnic.lampDummy, item.getItemDamage(), 1, render, getBlock(item).shouldForceInventoryColoring());
            GL11.glScalef(slightlyBigger, slightlyBigger, slightlyBigger);
            CustomBlockRenderingHelper.render(JaffasTechnic.lamp, 0, 1, render);
        }
    }

    private BlockLamp getBlock(ItemStack stack) {
        return (BlockLamp) Block.blocksList[stack.itemID];
    }

    private static boolean isMyType(ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }
}
