/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import monnef.jaffas.technic.block.TileEntityFungiBox;
import monnef.jaffas.technic.client.fungi.TileEntityFungiBoxRenderer;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFungiBox.class, new TileEntityFungiBoxRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompostCore.class, new TileEntityCompostTankRenderer());

        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.constructionBlock.blockID, new CustomBlockRenderer());
    }
}