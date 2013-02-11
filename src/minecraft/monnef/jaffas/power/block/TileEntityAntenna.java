package monnef.jaffas.power.block;

import monnef.jaffas.power.PowerConsumerManager;
import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.PowerUtils;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityAntenna extends TileEntityMachine implements IPowerConsumer, IPowerProvider {
    private IPowerConsumerManager consumerManager;
    private IPowerProviderManager providerManager;
    private boolean lit;

    public TileEntityAntenna() {
        this.providerManager = new PowerProviderManager();
        this.consumerManager = new PowerConsumerManager();
    }

    @Override
    public String getMachineTitle() {
        return "Antenna";
    }

    @Override
    public IPowerConsumerManager getPowerConsumerManager() {
        return this.consumerManager;
    }

    @Override
    public IPowerProviderManager getPowerProviderManager() {
        return this.providerManager;
    }

    public ForgeDirection changeRotation() {
        if (worldObj.isRemote) return ForgeDirection.UNKNOWN;

        PowerUtils.disconnect(this.consumerManager.getCoordinates(), providerManager.getCoordinates());

        int rotation = this.getRotation().ordinal();
        rotation++;
        rotation %= ForgeDirection.VALID_DIRECTIONS.length;
        this.setRotation(ForgeDirection.getOrientation(rotation));

        tryDirectConnect();

        sendUpdate();

        return ForgeDirection.VALID_DIRECTIONS[rotation];
    }

    private void tryDirectConnect() {
        if (worldObj.isRemote) return;

        ForgeDirection rot = getRotation();
        ForgeDirection rotInv = rot.getOpposite();
        int provX = xCoord + rotInv.offsetX;
        int provY = yCoord + rotInv.offsetY;
        int provZ = zCoord + rotInv.offsetZ;

        TileEntity te = worldObj.getBlockTileEntity(provX, provY, provZ);

        if (te instanceof IPowerProvider) {
            IPowerProvider provider = (IPowerProvider) te;
            if (provider.getPowerProviderManager().supportDirectConnection()) {
                PowerUtils.connect(provider.getPowerProviderManager().getCoordinates(), this.consumerManager.getCoordinates(), rot);
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        consumerManager.tick();
        providerManager.tick();
    }

    @Override
    protected void doWork() {
        super.doWork();
        transferPower();
    }

    private void transferPower() {
        if (consumerManager.hasBuffered(1) && providerManager.getFreeSpaceInBuffer() > 0) {
            int toTransfer = providerManager.getFreeSpaceInBuffer();
            if (!consumerManager.hasBuffered(toTransfer)) toTransfer = consumerManager.getCurrentBufferedEnergy();

            if (consumerManager.consume(toTransfer) != toTransfer) {
                System.err.println("consumed != transfered");
            }
            if (providerManager.storeEnergy(toTransfer) != toTransfer) {
                System.err.println("stored != transfered");
            }
        }
    }

    @Override
    protected void onTick(int number) {
        super.onTick(number);

        switch (number) {
            case 1:
                consumerManager.initialize(20, 20, this);
                providerManager.initialize(20, 20, this, true, new boolean[]{true, false, true, true, true, true});
                break;

            case 2:
                tryDirectConnect();
                break;
        }
    }

    public boolean isLit() {
        return lit;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        providerManager.readFromNBT(tag);
        consumerManager.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        consumerManager.writeToNBT(tag);
        providerManager.writeToNBT(tag);
    }
}
