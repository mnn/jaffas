/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.api.IItemBlock;
import monnef.core.client.CustomBlockRenderingHelper;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class CustomBlockRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    private boolean forceInventoryColoring = false;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        if (forceInventoryColoring) {
            CustomBlockRenderingHelper.render(block, metadata, 1, renderer, true);
        } else {
            CustomBlockRenderingHelper.doRendering(renderer, block, 0, 0, 0, true, metadata, 1);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        CustomBlockRenderingHelper.doRendering(renderer, block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ContentHolder.renderBlockID;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return isMyType(type);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        if (type == ItemRenderType.ENTITY) {
            return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
        } else {
            return helper == ItemRendererHelper.EQUIPPED_BLOCK;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (isMyType(type)) {
            RenderBlocks render = (RenderBlocks) data[0];

            if (type == ItemRenderType.ENTITY) {
                GL11.glScalef(.5f, .5f, .5f);
            } else {
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }

            if (forceInventoryColoring) {
                CustomBlockRenderingHelper.render(((IItemBlock) item.getItem()).getBlock(), item.getItemDamage(), 1, render, true);
            } else {
                CustomBlockRenderingHelper.doRendering(render, Block.getBlockFromItem(item.getItem()), 0, 0, 0, true, item.getItemDamage(), 1);
            }
        }
    }

    private static boolean isMyType(ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.ENTITY;
    }

    public boolean isForceInventoryColoring() {
        return forceInventoryColoring;
    }

    public void setForceInventoryColoring(boolean forceInventoryColoring) {
        this.forceInventoryColoring = forceInventoryColoring;
    }
}
