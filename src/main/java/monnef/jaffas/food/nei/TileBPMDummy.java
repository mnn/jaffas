/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

@ContainerRegistry.ContainerTag(slotsCount = 2, containerClassName = "monnef.jaffas.power.block.common.ContainerBasicProcessingMachine", guiClassName = "monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine")
public class TileBPMDummy extends TileEntityBasicProcessingMachine {
    @Override
    public String getInvName() {
        return this.getClass().getSimpleName();
    }
}

