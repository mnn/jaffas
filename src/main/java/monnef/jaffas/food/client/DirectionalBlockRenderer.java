/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.core.client.CustomBlockRenderingHelper;
import monnef.jaffas.food.block.BlockJDirectional;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class DirectionalBlockRenderer implements ISimpleBlockRenderingHandler {
    private DummyBlockAccess dummyBlockAccess = new DummyBlockAccess();
    private IBlockAccess originalBlockAccess;
    private boolean renderingInventory = false;

    private void activateDummyBlockAccess(RenderBlocks renderer) {
        originalBlockAccess = renderer.blockAccess;
        renderer.blockAccess = dummyBlockAccess;
        renderingInventory = true;
    }

    private void deactivateDummyBlockAccess(RenderBlocks renderer) {
        renderer.blockAccess = originalBlockAccess;
        renderingInventory = false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        activateDummyBlockAccess(renderer);
        BlockJDirectional dirBlock = (BlockJDirectional) block;
        render(dirBlock, dirBlock.getInventoryRenderRotation(), renderer, 0, 0, 0);
        deactivateDummyBlockAccess(renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        BlockJDirectional b = (BlockJDirectional) block;
        render(b, world.getBlockMetadata(x, y, z), renderer, x, y, z);
        return true;
    }

    private void render(BlockJDirectional block, int meta, RenderBlocks renderer, int x, int y, int z) {
        switch (block.getType()) {
            case LOG_LIKE:
                renderLogBlock(meta, renderer, block, x, y, z);
                break;

            case ALL_SIDES:
                renderAllSidesBlock(meta, renderer, block, x, y, z);
        }
    }

    private void renderAllSidesBlock(int meta, RenderBlocks renderer, BlockJDirectional block, int x, int y, int z) {
        doOurRendering(renderer, block, x, y, z, renderingInventory, meta);
    }

    private void renderLogBlock(int meta, RenderBlocks renderer, BlockJDirectional block, int x, int y, int z) {
        int rotation = block.getRotation(meta);
        if (rotation == 1) {
            renderer.uvRotateSouth = 1;
            renderer.uvRotateNorth = 1;
        } else if (rotation == 2) {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 1;
            renderer.uvRotateEast = 1;
            renderer.uvRotateWest = 1;
        }

        doOurRendering(renderer, block, x, y, z, renderingInventory, meta);

        resetRotations(renderer);
    }

    public static void doOurRendering(RenderBlocks renderer, Block block, int x, int y, int z, boolean renderingInventory, int meta) {
        if (renderingInventory) {
            CustomBlockRenderingHelper.doRendering(renderer, block, meta);
        } else {
            CustomBlockRenderingHelper.doRendering(renderer, block, x, y, z);
        }
    }

    private void resetRotations(RenderBlocks renderer) {
        renderer.uvRotateBottom = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateWest = 0;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ContentHolder.renderDirectionalBlockID;
    }

    private static class DummyBlockAccess implements IBlockAccess {
        @Override
        public Block getBlock(int i, int j, int k) {
            return null;
        }

        @Override
        public TileEntity getTileEntity(int i, int j, int k) {
            return null;
        }

        @Override
        public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
            return 0;
        }

        @Override
        public int getBlockMetadata(int i, int j, int k) {
            return 0;
        }


        @Override
        public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
            return false;
        }

        @Override
        public boolean isAirBlock(int i, int j, int k) {
            return true;
        }

        @Override
        public BiomeGenBase getBiomeGenForCoords(int i, int j) {
            return BiomeGenBase.forest;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public boolean extendedLevelsInChunkCache() {
            return false;
        }

        @Override
        public int isBlockProvidingPowerTo(int i, int j, int k, int l) {
            return 0;
        }
    }
}
