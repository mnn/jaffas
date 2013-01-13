package monnef.jaffas.power.api;

import net.minecraft.tileentity.TileEntity;

// classic: generator -> toaster
// [ provider -> ] [ -> consumer ]

// distribution: (generator) -> {antenna} -> /toaster\
// ([ provider -> ]) {[ -> consumer ] [ provider -> ]} ... /[ -> consumer ]\

public interface IPowerProvider {
    TileEntity getTile();

    /**
     * Requests energy from provider.
     * Energy returned might be less, than requested.
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

    void connect(IPowerConsumer output);

    void disconnect();
}
