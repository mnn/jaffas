package monnef.jaffas.power.api;

import net.minecraft.tileentity.TileEntity;

public interface IPowerProvider {
    TileEntity getTile();

    /**
     * Requests energy from provider.
     *
     * @param amount An amount.
     * @return How much energy is sent (always less than maximalPowerPerPacket()).
     */
    int requestEnergy(int amount);

    int maximalPowerPerPacket();

    /**
     * @return Can it be DIRECTLY connected (by player) to a consumer?
     */
    boolean canBeConnectedToConsumer();
}
