/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

public class ProcessingMachineRecipeHandlerWrapper extends ProcessingMachineRecipeHandler {
    private static Class<? extends TileEntityBasicProcessingMachine> machineClazz;

    public static void init(Class<? extends TileEntityBasicProcessingMachine> newClazz) {
        if (machineClazz != null) {
            throw new RuntimeException("machineClazz already set.");
        }

        machineClazz = newClazz;
    }

    public ProcessingMachineRecipeHandlerWrapper() {
        super(checkTile(machineClazz));
    }

    private static Class<? extends TileEntityBasicProcessingMachine> checkTile(Class<? extends TileEntityBasicProcessingMachine> clazz) {
        if (clazz == null) {
            throw new RuntimeException("Tile is null!");
        }
        return clazz;
    }
}
