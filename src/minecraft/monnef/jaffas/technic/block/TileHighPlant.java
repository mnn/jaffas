/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BlockHelper;
import monnef.core.utils.PlayerHelper;
import monnef.jaffas.food.JaffasFood;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.HighPlantCatalog;
import monnef.jaffas.technic.common.HighPlantInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.technic.JaffasTechnic.highPlant;

public class TileHighPlant extends TileEntity {
    private static final String STRUCTURE_HEIGHT_TAG = "height";
    private final static int TICK_QUANTUM = 20;
    private static final String PLANT_ID_TAG = "plantId";
    private static final String STAGE_TAG = "stage";
    private static final String GROW_TIMER_TAG = "growTimer";

    private boolean integrityCheckScheduled = true;

    private int counter = -1;
    private int structureHeight;
    private int stage;
    private int plantId;
    private int growTimer;

    private HighPlantInfo cachedInfo;
    private ItemStack cachedLoot;
    private byte renderRotation;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        counter++;
        if (counter >= TICK_QUANTUM) {
            counter = 0;

            onQuantumTick();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
    }

    public void setStructureHeight(int newHeight) {
        if (structureHeight != 0) {
            Log.printWarning("overriding height setting");
        }
        structureHeight = newHeight;
    }

    // only server-side
    private void onQuantumTick() {
        checkBlockIntegrity();

        if (containsPlant()) {
            if (canGrow()) {
                growTimer -= TICK_QUANTUM;
                if (growTimer <= 0) {
                    stage++;
                    refreshGrowTimer();
                    forceUpdate();
                }
            }
        }
    }

    private void refreshGrowTimer() {
        growTimer = getPlantInfo().lifeCycle.generateStageLength(stage);
    }

    private boolean canGrow() {
        return containsPlant() && !isMature();
    }

    private boolean isMature() {
        return getPlantInfo().lifeCycle.isFinalStage(stage);
    }

    private boolean containsPlant() {
        return !HighPlantCatalog.isBlank(plantId);
    }

    public void planIntegrityCheck() {
        if (worldObj.isRemote) return;
        integrityCheckScheduled = true;
    }

    private void checkBlockIntegrity() {
        if (worldObj.isRemote) return;
        if (integrityCheckScheduled) {
            integrityCheckScheduled = false;
            boolean failure = false;

            if (structureHeight == 0) {
                failure = true;
                Log.printSevere(String.format("%s: structureHeight is zero, destroying myself(%d, %d, %d @ %d).", this.getClass().getSimpleName(), xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
            }

            for (int i = 1; i < structureHeight; i++) {
                if (failure) break;
                int yc = yCoord + i;
                int bId = worldObj.getBlockId(xCoord, yc, zCoord);
                int currMeta = worldObj.getBlockMetadata(xCoord, yc, zCoord);
                if (bId != getBlockType().blockID) {
                    failure = true;
                    break;
                }
                if (!highPlant.isSlave(currMeta)) {
                    failure = true;
                }
            }

            if (failure) {
                for (int i = 1; i < structureHeight; i++) {
                    int yc = yCoord + i;
                    if (worldObj.getBlockId(xCoord, yc, zCoord) == getBlockType().blockID) {
                        BlockHelper.setBlock(worldObj, xCoord, yc, zCoord, 0);
                    }
                }
                int myMeta = getBlockMetadata();
                BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
                invalidate();
                highPlant.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, myMeta, 0); // last param is fortune
            }
        }
    }

    public void forceUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        structureHeight = tag.getByte(STRUCTURE_HEIGHT_TAG);
        if (structureHeight == 0) {
            Log.printWarning(String.format("TEHighPlant loaded incorrect data, self-destroying."));
            BlockHelper.setBlock(worldObj, xCoord, yCoord, zCoord, 0);
            invalidate();
            return;
        }
        plantId = tag.getByte(PLANT_ID_TAG);
        stage = tag.getByte(STAGE_TAG);
        growTimer = tag.getInteger(GROW_TIMER_TAG);
        init();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(STRUCTURE_HEIGHT_TAG, (byte) structureHeight);
        tag.setByte(PLANT_ID_TAG, (byte) plantId);
        tag.setByte(STAGE_TAG, (byte) stage);
        tag.setInteger(GROW_TIMER_TAG, growTimer);
    }

    @Override
    public Packet getDescriptionPacket() {
        Packet132TileEntityData packet = (Packet132TileEntityData) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.data : new NBTTagCompound();
        writeToNBT(tag);
        tag.setInteger(GROW_TIMER_TAG, 0); // don't let client know when it will grow
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.data;
        readFromNBT(tag);
    }

    public boolean playerActivatedBox(EntityPlayer player) {
        if (MonnefCorePlugin.debugEnv && !worldObj.isRemote) {
            ItemStack hand = player.getCurrentEquippedItem();
            if (hand != null) {
                if (hand.itemID == JaffasTechnic.limsew.itemID) {
                    growTimer = 30;
                    return false;
                }
            }
        }

        if (player.isSneaking()) return false;

        if (containsPlant()) {
            ItemStack res = harvest();
            if (res != null) {
                PlayerHelper.giveItemToPlayer(player, res);
                return true;
            }
        } else {
            // no plant
            ItemStack hand = player.getCurrentEquippedItem();
            return plant(hand, true);
        }

        return false;
    }

    public boolean plant(ItemStack seedStack, boolean decreaseStackSize) {
        if (seedStack == null) return false;

        HighPlantInfo info = HighPlantCatalog.getPlantBySeedItem(seedStack);
        if (info == null) return false;

        if (decreaseStackSize) seedStack.stackSize--;

        stage = 0;
        plantId = info.id;
        refreshGrowTimer();
        forceUpdate();

        return true;
    }

    public ItemStack harvest() {
        if (!readyForHarvest()) return null;

        generateLoot();
        stage = 0;
        plantId = 0;
        invalidatePlantInfoCache();
        forceUpdate();
        return collectLoot();
    }

    public void generateLoot() {
        cachedLoot = getPlantInfo().fruit.copy();
        cachedLoot.stackSize = getPlantInfo().fruitCount.getRandom();
    }

    public ItemStack collectLoot() {
        ItemStack tmp = cachedLoot;
        cachedLoot = null;
        return tmp;
    }

    public void printDebugInfo(EntityPlayer player) {
        String msg = String.format("Stage: %d, Plant: %d, Grow Timer: %d", stage, plantId, growTimer);
        if (!player.worldObj.isRemote) {
            player.addChatMessage(msg);
        } else {
            JaffasFood.Log.printDebug("Client: " + msg);
        }
    }

    public HighPlantInfo getPlantInfo() {
        if (cachedInfo == null && !HighPlantCatalog.isBlank(plantId)) cachedInfo = HighPlantCatalog.getPlant(plantId);
        return cachedInfo;
    }

    public void invalidatePlantInfoCache() {
        cachedInfo = null;
    }

    public boolean readyForHarvest() {
        return containsPlant() && isMature();
    }

    public int getRenderRotation() {
        return renderRotation;
    }

    @Override
    public void validate() {
        super.validate();
        init();
    }

    public int getStage() {
        return stage;
    }

    private void init() {
        renderRotation = (byte) ((271 + xCoord * 221546 + yCoord - zCoord * 11075) % 4);
    }
}
