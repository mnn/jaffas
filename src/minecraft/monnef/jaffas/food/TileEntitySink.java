package monnef.jaffas.food;

import monnef.core.BitHelper;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntitySink extends TileEntity {
    private final static int maxDelay = 20;
    private int delay = maxDelay;
    private static final Random rand = new Random();

    public void updateEntity() {
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
                if (delay <= 0) {
                    delay = maxDelay * (1 + rand.nextInt(3));

                    int newMeta = BitHelper.setBit(meta, BlockSink.waterBit);
                    worldObj.setBlockMetadata(xCoord, yCoord, zCoord, newMeta);
                }
            }
        }

        super.updateEntity();
    }

}
