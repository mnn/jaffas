/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.technic.common;

import monnef.core.common.ContainerRegistry;
import monnef.jaffas.technic.block.ContainerCobbleBreaker;
import monnef.jaffas.technic.block.ContainerCompost;
import monnef.jaffas.technic.block.ContainerFermenter;
import monnef.jaffas.technic.block.TileCobbleBreaker;
import monnef.jaffas.technic.block.TileCompostCore;
import monnef.jaffas.technic.block.TileFermenter;

public class CommonProxy {
    public void registerRenderThings() {
    }

    public void registerContainers() {
        ContainerRegistry.register(TileCobbleBreaker.class, ContainerCobbleBreaker.class);
        ContainerRegistry.register(TileCompostCore.class, ContainerCompost.class);
        ContainerRegistry.register(TileFermenter.class, ContainerFermenter.class);
    }
}
