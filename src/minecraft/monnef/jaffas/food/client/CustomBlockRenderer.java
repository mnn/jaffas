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
import net.minecraft.world.IBlockAccess;

public class CustomBlockRenderer implements ISimpleBlockRenderingHandler {
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
}
