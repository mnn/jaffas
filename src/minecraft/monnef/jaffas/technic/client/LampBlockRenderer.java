/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.client.CustomBlockRenderingHelper;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class LampBlockRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {
    private static final float slightlyBigger = 1.001f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        CustomBlockRenderingHelper.Render(JaffasTechnic.lampDummy, 0, 1, renderer);
        GL11.glScalef(slightlyBigger, slightlyBigger, slightlyBigger);
        CustomBlockRenderingHelper.Render(JaffasTechnic.lamp, 0, 1, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(JaffasTechnic.lampDummy, x, y, z); // colored core
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
        return type == ItemRenderType.EQUIPPED;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.EQUIPPED_BLOCK;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.EQUIPPED) {
            RenderBlocks render = (RenderBlocks) data[0];
            EntityLiving entity = (EntityLiving) data[1];
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            CustomBlockRenderingHelper.Render(JaffasTechnic.lampDummy, 0, 1, render);
            GL11.glScalef(slightlyBigger, slightlyBigger, slightlyBigger);
            CustomBlockRenderingHelper.Render(JaffasTechnic.lamp, 0, 1, render);
        }
    }
}
