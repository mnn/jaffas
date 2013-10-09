/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.block.TileBoard;
import monnef.jaffas.food.block.TileRipeningBox;
import monnef.jaffas.food.client.CustomBlockRenderer;
import monnef.jaffas.food.client.GuiBoard;
import monnef.jaffas.food.client.GuiRipeningBox;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.block.TileCobbleBreaker;
import monnef.jaffas.technic.block.TileCompostCore;
import monnef.jaffas.technic.block.TileFermenter;
import monnef.jaffas.technic.block.TileFungiBox;
import monnef.jaffas.technic.block.TileHighPlant;
import monnef.jaffas.technic.block.TileKeg;
import monnef.jaffas.technic.client.fungi.TileEntityFungiBoxRenderer;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());

        ClientRegistry.bindTileEntitySpecialRenderer(TileFungiBox.class, new TileEntityFungiBoxRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCompostCore.class, new TileCompostTankRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileKeg.class, new TileKegRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHighPlant.class, new TileHighPlantRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFermenter.class, new TileFermenterRenderer());

        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.constructionBlock.blockID, new CustomBlockRenderer());

        JaffasTechnic.lampRenderID = RenderingRegistry.getNextAvailableRenderId();
        LampBlockRenderer lampBlockRenderer = new LampBlockRenderer();
        RenderingRegistry.registerBlockHandler(lampBlockRenderer);

        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.lamp.blockID, lampBlockRenderer);
        MinecraftForgeClient.registerItemRenderer(JaffasTechnic.lampDeco.blockID, lampBlockRenderer);
    }

    @Override
    public void registerContainers() {
        super.registerContainers();
        ContainerRegistry.registerOnClient(TileCobbleBreaker.class, GuiCobbleBreaker.class);
        ContainerRegistry.registerOnClient(TileCompostCore.class, GuiCompost.class);
        ContainerRegistry.registerOnClient(TileFermenter.class, GuiFermenter.class);
    }
}