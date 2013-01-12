package monnef.jaffas.food.blocks;

import monnef.core.BitHelper;
import monnef.jaffas.food.blocks.BlockSink;
import monnef.jaffas.food.mod_jaffas;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntitySink extends TileEntity {
    private final static int maxDelay = 20;
    private int delay = maxDelay * 5;
    private static final Random rand = new Random();

    private String soundToRun = null;
    private float soundVolume = 1f;

    public void updateEntity() {
        playQueuedSound();

        if (getBlockType().blockID != mod_jaffas.blockSink.blockID) {
            if (mod_jaffas.debug) {
                System.out.println("sink block not detected, ending - " + xCoord + "x" + yCoord + "x" + zCoord);
            }
            invalidate();
            return;
        }

        if (!worldObj.isRemote) {
            int meta = getBlockMetadata();
            if (!BlockSink.WaterIsReady(meta)) {
                delay--;
                if (delay == 20) {
                    queueSound("water", 0.7f);
                }

                if (delay <= 0) {
                    delay = maxDelay * (2 + rand.nextInt(3));

                    int newMeta = BitHelper.setBit(meta, BlockSink.waterBit);
                    worldObj.setBlockMetadata(xCoord, yCoord, zCoord, newMeta);
                }
            }
        }

        super.updateEntity();
    }

    private void playQueuedSound() {
        if (this.soundToRun != null) {
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, this.soundToRun, this.soundVolume, this.rand.nextFloat() * 0.1F + 0.9F);
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
}
