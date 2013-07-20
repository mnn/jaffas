/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.TileEntityMachineWithInventory;

public class TileWebHarvester extends TileEntityMachineWithInventory {
    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public String getInvName() {
        return "jaffas.power.webHarvester";
    }

    @Override
    public String getMachineTitle() {
        return "Web Harvester";
    }

    @Override
    public void doWork() {

    }
}
