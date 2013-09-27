/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import com.google.common.collect.HashMultimap;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.StringsHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.food.client.GuiHandler;
import monnef.jaffas.technic.JaffasTechnic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;
import java.util.Set;

import static monnef.jaffas.technic.JaffasTechnic.fermenter;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_INPUT;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_KEG;
import static monnef.jaffas.technic.block.ContainerFermenter.SLOT_OUTPUT;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.BEER;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.BEER_RAW;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.NOTHING;
import static monnef.jaffas.technic.block.TileFermenter.FermentedLiquid.WINE;
import static monnef.jaffas.technic.block.TileKeg.KegType;

public class TileFermenter extends TileEntity implements IInventory, ISidedInventory {
    private static final String WORK_TIME_TAG = "workCounter";
    private static final String LIQUID_AMOUNT_TAG = "liquidAmount";
    private static final String LIQUID_TYPE_TAG = "liquidType";

    public static final int FERMENTER_CAPACITY = 100;
    public static final int FERMENTER_HALF_CAPACITY = FERMENTER_CAPACITY / 2 + 1;

    private static final int TICK_QUANTUM = 20;
    private boolean integrityCheckScheduled = true;
    private ItemStack[] inv;
    private int workCounter = 0;
    private int tickCounter = 0;
    private FermentedLiquid liquid = NOTHING;
    private int liquidAmount;

    private int inputSide = -1;
    private int outputSide = ForgeDirection.DOWN.ordinal();

    private static int[] rotationMatrix = new int[]{
            ForgeDirection.NORTH.ordinal(),
            ForgeDirection.EAST.ordinal(),
            ForgeDirection.SOUTH.ordinal(),
            ForgeDirection.WEST.ordinal(),
    };

    public int getInputSide() {
        if (inputSide == -1) {
            inputSide = rotationMatrix[getBlockMetadata() & 3];
            //inputSide = ForgeDirection.UP.ordinal();
        }
        return inputSide;
    }

    public enum FermentedLiquid {
        NOTHING("empty"),
        BEER_RAW("hopped wort", 5),
        WINE_RAW("raw wine", 10),
        BEER("beer"),
        WINE("wine");

        private int fermentationLength; // ticks
        private static HashMap<FermentedLiquid, FermentedLiquid> transform;

        static {
            transform = new HashMap<FermentedLiquid, FermentedLiquid>();
            transform.put(BEER_RAW, BEER);
            transform.put(WINE_RAW, WINE);
        }

        private final String title;
        private final String titleCap;

        private FermentedLiquid(String title, int fermentationLengthInMinutes) {
            this(title);
            this.fermentationLength = fermentationLengthInMinutes * 20 * (MonnefCorePlugin.debugEnv ? 1 : 60);
        }

        FermentedLiquid(String title) {
            this.title = title;
            this.titleCap = StringsHelper.makeFirstCapital(title);
        }

        public int getFermentationLength() {
            return fermentationLength;
        }

        public boolean canBeFermented() {
            return fermentationLength > 0;
        }

        public FermentedLiquid getFermentationProduct() {
            return transform.get(this);
        }

        public String getLowTitle() {
            return title;
        }

        public String getCapTitle() {
            return titleCap;
        }
    }

    public static class InputLiquidEntry {
        public final int itemID;
        public final FermentedLiquid liquid;
        public final int amount;
        public final ItemStack returnItem;

        public InputLiquidEntry(int itemID, FermentedLiquid liquid, int amount, ItemStack returnItem) {
            this.liquid = liquid;
            this.amount = amount;
            this.itemID = itemID;
            this.returnItem = returnItem.copy();
        }
    }

    public static class OutputLiquidEntry {
        public int containerItemID;
        public FermentedLiquid liquid;
        public int amount;
        public ItemStack output;

        public OutputLiquidEntry(int containerItemID, FermentedLiquid liquid, int amount, ItemStack output) {
            this.containerItemID = containerItemID;
            this.liquid = liquid;
            this.amount = amount;
            this.output = output;
        }
    }

