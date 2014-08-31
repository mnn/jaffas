/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.client;

import monnef.core.client.GuiContainerMachine;
import monnef.core.client.GuiContainerMonnefCore;
import monnef.core.utils.LanguageHelper;
import monnef.jaffas.trees.block.ContainerFruitCollector;
import monnef.jaffas.trees.block.TileFruitCollector;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiFruitCollector extends GuiContainerMachine {

    public GuiFruitCollector(InventoryPlayer inventoryPlayer, TileFruitCollector tileEntity, ContainerFruitCollector container) {
        super(inventoryPlayer, tileEntity, container);
        setBackgroundTexture("guicollector.png");
    }
}