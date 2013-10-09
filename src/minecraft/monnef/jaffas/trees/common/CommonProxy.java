/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.common;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.block.ContainerFruitCollector;
import monnef.jaffas.trees.block.TileFruitCollector;
import net.minecraft.world.World;

public class CommonProxy {
    public void registerRenderThings() {
    }

    public void setFancyGraphicsLevel(BlockFruitLeaves leaves, boolean value) {
    }

    public void addEffect(String s, World world, double d1, double d2, double d3, double d4, double d5, double d6) {
    }

    public void registerContainers(){
        ContainerRegistry.register(TileFruitCollector.class, ContainerFruitCollector.class);
    }
}
