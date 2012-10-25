package monnef.jaffas.trees;

import buildcraft.api.core.Orientations;
import buildcraft.api.inventory.ISpecialInventory;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import monnef.jaffas.food.TileEntityJaffaMachine;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.src.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityFruitCollector extends TileEntityJaffaMachine implements IInventory, ISpecialInventory {

    public static final int suckCost = 30;
    public static Random rand = new Random();

    private int eventTime;

    private int tickCounter;
    private int cooldown = 0;

    public static int tickDivider = 20;
    private AxisAlignedBB box;
    private static ArrayList<Integer> fruitList;

    private static int collectorSyncDistance = 32;

    private double ix, iy, iz;

    public double getIX() {
        return ix;
    }

    public double getIY() {
        return iy;
    }

    public double getIZ() {
        return iz;
    }

    public static enum CollectorStates {
        idle, targeted
    }

    public static CollectorStates[] OrdinalToState;

    private CollectorStates state = CollectorStates.idle;
    private EntityItem targetedItem = null;

    static {
        fruitList = new ArrayList<Integer>();
        fruitList.add(mod_jaffas_trees.itemLemon.shiftedIndex);
        fruitList.add(mod_jaffas_trees.itemOrange.shiftedIndex);
        fruitList.add(mod_jaffas_trees.itemPlum.shiftedIndex);
        fruitList.add(mod_jaffas.getJaffaItem(mod_jaffas.JaffaItem.vanillaBeans).shiftedIndex);
        fruitList.add(Item.appleRed.shiftedIndex);

        OrdinalToState = new CollectorStates[CollectorStates.values().length];
        for (CollectorStates state : CollectorStates.values()) {
            OrdinalToState[state.ordinal()] = state;
        }
    }

    public TileEntityFruitCollector() {
        super(100);
        inv = new ItemStack[4 + 1];
        eventTime = 0;
        this.fuelSlot = 4;
    }

    public CollectorStates getState() {
        return this.state;
    }

    public EntityItem getTargetedItem() {
        return targetedItem;
    }

    public void updateInnerState(byte newState, double ix, double iy, double iz) {
        this.ix = ix;
        this.iy = iy;
        this.iz = iz;
        this.state = OrdinalToState[newState];

        ((BlockFruitCollector) this.getBlockType()).spawnParticlesOfTargetedItem(worldObj, this.rand, xCoord, yCoord, zCoord, true);
    }

    public void updateEntity() {
        tickCounter++;

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (isBurning()) {
                eventTime++;
                burnTime -= 5;
            }

            if (burnTime <= 0) {
                burnTime = 0;
                if (!inventoryFull()) {
                    tryGetFuel();
                }
            }


            switch (state) {
                case idle:
                    if (eventTime > 5 && burnTime > suckCost) {
                        if (aquireTarget()) {
                            burnTime -= suckCost;
                        } else {
                            burnTime -= 1;
                        }
                        eventTime = 0;
                    }
                    break;

                case targeted:
                    cooldown -= 1;
                    if (cooldown <= 0) {
                        if (targetedItem != null && this.targetedItem.isEntityAlive()) {
                            ItemStack stack = this.targetedItem.item.copy();
                            int itemsAdded = addItemToInventory(this.targetedItem.item.copy(), true);
                            this.targetedItem.setDead();
                            if (mod_jaffas_trees.debug) System.out.println("target destroyed");
                            int itemsLeft = stack.stackSize - itemsAdded;

                            // spit out stuff we can't add
                            if (itemsLeft != 0) {
                                EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.7, zCoord + 0.5, stack);
                                ei.motionX = 0;
                                ei.motionY = 0.2;
                                ei.motionZ = 0;

                                worldObj.spawnEntityInWorld(ei);
                            }
                        }

                        this.state = CollectorStates.idle;
                        this.sendStateUpdatePacket();
                        this.targetedItem = null;
                    }
                    break;
            }

        }
    }

    private boolean aquireTarget() {
        if (!worldObj.isRemote) {
            box = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            box = box.expand(7, 2, 7);

            List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, box);
            Iterator<EntityItem> it = list.iterator();
            boolean notFound = true;
            EntityItem item = null;
            while (notFound && it.hasNext()) {
                item = it.next();
                if (fruitList.contains(item.item.itemID) && canAddToInventory(item)) {
                    notFound = false;
                }
            }

            if (!notFound) {
                // fruit found, moving
                this.targetedItem = item;
                this.state = CollectorStates.targeted;
                this.cooldown = 3;
//                if (mod_jaffas_trees.debug) System.out.println("target aquired!");

                this.sendStateUpdatePacket();
                return true;
            } else {
//                if (mod_jaffas_trees.debug) System.out.println("no fruit found");
                return false;
            }
        }

        return false;
    }

    private void sendStateUpdatePacket() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.CLIENT) {
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeInt(this.xCoord);
            outputStream.writeInt(this.yCoord);
            outputStream.writeInt(this.zCoord);
            outputStream.writeByte(this.state.ordinal());
            outputStream.writeDouble(this.targetedItem.posX);
            outputStream.writeDouble(this.targetedItem.posY);
            outputStream.writeDouble(this.targetedItem.posZ);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = mod_jaffas_trees.channel;
        packet.data = bos.toByteArray();
        packet.length = bos.size();

        AxisAlignedBB pbox = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        pbox = pbox.expand(collectorSyncDistance, collectorSyncDistance, collectorSyncDistance);

        List<Player> list = worldObj.getEntitiesWithinAABB(Player.class, pbox);
        for (Player p : list) {
            PacketDispatcher.sendPacketToPlayer(packet, p);
        }

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
    public String getInvName() {
        return "container.fruitCollector";
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < inv.length) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        eventTime = tagCompound.getInteger("eventTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
        tagCompound.setInteger("eventTime", eventTime);
    }


    @Override
    public int addItem(ItemStack stack, boolean doAdd, Orientations from) {
        if (from == Orientations.YNeg) {
            if (this.inv[fuelSlot] == null) {
                if (doAdd) setInventorySlotContents(fuelSlot, stack);
                return stack.stackSize;
            } else {
                if (this.inv[fuelSlot].itemID != stack.itemID) {
                    return 0;
                } else {
                    int newStackSize = stack.stackSize + inv[fuelSlot].stackSize;
                    if (doAdd) inv[fuelSlot].stackSize += stack.stackSize;

                    if (newStackSize > stack.getMaxStackSize()) {
                        if (doAdd) inv[fuelSlot].stackSize = stack.getMaxStackSize();
                        return stack.stackSize - (newStackSize % stack.getMaxStackSize());
                    } else {
                        return stack.stackSize;
                    }
                }
            }
        } else {
            return this.addItemToInventory(stack, doAdd);
        }
    }

    @Override
    public ItemStack[] extractItem(boolean doRemove, Orientations from, int maxItemCount) {
        int itemSlot = -1;

        for (int i = 0; i < fuelSlot; i++) {
            if (this.inv[i] != null) {
                itemSlot = i;
                i = fuelSlot;
            }
        }

        if (itemSlot == -1) {
            return null;
        }

        ItemStack stack = this.inv[itemSlot];
        ItemStack output = stack.copy();
        int newStackCount = stack.stackSize - maxItemCount;
        int outputStackCount = maxItemCount;
        if (newStackCount <= 0) {
            if (doRemove) setInventorySlotContents(itemSlot, null);
            outputStackCount = stack.stackSize;
        } else {
            if (doRemove) stack.stackSize = newStackCount;
        }

        output.stackSize = outputStackCount;
        return new ItemStack[]{output};
    }
}