    private static HashMap<Integer, InputLiquidEntry> inputDatabase;
    private static HashMultimap<Integer, OutputLiquidEntry> outputDatabase;

    static {
        inputDatabase = new HashMap<Integer, InputLiquidEntry>();
        int brewedHopId = JaffasTechnic.brewedHopInBucket.itemID;
        inputDatabase.put(brewedHopId, new InputLiquidEntry(brewedHopId, BEER_RAW, FERMENTER_HALF_CAPACITY, new ItemStack(Item.bucketEmpty)));

        outputDatabase = HashMultimap.create();
        int kegId = JaffasTechnic.itemKeg.itemID;
        outputDatabase.put(kegId, new OutputLiquidEntry(kegId, BEER, FERMENTER_CAPACITY, new ItemStack(kegId, 1, KegType.BEER.ordinal())));
        outputDatabase.put(kegId, new OutputLiquidEntry(kegId, WINE, FERMENTER_CAPACITY, new ItemStack(kegId, 1, KegType.WINE.ordinal())));
    }

    public TileFermenter() {
        inv = new ItemStack[3];
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
    }

    public void planIntegrityCheck() {
        if (worldObj.isRemote) return;
        integrityCheckScheduled = true;
    }

    @Override
    public void updateEntity() {
        // 2x bucket of brewed hops -> 1x keg

        super.updateEntity();
        checkBlockIntegrity();
        if (worldObj.isRemote) return;
        tickCounter++;
        if (tickCounter % TICK_QUANTUM == 0) {
            onTickQuantum();
        }
    }

    private void onTickQuantum() {
        if (isWorking()) {
            workCounter += TICK_QUANTUM;
            if (workCounter >= liquid.getFermentationLength()) {
                workCounter = 0;
                FermentedLiquid newLiquid = liquid.getFermentationProduct();
                if (newLiquid == null) {
                    throw new RuntimeException("null in liquid");
                }
                liquid = newLiquid;
            }
        } else {
            if (canWork()) {
                workCounter = 1;
            } else {
                if (!tryProcessInput()) {
                    tryProcessOutput();
                }
            }
        }
    }

    private boolean tryProcessOutput() {
        ItemStack kegStack = inv[SLOT_KEG];
        ItemStack outStack = inv[SLOT_OUTPUT];
        if (kegStack == null) return false;
        if (outStack != null) return false;
        Set<OutputLiquidEntry> found = outputDatabase.get(kegStack.itemID);
        for (OutputLiquidEntry item : found) {
            if (item.liquid == liquid) {
                if (item.amount > liquidAmount) continue;

                liquidAmount -= item.amount;
                refreshLiquidAmount();

                ItemStack output = item.output.copy();
                setInventorySlotContents(SLOT_OUTPUT, output);

                kegStack.stackSize--;
                if (kegStack.stackSize <= 0) setInventorySlotContents(SLOT_KEG, null);

                return true;
            }
        }
        return false;
    }

    private boolean tryProcessInput() {
        ItemStack input = inv[SLOT_INPUT];
        if (input == null) return false;
        if (isFull()) return false;
        InputLiquidEntry found = inputDatabase.get(input.itemID);
        if (found == null) return false;
        if (!isEmpty() && liquid != found.liquid) return false;
        ItemStack output = inv[SLOT_OUTPUT];
        if (found.returnItem != null && output != null) return false;

        input.stackSize--;
        if (input.stackSize <= 0) {
            setInventorySlotContents(SLOT_INPUT, null);
        }

        if (isEmpty()) {
            liquid = found.liquid;
            liquidAmount = 0;
        }
        liquidAmount += found.amount;
        if (liquidAmount > FERMENTER_CAPACITY) {
            liquidAmount = FERMENTER_CAPACITY;
        }

        if (found.returnItem != null) {
            setInventorySlotContents(SLOT_OUTPUT, found.returnItem.copy());
        }
        return true;
    }

