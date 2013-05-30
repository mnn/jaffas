/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.client.CustomBlockRenderingHelper;
import monnef.jaffas.food.JaffasFood;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class CustomBlockRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        CustomBlockRenderingHelper.doRendering(renderer, block, 0, 0, 0, true, metadata, 1);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        CustomBlockRenderingHelper.doRendering(renderer, block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return JaffasFood.renderBlockID;
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
            CustomBlockRenderingHelper.doRendering(render, Block.blocksList[item.itemID], 0, 0, 0, true, item.getItemDamage(), 1);
        }
    }
}
