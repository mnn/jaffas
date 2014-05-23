/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.trees.JaffasTrees;
import monnef.jaffas.trees.block.BlockFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class LeavesBlockRenderer implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);
        BlockFruitLeaves leaves = (BlockFruitLeaves) block;
        int meta = world.getBlockMetadata(x, y, z);
        if (leaves.doFruitRendering(meta)) {
            renderer.renderStandardBlock(JaffasTrees.dummyLeaves, x, y, z);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return JaffasTrees.leavesRenderID;
    }
}
