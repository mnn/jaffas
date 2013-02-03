package monnef.jaffas.power;

import monnef.core.IntegerCoordinates;
import monnef.jaffas.power.api.IPowerConsumer;
import monnef.jaffas.power.api.IPowerNode;
import monnef.jaffas.power.api.IPowerNodeCoordinates;
import monnef.jaffas.power.api.IPowerProvider;
import net.minecraft.world.World;

public class PowerNodeCoordinates extends IntegerCoordinates implements IPowerNodeCoordinates {
    public PowerNodeCoordinates(int x, int y, int z, World world) {
        super(x, y, z, world);
    }

    @Override
    public IPowerNode asNode() {
        return (IPowerNode) getTile();
    }

    @Override
    public IPowerProvider asProvider() {
        return (IPowerProvider) getTile();
    }

    @Override
    public IPowerConsumer asConsumer() {
        return (IPowerConsumer) getTile();
    }
}
