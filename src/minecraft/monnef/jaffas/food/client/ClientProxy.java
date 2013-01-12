package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.food.*;
import monnef.jaffas.food.blocks.TileEntityCross;
import monnef.jaffas.food.blocks.TileEntitySink;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.entities.EntityJaffaPainting;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture("/jaffas_01.png");
        RenderingRegistry.registerEntityRenderingHandler(EntityJaffaPainting.class, new RenderJaffaPainting());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCross.class, new TileEntityCrossRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySink.class, new TileEntitySinkRenderer());

        mod_jaffas.renderID = RenderingRegistry.getNextAvailableRenderId();
        //RenderingRegistry.registerBlockHandler(new RenderingHandler());
    }

    public int addArmor(String name) {
        return ModLoader.addArmor(name);
    }

    @Override
    public void registerSounds() {
        MinecraftForge.EVENT_BUS.register(new jaffas_EventSounds());
    }

    @Override
    public void registerTickHandler() {
        TickRegistry.registerScheduledTickHandler(new ClientTickHandler(), Side.CLIENT);
    }
}
