/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;

public class ProcessingMachineRecipeHandlerWrapper extends ProcessingMachineRecipeHandler {
    private static TileEntityBasicProcessingMachine tile;

    public static void init(TileEntityBasicProcessingMachine newTile) {
        if (tile != null) {
            throw new RuntimeException("Tile already set.");
        }

        tile = newTile;
    }

    public ProcessingMachineRecipeHandlerWrapper() {
        super(checkTile(tile));
    }

    private static TileEntityBasicProcessingMachine checkTile(TileEntityBasicProcessingMachine tile) {
        if (tile == null) {
            throw new RuntimeException("Tile is null!");
        }
        return tile;
    }
}
