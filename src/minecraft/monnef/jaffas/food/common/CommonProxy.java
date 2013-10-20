/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.network.Player;
import monnef.core.common.ContainerRegistry;
import monnef.jaffas.food.block.ContainerBoard;
import monnef.jaffas.food.block.ContainerFridge;
import monnef.jaffas.food.block.ContainerRipeningBox;
import monnef.jaffas.food.block.TileBoard;
import monnef.jaffas.food.block.TileFridge;
import monnef.jaffas.food.block.TileRipeningBox;

public class CommonProxy {
    public void registerRenderThings() {
    }

    public int addArmor(String name) {
        return 0;
    }

    public void registerSounds() {
    }

    public void registerTickHandler() {
    }

    public void handleSyncPacket(Player player, int secondsRemaining, boolean openGUI) {
    }

    public void registerEggRendering() {
    }

    public int getCommonRarity() {
        return 0;
    }

    public int getEpicRarity() {
        return 0;
    }

    public int getUncommonRarity() {
        return 0;
    }
}
