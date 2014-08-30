/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.block.TileMachineWithInventory;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.item.JaffaItem;
import monnef.jaffas.food.item.common.ItemManager;
import monnef.jaffas.technic.network.FruitCollectorPacket;
import monnef.jaffas.trees.JaffasTrees;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static monnef.jaffas.food.JaffasFood.Log;
import static monnef.jaffas.food.client.Sounds.SoundsEnum.COLLECTOR_NOISE;
import static monnef.jaffas.food.client.Sounds.SoundsEnum.COLLECTOR_SUCK;

@ContainerRegistry.ContainerTag(slotsCount = 4, outputSlotsCount = 4, containerClassName = "monnef.jaffas.trees.block.ContainerFruitCollector", guiClassName = "monnef.jaffas.trees.client.GuiFruitCollector")
public class TileFruitCollector extends TileMachineWithInventory {

    public static final int suckCost = 30;
    public static Random rand = new Random();

    private int eventTime;

    private int tickCounter;
    private int cooldown = 0;

    public static int tickDivider = 20;
    private AxisAlignedBB box;
    private static HashMap<Item, Integer> fruitList;

    private static int collectorSyncDistance = 32;

    private double ix, iy, iz;

    private String soundToRun = null;
    private float soundVolume = 1f;
    private boolean findFruitToKnockHarder = false;

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

    private static void addToFruitList(Item item) {
        addToFruitList(item, 0);
    }

    private static void addToFruitList(Item item, int dmg) {
        fruitList.put(item, dmg);
    }

    public static void onLoad() {
        fruitList = new HashMap<Item, Integer>();
        addToFruitList(JaffasTrees.itemLemon);
        addToFruitList(JaffasTrees.itemOrange);
        addToFruitList(JaffasTrees.itemPlum);
        addToFruitList(ItemManager.getItem(JaffaItem.vanillaBeans));
        addToFruitList(Items.apple);
        addToFruitList(JaffasTrees.itemLemon);
        addToFruitList(Items.dye, 3); // cocoa beans
        addToFruitList(JaffasTrees.itemCoconut);

        OrdinalToState = new CollectorStates[CollectorStates.values().length];
        for (CollectorStates state : CollectorStates.values()) {
            OrdinalToState[state.ordinal()] = state;
        }
    }

    public TileFruitCollector() {
        super();
        eventTime = 0;
    }

    @Override
    protected void configurePowerParameters() {
        super.configurePowerParameters();
        powerNeeded = 10;
    }

    @Override
    public String getMachineTitle() {
        return "Fruit Collector";
    }

    public CollectorStates getState() {
        return this.state;
    }

    public EntityItem getTargetedItem() {
        return targetedItem;
    }

    @SideOnly(Side.CLIENT)
    public void updateInnerState(byte newState, double ix, double iy, double iz) {
        this.ix = ix;
        this.iy = iy;
        this.iz = iz;
        CollectorStates oldState = this.state;
        this.state = OrdinalToState[newState];

        ((BlockFruitCollector) this.getBlockType()).spawnParticlesOfTargetedItem(worldObj, rand, xCoord, yCoord, zCoord, true);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        tickCounter++;
        playQueuedSound();

        if (gotPowerToActivate()) {
            int limit = this.findFruitToKnockHarder ? 15 : 3;
            this.findFruitToKnockHarder = false;
            for (int i = 0; i < limit; i++)
                if (tryKnockingFruitDown()) break;
        }

        if (tickCounter % tickDivider == 0) {
            // only every second do stuff

            if (gotPowerToActivate()) {
                eventTime++;
                consumePower(5);

                switch (state) {
                    case idle:
                        if (eventTime > 5 && gotPower(suckCost)) {
                            if (aquireTarget()) {
                                this.queueSound(COLLECTOR_NOISE.getSoundName(), 0.7F);
                                consumePower(suckCost);
                                this.findFruitToKnockHarder = true;
                            } else {
                                consumePower(1);
                            }
                            eventTime = 0;
                        }
                        break;

                    case targeted:
                        cooldown -= 1;
                        if (cooldown <= 0) {
                            if (targetedItem != null && this.targetedItem.isEntityAlive()) {
                                ItemStack stack = this.targetedItem.getEntityItem().copy();
                                int itemsAdded = addItemToInventory(this.targetedItem.getEntityItem().copy(), true);
                                this.targetedItem.setDead();
                                if (JaffasTrees.debug) Log.printInfo("target destroyed");
                                int itemsLeft = stack.stackSize - itemsAdded;

                                this.queueSound(COLLECTOR_SUCK.getSoundName());

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
                            this.sendStateUpdatePacketNew();
                            this.targetedItem = null;
                        }
                        break;
                }
            }
        }
    }

    private boolean tryKnockingFruitDown() {
        // 7 x 7 x 7
        int cx = this.xCoord + computeRandomCoordinate(7);
        int cy = this.yCoord + rand.nextInt(7);
        int cz = this.zCoord + computeRandomCoordinate(7);

        if (worldObj.getChunkFromBlockCoords(cx, cz).isChunkLoaded) {
            if (BlockFruitLeaves.haveFruit(worldObj, cx, cy, cz)) {
                return BlockFruitLeaves.harvest(worldObj, cx, cy, cz, 0, null);
            }
        }

        return false;
    }

    public static int computeRandomCoordinate(int radius) {
        return rand.nextInt(radius * 2 + 1) - radius;
    }

    private void playQueuedSound() {
        if (this.soundToRun != null) {
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, this.soundToRun, this.soundVolume, rand.nextFloat() * 0.1F + 0.9F);
            this.soundToRun = null;
        }
    }

    private void queueSound(String name) {
        this.queueSound(name, 1F);
    }

    private void queueSound(String name, float volume) {
        this.soundToRun = name;
        this.soundVolume = volume;
    }

    private boolean aquireTarget() {
        if (!worldObj.isRemote) {
            box = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            box = box.expand(8, 2, 8);

            List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, box);
            Iterator<EntityItem> it = list.iterator();
            boolean notFound = true;
            EntityItem item = null;
            while (notFound && it.hasNext()) {
                item = it.next();
                ItemStack stack = item.getEntityItem();
                Integer itemDmg = fruitList.get(stack.getItem());
                if (itemDmg != null && itemDmg == stack.getItemDamage() && canAddToInventory(item)) {
                    notFound = false;
                }
            }

            if (!notFound) {
                // fruit found, moving
                this.targetedItem = item;
                this.state = CollectorStates.targeted;
                this.cooldown = 3;

                this.sendStateUpdatePacketNew();
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private void sendStateUpdatePacketNew() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return;
        }

        FruitCollectorPacket packet = new FruitCollectorPacket();
        packet.init(this, (byte) state.ordinal(), targetedItem);

        packet.dispatcherMC17().sendToAllAround(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, collectorSyncDistance, packet);
    }

    @Override
    public String getInventoryName() {
        return "container.fruitCollector";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        eventTime = tagCompound.getInteger("eventTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("eventTime", eventTime);
    }

    @Override
    protected void doMachineWork() {
    }
}
