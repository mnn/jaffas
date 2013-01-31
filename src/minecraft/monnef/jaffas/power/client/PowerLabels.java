package monnef.jaffas.power.client;

import cpw.mods.fml.client.FMLClientHandler;
import monnef.jaffas.power.api.IMachineTool;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerNode;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.block.common.TileEntityMachine;
import monnef.jaffas.power.utils.StringPowerFormatter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PowerLabels {
    private static LabelRenderer label = new LabelRenderer();

    public static void render(TileEntityMachine tile, double x, double y, double z, boolean debug) {
        if (!holdingTool()) {
            return;
        }

        // "Generator\nEnergy: 10/20\nConnected: No"
        StringBuilder text = new StringBuilder();

        text.append(tile.getMachineTitle());
        text.append("\n");
        boolean isProvider = tile instanceof IPowerProvider;
        IPowerNode powerNode = isProvider ? ((IPowerProvider) tile).getPowerProviderManager() : ((IPowerConsumer) tile).getPowerConsumerManager();
        text.append(StringPowerFormatter.getEnergyInfo(isProvider, powerNode.getCurrentBufferedEnergy(), powerNode.getBufferSize(), powerNode.getMaximalPacketSize()));
        text.append("\n");
        text.append(StringPowerFormatter.getConnectionInfo(powerNode, debug));

        label.renderLivingLabel(tile, text.toString(), 64, x, y, z, 40, 3);
    }

    // cache?
    private static boolean holdingTool() {
        ItemStack stack = FMLClientHandler.instance().getClient().thePlayer.getCurrentEquippedItem();
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item instanceof IMachineTool) {
            return ((IMachineTool) item).renderPowerLabels();
        } else {
            return false;
        }
    }
}
