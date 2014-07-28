/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

@ContainerRegistry.ContainerTag(slotsCount = 3, containerClassName = "monnef.jaffas.power.block.common.ContainerDoubleBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
public class TileDBPMDummy extends TileEntityBasicProcessingMachine {
    @Override
    public String getInventoryName() {
        return this.getClass().getSimpleName();
    }
}
