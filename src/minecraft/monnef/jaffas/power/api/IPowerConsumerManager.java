package monnef.jaffas.power.api;

public interface IPowerConsumerManager {
    int getMaximalPacketSize();

    void setMaximalPacketSize(int size);

    int getBufferSize();

    void setBufferSize(int size);

    int getCurrentMaximalPacketSize();

    boolean haveBuffered(int energy);

    /**
     * Consumes energy from a buffer.
     *
     * @param energy Energy amount.
     * @return Energy successfully consumed.
     */
    int consume(int energy);

    /**
     * Stores energy in a buffer (called by antennas).
     *
     * @param energy Energy amount.
     * @return Energy successfully stored.
     */
    int store(int energy);
}
