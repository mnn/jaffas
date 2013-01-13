package monnef.jaffas.power.api;

public interface IPowerConsumerManager {
    int getMaximalPacketSize();

    void setMaximalPacketSize(int size);

    int getBufferSize();

    void setBufferSize(int size);

    int getCurrentMaximalPacketSize();

    boolean hasBuffered(int energy);

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

    void connect(IPowerProvider provider);

    void disconnect();

    boolean isConnected();

    /**
     * Is buffer not full?
     *
     * @return Power consumer wants juice.
     */
    boolean energyNeeded();

    /**
     * Only in this method will manager request power (maximal one packet).
     * Do NOT call more often than once per tick.
     */
    void tick();
}
