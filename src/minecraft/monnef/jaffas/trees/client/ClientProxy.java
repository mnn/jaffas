/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.client;

import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
    }

    @Override
    public void setFancyGraphicsLevel(BlockFruitLeaves leaves, boolean value) {
        leaves.setGraphicsLevel(value);
    }

    @Override
    public void addEffect(String s, World world, double d1, double d2, double d3, double d4, double d5, double d6) {
        EntityFX efx = null;

        if (s == "sucking") {
            efx = new EntitySuckingFX(world, d1, d2, d3, d4, d5, d6);
        }

        if (efx != null) {
            Minecraft.getMinecraft().effectRenderer.addEffect(efx);
        }
    }
}
