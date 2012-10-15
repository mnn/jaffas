package monnef.jaffas.food;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;

public class ServerTickHandler implements IScheduledTickHandler {


    @Override
    public int nextTickSpacing() {
        return 0;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.equals(EnumSet.of(TickType.SERVER))) {
            onTick();
        }
    }

    private void onTick() {

    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel() {
        return null;
    }
}
