package monnef.jaffas.power.block;

import monnef.jaffas.power.PowerConsumerManager;
import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.PowerUtils;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerConsumerManager;
import monnef.jaffas.power.api.IPowerProvider;
import monnef.jaffas.power.api.IPowerProviderManager;
import monnef.jaffas.power.block.common.TileEntityMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import static monnef.jaffas.power.mod_jaffas_power.antenna;

public class TileEntityAntenna extends TileEntityMachine implements IPowerConsumer, IPowerProvider {
    private IPowerConsumerManager consumerManager;
    private IPowerProviderManager providerManager;

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

        PowerUtils.Disconnect(this.consumerManager.getCoordinates(), providerManager.getCoordinates());

        int rotation = antenna.getRotation(getBlockMetadata());
        rotation++;
        rotation %= ForgeDirection.VALID_DIRECTIONS.length;
        antenna.setRotation(worldObj, xCoord, yCoord, zCoord, rotation);

        tryDirectConnect();

        return ForgeDirection.VALID_DIRECTIONS[rotation];
    }

    public ForgeDirection getMyRotation() {
        return ForgeDirection.VALID_DIRECTIONS[antenna.getRotation(getBlockMetadata())];
    }

    private void tryDirectConnect() {
        if (worldObj.isRemote) return;

        ForgeDirection rot = getMyRotation();
        ForgeDirection rotInv = rot.getOpposite();
        int provX = xCoord + rotInv.offsetX;
        int provY = yCoord + rotInv.offsetY;
        int provZ = zCoord + rotInv.offsetZ;

        TileEntity te = worldObj.getBlockTileEntity(provX, provY, provZ);

        if (te instanceof IPowerProvider) {
            IPowerProvider provider = (IPowerProvider) te;
            if (provider.getPowerProviderManager().supportDirectConnection()) {
                PowerUtils.Connect(provider.getPowerProviderManager().getCoordinates(), this.consumerManager.getCoordinates(), rot);
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    protected void onTick(int number) {
        super.onTick(number);

        switch (number) {
            case 1:
                consumerManager.initialize(20, 40, this);
                providerManager.initialize(20, 40, this, true, new boolean[]{true, false, true, true, true, true});
                break;

            case 2:
                tryDirectConnect();
                break;
        }
    }

}
