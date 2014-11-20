/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.common.SpecialCobWebRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

@ContainerRegistry.ContainerTag(slotsCount = 1, containerClassName = "monnef.core.block.ContainerMachine", guiClassName = "monnef.jaffas.power.client.GuiContainerWebHarvester")
public class TileWebHarvester extends TileMachineWithInventory implements ISidedInventory {
    public static final int WEB_HARVESTER_RADIUS = 5;
    public static final int WEB_HARVESTER_DIAMETER = 2 * WEB_HARVESTER_RADIUS;
    private static final int WEB_HARVESTER_DIAMETER_SQUARE = WEB_HARVESTER_DIAMETER * WEB_HARVESTER_DIAMETER;
    private static final int WEB_HARVESTER_DIAMETER_CUBE = WEB_HARVESTER_DIAMETER * WEB_HARVESTER_DIAMETER_SQUARE;
    public static final int WEB_SEARCH_ENERGY = 3;
    public static final int MAX_SCANS_PER_TRY = WEB_HARVESTER_DIAMETER;
    private static final boolean DEBUG_PRINTS = false;

    private int lookPointer = JaffasFood.rand.nextInt(WEB_HARVESTER_RADIUS * WEB_HARVESTER_RADIUS * WEB_HARVESTER_RADIUS);

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded *= 5;
        maxEnergyReceived = powerNeeded / 5 / 2;
        slowingCoefficient = 10;
        if (MonnefCorePlugin.debugEnv) {
            slowingCoefficient = 1;
            powerNeeded = 1;
        }
    }

    @Override
    public String getInventoryName() {
        return "jaffas.power.webHarvester";
    }

    @Override
    public String getMachineTitle() {
        return "Web Harvester";
    }

    @Override
    protected void doMachineWork() {
        if (worldObj.isRemote) return;
        if (!freeSpaceInInventory()) return;

        IIntegerCoordinates web = findWeb();
        if (web == null) {
            getEnergyStorage().extractEnergy(WEB_SEARCH_ENERGY, false);
            return;
        }

        if (consumeNeededPower() < powerNeeded) {
            return;
        }

        Block webBlock = web.getBlock();
        ArrayList<ItemStack> drops = webBlock.getDrops(worldObj, web.getX(), web.getY(), web.getZ(), web.getBlockMetadata(), 0);
        worldObj.func_147480_a(web.getX(), web.getY(), web.getZ(), false); // destroyBlock
        if (drops.size() > 0) {
            ItemStack dropsHeadSquashed = drops.get(0);
            boolean containsOtherItems = false;
            boolean tooBig = false;
            for (int i = 1; i < drops.size(); i++) {
                ItemStack drop = drops.get(i);
                if (dropsHeadSquashed.isItemEqual(drop)) {
                    if (drop.stackSize + dropsHeadSquashed.stackSize < dropsHeadSquashed.getMaxStackSize()) {
                        dropsHeadSquashed.stackSize += drop.stackSize;
                    } else {
                        tooBig = true;
                    }
                } else {
                    containsOtherItems = true;
                }
            }

            if (tooBig || containsOtherItems) {
                JaffasFood.Log.printWarning(String.format("Web drops returned more stuff that I can fit into my inventory, web block: %s, tooBig: %s, containsOtherItems: %s", webBlock.getUnlocalizedName(), tooBig, containsOtherItems));
            }
            setInventorySlotContents(0, dropsHeadSquashed);
        }
    }

    private boolean freeSpaceInInventory() {
        return getStackInSlot(0) == null;
    }

    private IIntegerCoordinates findWeb() {
        boolean found;
        int tryCount = 0;
        int x, y, z;
        do {
            lookPointer++;
            if (lookPointer > WEB_HARVESTER_DIAMETER_CUBE) lookPointer = 0;
            int sx = lookPointer % WEB_HARVESTER_DIAMETER;
            int sz = (lookPointer % WEB_HARVESTER_DIAMETER_SQUARE) / WEB_HARVESTER_DIAMETER;
            int sy = lookPointer / WEB_HARVESTER_DIAMETER_SQUARE;
            sx = fixZero(sx);
            sy = fixZero(sy);
            sz = fixZero(sz);
            x = xCoord - WEB_HARVESTER_RADIUS + sx;
            y = yCoord - WEB_HARVESTER_RADIUS + sy;
            z = zCoord - WEB_HARVESTER_RADIUS + sz;
            if (MonnefCorePlugin.debugEnv && DEBUG_PRINTS) {
                JaffasFood.Log.printInfo(String.format("checking if cobweb - %d, %d, %d", x, y, z));
            }
            found = SpecialCobWebRegistry.blockRegistered(worldObj.getBlock(x, y, z));
        } while (tryCount++ < MAX_SCANS_PER_TRY && !found);
        if (found) return new IntegerCoordinates(x, y, z, worldObj);
        return null;
    }

    private int fixZero(int value) {
        return value < WEB_HARVESTER_RADIUS ? value + 1 : value;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return true;
    }
}
