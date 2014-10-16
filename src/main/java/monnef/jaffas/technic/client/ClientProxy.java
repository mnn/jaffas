/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.client.ClientUtils;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileCompostCore;
import monnef.jaffas.technic.block.TileFermenter;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.block.TileKeg;
import monnef.jaffas.technic.client.fungi.TileEntityFungiBoxRenderer;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityCombineHarvester;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());
        RenderingRegistry.registerEntityRenderingHandler(EntityCombineHarvester.class, new RenderCombineHarvester());

        ClientRegistry.bindTileEntitySpecialRenderer(TileFungiBox.class, new TileEntityFungiBoxRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCompostCore.class, new TileCompostTankRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileKeg.class, new TileKegRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHighPlant.class, new TileHighPlantRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFermenter.class, new TileFermenterRenderer());

        ClientUtils.registerItemRendererOfBlock(JaffasTechnic.constructionBlock, new CustomBlockRenderer());

        JaffasTechnic.lampRenderID = RenderingRegistry.getNextAvailableRenderId();
        LampBlockRenderer lampBlockRenderer = new LampBlockRenderer();
        RenderingRegistry.registerBlockHandler(lampBlockRenderer);

        ClientUtils.registerItemRendererOfBlock(JaffasTechnic.lamp, lampBlockRenderer);
        ClientUtils.registerItemRendererOfBlock(JaffasTechnic.lampDeco, lampBlockRenderer);
    }
}