/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileEntityCompostCore;
import monnef.jaffas.technic.block.TileEntityFermenter;
import monnef.jaffas.technic.block.TileEntityFungiBox;
import monnef.jaffas.technic.block.TileEntityHighPlant;
import monnef.jaffas.technic.block.TileEntityKeg;
import monnef.jaffas.technic.client.fungi.TileEntityFungiBoxRenderer;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFungiBox.class, new TileEntityFungiBoxRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompostCore.class, new TileEntityCompostTankRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKeg.class, new TileEntityKegRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHighPlant.class, new TileEntityHighPlantRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFermenter.class, new TileEntityFermenterRenderer());

        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.constructionBlock.blockID, new CustomBlockRenderer());

        JaffasTechnic.lampRenderID = RenderingRegistry.getNextAvailableRenderId();
        LampBlockRenderer lampBlockRenderer = new LampBlockRenderer();
        RenderingRegistry.registerBlockHandler(lampBlockRenderer);

        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.lamp.blockID, lampBlockRenderer);
        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.lampDeco.blockID, lampBlockRenderer);
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}