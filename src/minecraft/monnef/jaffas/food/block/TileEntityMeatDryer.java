/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.block;

import monnef.core.utils.Interval;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMeatDryer extends TileEntity {
    public static final int MEAT_COUNT = 4;

    private static final int tickQuantum = 20;
    private static final int TPS = 20;
    private static final int NOTHING_PLANNED_TICKS = TPS * 60 * 5;
    private static final String MEAT_TAG = "meat";

    private int counter = 0;
    private MeatStatus[] meats = new MeatStatus[MEAT_COUNT];

    public TileEntityMeatDryer() {
        for (int i = 0; i < meats.length; i++) {
            meats[i] = new MeatStatus();
        }
    }

    private static int calcTicks(int seconds) {
        return (seconds * TPS) / tickQuantum;
    }

    public enum MeatState {
        ZOMBIE_RAW(2, 3),
        ZOMBIE_HALF_DONE(1, 5),
        ZOMBIE_DONE,
        NORMAL_RAW(1, 2),
        NORMAL_HALF_DONE(1, 3),
        NORMAL_DONE,
        NO_MEAT;

        private final Interval duration;

        MeatState() {
            duration = null;
        }

        MeatState(int minMinutes, int maxMinutes) {
            this.duration = new Interval(calcTicks(minMinutes * 60), calcTicks(maxMinutes * 60));
        }

        public int getStateDurationRandomly() {
            return duration == null ? 0 : duration.getRandom();
        }

        public MeatState getNextStage() {
            switch (this) {
                case ZOMBIE_DONE:
                case NORMAL_DONE:
                case NO_MEAT:
                    return this;

                case ZOMBIE_RAW:
                    return ZOMBIE_HALF_DONE;

                case ZOMBIE_HALF_DONE:
                    return ZOMBIE_DONE;

                case NORMAL_RAW:
                    return NORMAL_HALF_DONE;

                case NORMAL_HALF_DONE:
                    return NORMAL_DONE;
            }

            throw new RuntimeException("unknown meat");
        }
    }

    private static class MeatStatus {
        private static final String MEAT_STATE_TAG = "meatState";
        private static final String TICKS_LEFT_TAG = "ticksLeft";
        private MeatState state;
        private int ticksLeft;

        public MeatStatus() {
            state = MeatState.NO_MEAT;
        }

        public void setNewMeat(MeatState newState) {
            if (newState != MeatState.ZOMBIE_RAW && newState != MeatState.NORMAL_RAW) {
                throw new RuntimeException("Incorrect parameter in newMeat");
            }
            if (state != MeatState.NO_MEAT) {
                throw new RuntimeException("Overriding meat");
            }

            state = newState;
            computeNewTicksLeft();
        }

        private void computeNewTicksLeft() {
            ticksLeft = state.getStateDurationRandomly();
            if (ticksLeft == 0) ticksLeft = NOTHING_PLANNED_TICKS;
        }

        // only on a server side
        public void tick() {
            ticksLeft -= tickQuantum;
            if (ticksLeft <= 0) {
                ticksLeft = 0;
                state = state.getNextStage();
                computeNewTicksLeft();
            }
        }

        public void saveToNBT(NBTTagCompound tag) {
            tag.setByte(MEAT_STATE_TAG, (byte) state.ordinal());
            tag.setInteger(TICKS_LEFT_TAG, ticksLeft);
        }

        public void loadFromNBT(NBTTagCompound tag) {
            state = MeatState.values()[tag.getByte(MEAT_STATE_TAG)];
            ticksLeft = tag.getInteger(TICKS_LEFT_TAG);
        }
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        counter++;
        if (counter % tickQuantum == 0) {
            tickMeats();
        }
    }

    private void tickMeats() {
        for (MeatStatus meat : meats) {
            meat.tick();
        }
    }

    public MeatState getCurrentMeatState(int meatNumber) {
        return meats[meatNumber].state;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < meats.length; i++) {
            NBTTagCompound comp = tag.getCompoundTag(MEAT_TAG + i);
            meats[i].loadFromNBT(comp);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        for (int i = 0; i < meats.length; i++) {
            NBTTagCompound comp = new NBTTagCompound();
            meats[i].saveToNBT(comp);
            tag.setCompoundTag(MEAT_TAG + i, comp);
        }
    }

    // TODO: placing and harvesting of meat
}
