/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.client;

import cpw.mods.fml.client.FMLClientHandler;
import monnef.core.block.TileMachine;
import monnef.core.power.IMachineTool;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PowerLabels {
    private static LabelRenderer labelRenderer = new LabelRenderer();
    private static final PowerLabels instance = new PowerLabels();
    private EntityClientPlayerMP player;
    private static final int CACHE_MAX_TIME = 20;
    private int cacheValidTimer = CACHE_MAX_TIME;
    private boolean cachedHoldingToolResult = false;
    public final static int RENDER_DISTANCE = 64;

    public static PowerLabels getInstance() {
        return instance;
    }

    public PowerLabels() {
        refreshPlayer();
    }

    private void refreshPlayer() {
        player = FMLClientHandler.instance().getClient().thePlayer;
    }

    public static void renderLabel(TileMachine tile, double x, double y, double z, boolean debug) {
        // skip label rendering when rendering in inventory/hand
        if (tile.getWorldObj() == null) return;

        instance.render(tile, x, y, z, debug);
    }

    public void render(TileMachine tile, double x, double y, double z, boolean debug) {
        if (!holdingTool()) {
            return;
        }

        // TODO cache
        String label = generateLabelText(tile, debug);

        labelRenderer.renderLabel(tile, label, RENDER_DISTANCE, x, y, z, 40, 3);
    }

    private String generateLabelText(TileMachine tile, boolean debug) {
        StringBuilder text = new StringBuilder();

        text.append(tile.getMachineTitle());
        text.append("\n");
/*        boolean isProvider = tile instanceof IPowerProvider;
        boolean isConsumer = tile instanceof IPowerConsumer;

        IPowerProviderManager providerManager = null;
        if (isProvider) {
            providerManager = ((IPowerProvider) tile).getPowerProviderManager();
        }
        IPowerConsumerManager consumerManager = null;
        if (isConsumer) {
            consumerManager = ((IPowerConsumer) tile).getPowerConsumerManager();
        }

        IPowerNodeManager powerNode = isProvider ? providerManager : consumerManager;

        if (isFullyInitialized(tile, isProvider, isConsumer)) {
            text.append(StringPowerFormatter.formatEnergyInfo(isProvider, isConsumer, StringPowerFormatter.getCurrentBufferedEnergyCombined(providerManager, consumerManager), StringPowerFormatter.getBufferSizeCombined(providerManager,consumerManager), powerNode.getMaximalPacketSize()));
            text.append("\n");
            text.append(StringPowerFormatter.getConnectionInfo(powerNode, debug));
        } else {
            text.append("** not initialized! **\n** not initialized! **");
        }*/

        return text.toString();
    }

    private boolean isFullyInitialized(TileMachine tile, boolean provider, boolean consumer) {
/*        if (provider && !((IPowerProvider) tile).getPowerProviderManager().isInitialized()) {
            return false;
        }

        if (consumer && !((IPowerConsumer) tile).getPowerConsumerManager().isInitialized()) {
            return false;
        }
  */
        return true;
    }

    private boolean holdingTool() {
        cacheValidTimer--;
        if (cacheValidTimer < 0) {
            boolean ret;
            cacheValidTimer = CACHE_MAX_TIME;
            refreshPlayer();
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack == null) ret = false;
            else {
                Item item = stack.getItem();

                if (item instanceof IMachineTool) {
                    ret = ((IMachineTool) item).renderPowerLabels(player);
                } else {
                    ret = false;
                }
            }

            cachedHoldingToolResult = ret;
        }

        return cachedHoldingToolResult;
    }
}
