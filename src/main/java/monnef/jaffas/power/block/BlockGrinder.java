/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block;

import monnef.jaffas.power.block.common.BlockBasicProcessingMachine;
import monnef.jaffas.power.block.common.TileEntityBasicProcessingMachine;
import monnef.jaffas.power.client.GuiHandler;

public class BlockGrinder extends BlockBasicProcessingMachine {
    private static final float U = 1f / 16f;
    private static final float BORDER_LEFT = 5 * U;
    private static final float BORDER_BACK = 2 * U;
    private static final float BORDER_RIGHT = 4 * U;
    private static final float BORDER_FRONT = 3 * U;
    private static final float TOP = (16 - 16) * U;

    public BlockGrinder(int index, Class<? extends TileEntityBasicProcessingMachine> tileClass, GuiHandler.GuiId guiId, boolean customRenderer, boolean customRenderingId) {
        super(index, tileClass, guiId, customRenderer, customRenderingId);
        setCustomRotationSensitiveBoundingBox(BORDER_LEFT, 0, BORDER_BACK, 1 - BORDER_RIGHT, 1 - TOP, 1 - BORDER_FRONT);
    }
}
