/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.technic.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.jaffas.technic.JaffasTechnic;
import monnef.jaffas.technic.common.CommonProxy;
import monnef.jaffas.technic.entity.EntityLocomotive;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLocomotive.class, new RenderLocomotive());
    }
}