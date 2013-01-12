package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import monnef.jaffas.food.client.ModelGrave;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderingHandler implements ISimpleBlockRenderingHandler {
    static ModelGrave grave = new ModelGrave();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        return;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        //grave.render(null, );

        if (modelId == mod_jaffas.renderID) {
/*            if (block.blockID == mod_jaffas.blockCross.blockID) {

            }*/
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return mod_jaffas.renderID;
    }
}
