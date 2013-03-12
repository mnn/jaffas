package monnef.core.utils;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void renderStaticTileEntityInWorld(IBlockAccess world, int x, int y, int z, RenderBlocks blocksRenderer, TileEntity tile, TileEntitySpecialRenderer tileRenderer) {
        GL11.glPushMatrix();

        // code based on TileEntityRenderer
        int var3 = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var4 / 1.0F, (float) var5 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderTileEntity(x & 15, y & 15, z & 15, tile, tileRenderer);

        GL11.glPopMatrix();
    }

    public static void renderTileEntity(double x, double y, double z, TileEntity tile, TileEntitySpecialRenderer tileRenderer) {
        tileRenderer.renderTileEntityAt(tile, x, y, z, 0f);
    }

    public static void RenderStaticTileEntityOld(IBlockAccess world, int x, int y, int z, RenderBlocks blocksRenderer, TileEntity tile) {
        GL11.glPushMatrix();

        // code based on TileEntityRenderer
        int var3 = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var4 / 1.0F, (float) var5 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        TileEntitySpecialRenderer renderer = TileEntityRenderer.instance.getSpecialRendererForEntity(tile);
        renderer.renderTileEntityAt(tile, x & 15, y & 15, z & 15, 0f);

        GL11.glPopMatrix();
    }
}
