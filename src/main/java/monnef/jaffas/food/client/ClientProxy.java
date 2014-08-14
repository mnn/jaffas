/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import monnef.core.client.ClientUtils;
import monnef.core.client.RenderItemInAir;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.block.TileBoard;
import monnef.jaffas.food.block.TileColumn;
import monnef.jaffas.food.block.TileCross;
import monnef.jaffas.food.block.TileJaffaStatue;
import monnef.jaffas.food.block.TileMeatDryer;
import monnef.jaffas.food.block.TilePie;
import monnef.jaffas.food.block.TilePizza;
import monnef.jaffas.food.block.TileSink;
import monnef.jaffas.food.common.CommonProxy;
import monnef.jaffas.food.common.ContentHolder;
import monnef.jaffas.food.common.CoolDownRegistry;
import monnef.jaffas.food.entity.EntityDuck;
import monnef.jaffas.food.entity.EntityDuckEgg;
import monnef.jaffas.food.entity.EntityJaffaPainting;
import monnef.jaffas.food.entity.EntityLittleSpider;
import monnef.jaffas.food.item.ItemSpawnStone;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.network.HomeStonePacket;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import static monnef.jaffas.food.common.CoolDownType.SPAWN_STONE;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityJaffaPainting.class, new RenderJaffaPainting());
        RenderingRegistry.registerEntityRenderingHandler(EntityDuck.class, new RenderDuck(new ModelChicken(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityDuckEgg.class, new RenderItemInAir(JaffasFood.getItem(JaffaItem.duckEgg)));
        RenderingRegistry.registerEntityRenderingHandler(EntityLittleSpider.class, constructLittleSpiderRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileCross.class, new TileCrossRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSink.class, new TileSinkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBoard.class, new TileBoardRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePizza.class, new TilePizzaRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileColumn.class, new TileColumnRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileJaffaStatue.class, new TileJaffaStatueRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePie.class, new TilePieRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMeatDryer.class, new TileMeatDryerRenderer());

        ContentHolder.renderID = RenderingRegistry.getNextAvailableRenderId();
        ContentHolder.renderSwitchgrassID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new SwitchgrassRenderer());
        ContentHolder.renderDirectionalBlockID = RenderingRegistry.getNextAvailableRenderId();
        ContentHolder.renderBlockID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(new DirectionalBlockRenderer());
        RenderingRegistry.registerBlockHandler(new CustomBlockRenderer());

        ClientUtils.registerItemRendererOfBlock(ContentHolder.blockTable, new CustomBlockRenderer());

        CustomBlockRenderer bricksRenderer = new CustomBlockRenderer();
        bricksRenderer.setForceInventoryColoring(true);
        ClientUtils.registerItemRendererOfBlock(ContentHolder.blockBricks, bricksRenderer);
        ClientUtils.registerItemRendererOfBlock(ContentHolder.blockStainedStone, bricksRenderer);
        ClientUtils.registerItemRendererOfBlock(ContentHolder.blockStainedPolishedStone, bricksRenderer);
        ClientUtils.registerItemRendererOfBlock(ContentHolder.blockStainedPlanks, bricksRenderer);
    }

    private Render constructLittleSpiderRenderer() {
        /*
        RenderSpider renderer = new RenderSpider();
        RenderUtils.setShadowSizeInRenderer(renderer, 0.33f);
        return renderer;
        */
        return new RenderJaffaSpider(0.33f);
    }

    @Override
    public int addArmor(String name) {
        return RenderingRegistry.addNewArmourRendererPrefix(name);
    }

    @Override
    public void registerSounds() {
        MinecraftForge.EVENT_BUS.register(new Sounds());
    }

    @Override
    public void registerTickHandler() {
        new ClientTickHandler().register();
    }

    @Override
    public void handleSyncPacket(EntityPlayer player, int secondsRemaining, boolean openGUI) {
        EntityClientPlayerMP p = (EntityClientPlayerMP) player;
        CoolDownRegistry.setCoolDown(p.getUniqueID(), SPAWN_STONE, secondsRemaining);
        if (openGUI) {
            ItemSpawnStone stone = HomeStonePacket.getSpawnStone(p);
            if (stone != null) {
                GuiSpawnStone gui = new GuiSpawnStone(p, stone);
                FMLCommonHandler.instance().showGuiScreen(gui);
            }
        }
    }
}
