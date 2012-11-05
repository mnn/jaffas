package monnef.jaffas.food;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxyTutorial extends CommonProxyTutorial {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture("/jaffas_01.png");
        RenderingRegistry.registerEntityRenderingHandler(EntityJaffaPainting.class, new RenderJaffaPainting());
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
