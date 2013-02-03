package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.jaffas.food.block.*;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.common.SpawnStonePacketUtils;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.item.ItemSpawnStone;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture("/jaffas_01.png");

        RenderingRegistry.registerEntityRenderingHandler(EntityJaffaPainting.class, new RenderJaffaPainting());
        RenderingRegistry.registerEntityRenderingHandler(EntityDuck.class, new RenderDuck(new ModelChicken(), 0.3F));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCross.class, new TileEntityCrossRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySink.class, new TileEntitySinkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBoard.class, new TileEntityBoardRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPizza.class, new TileEntityPizzaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColumn.class, new TileEntityColumnRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJaffaStatue.class, new TileEntityJaffaStatueRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPie.class, new TileEntityPieRenderer());

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

    @Override
    public void handleSyncPacket(Player player, int secondsRemaining, boolean openGUI) {
        EntityClientPlayerMP p = (EntityClientPlayerMP) player;
        CoolDownRegistry.setCoolDown(p.getEntityName(), SPAWN_STONE, secondsRemaining);
        if (openGUI) {
            ItemSpawnStone stone = SpawnStonePacketUtils.getSpawnStone(p);
            if (stone != null) {
                GuiSpawnStone gui = new GuiSpawnStone(p, stone);
                FMLCommonHandler.instance().showGuiScreen(gui);
            }
        }
    }
}
