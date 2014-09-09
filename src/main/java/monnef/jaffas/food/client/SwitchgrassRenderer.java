/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.food.common.ContentHolder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class SwitchgrassRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        return;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderCrossedSquares(block, x, y, z, world, renderer);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return ContentHolder.renderSwitchgrassID;
    }

    // grass renderer
    public boolean renderCrossedSquares(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer) {
        Tessellator var5 = Tessellator.instance;
        var5.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        float opacity = 1.0F;
        int saturation = block.colorMultiplier(world, x, y, z);
        float var8 = (float) (saturation >> 16 & 255) / 255.0F;
        float var9 = (float) (saturation >> 8 & 255) / 255.0F;
        float var10 = (float) (saturation & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
            float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
            float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
            var8 = var11;
            var9 = var12;
            var10 = var13;
        }

        var5.setColorOpaque_F(opacity * var8, opacity * var9, opacity * var10);
        double newX = (double) x;
        double newY = (double) y;
        double newZ = (double) z;

        if (block == ContentHolder.blockSwitchgrass) {
            long var17 = (long) (x * 3129871) ^ (long) z * 116129781L /*^ (long) y*/;
            var17 = var17 * var17 * 42317861L + var17 * 11L;
            newX += ((double) ((float) (var17 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            newY += ((double) ((float) (var17 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            newZ += ((double) ((float) (var17 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }

        renderer.drawCrossedSquares(block.getIcon(0, world.getBlockMetadata(x, y, z)), newX, newY, newZ, 1.0F);
        return true;
    }
}