    private void checkBlockIntegrity() {
        if (worldObj.isRemote) return;
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;
            boolean failure = false;
            boolean removeBlock = false;

            int bId = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);
            if (bId != getBlockType().blockID) {
                failure = true;
            } else {
                int meta = worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord);
                if (!fermenter.isSlave(meta)) {
                    failure = true;
                    removeBlock = true;
                }
            }

            if (failure) {
                if (removeBlock) {
                    BlockHelper.setBlock(worldObj, xCoord, yCoord + 1, zCoord, 0);
                }
                int myMeta = getBlockMetadata();
                BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
                invalidate();
                fermenter.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, myMeta, 0); // last param is fortune
            }
        }
    }

    public boolean playerActivatedBox(EntityPlayer player) {
        player.openGui(JaffasFood.instance, GuiHandler.GuiTypes.COBBLE_BREAKER.ordinal(), worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    @Override
    public int getSizeInventory() {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound innerTag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = innerTag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(innerTag);
            }
        }

        setWorkCounter(tag.getInteger(WORK_TIME_TAG));
        setLiquid(tag.getInteger(LIQUID_TYPE_TAG));
        setLiquidAmount(tag.getInteger(LIQUID_AMOUNT_TAG));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack != null) {
                NBTTagCompound innterTag = new NBTTagCompound();
                innterTag.setByte("Slot", (byte) i);
                stack.writeToNBT(innterTag);
                itemList.appendTag(innterTag);
            }
        }
        tag.setTag("Inventory", itemList);
        tag.setInteger(WORK_TIME_TAG, getWorkMeter());
        tag.setInteger(LIQUID_TYPE_TAG, getLiquid().ordinal());
        tag.setInteger(LIQUID_AMOUNT_TAG, getLiquidAmount());
    }

    /*
    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.customParam1 : new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.customParam1;
        readFromNBT(tag);
    }
    */

    @Override
    public String getInvName() {
        return "jaffas.board";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == outputSide) {
            return new int[]{SLOT_OUTPUT};
        } else if (side == getInputSide()) {
            return new int[]{SLOT_INPUT};
        } else {
            return new int[]{SLOT_KEG};
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side == getInputSide();
        } else if (slot == SLOT_KEG) {
            return side != getInputSide() && side != outputSide;
        } else if (slot == SLOT_OUTPUT) {
            return false; // don't allow inserintg to the output slot
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (slot == SLOT_INPUT) {
            return side == getInputSide();
        } else {
            if (slot == SLOT_OUTPUT) {
                return side == outputSide;
            } else if (slot == SLOT_KEG) {
                return side != getInputSide() && side != outputSide;
            }
        }

        return false;
    }

    public int getWorkMeter() {
        return workCounter;
    }

    public void setWorkCounter(int workCounter) {
        this.workCounter = workCounter;
    }

    public int getMaxWorkMeter() {
        return liquid.getFermentationLength();
    }

    public FermentedLiquid getLiquid() {
        return liquid;
    }

    public void setLiquid(FermentedLiquid liquid) {
        this.liquid = liquid;
    }

    public void setLiquid(int value) {
        liquid = TileFermenter.FermentedLiquid.values()[value];
    }

    public int getLiquidAmount() {
        return liquidAmount;
    }

    public void setLiquidAmount(int liquidAmount) {
        this.liquidAmount = liquidAmount;
    }

    public boolean canWork() {
        if (!isFull()) return false;
        return liquid.canBeFermented();
    }

    public boolean isFull() {
        refreshLiquidAmount();
        if (liquid == NOTHING) return false;
        return liquidAmount == FERMENTER_CAPACITY;
    }

    public boolean isEmpty() {
        refreshLiquidAmount();
        return liquid == NOTHING;
    }

    public void refreshLiquidAmount() {
        if (liquid == NOTHING) {
            liquidAmount = 0;
        } else if (liquidAmount <= 0) {
            liquidAmount = 0;
            liquid = NOTHING;
        }
    }

    public boolean isWorking() {
        return workCounter > 0;
    }
}
