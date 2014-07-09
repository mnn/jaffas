/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;

public class JaffasRegistryHelper {
    public static final String JAFFAS_TE_PREFIX = "jaffas";

    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        id = JAFFAS_TE_PREFIX + "." + id;
        GameRegistry.registerTileEntity(tileEntityClass, id);
    }
}
