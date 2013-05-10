/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food;

import com.google.common.base.Optional;
import cpw.mods.fml.common.registry.EntityRegistry;
import extrabiomes.api.Biomes;
import monnef.core.MonnefCorePlugin;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import sun.rmi.runtime.Log;

import static monnef.core.MonnefCorePlugin.Log;

public class ExtrabiomesHelper {
    /**
     * Valid values:
     * <p/>
     * <pre>
     * ALPINE
     * AUTUMNWOODS
     * BIRCHFOREST
     * EXTREMEJUNGLE
     * FORESTEDHILLS
     * FORESTEDISLAND
     * GLACIER
     * GREENHILLS
     * GREENSWAMP
     * ICEWASTELAND
     * MARSH
     * MEADOW
     * MINIJUNGLE
     * MOUNTAINDESERT
     * MOUNTAINRIDGE
     * MOUNTAINTAIGA
     * PINEFOREST
     * RAINFOREST
     * REDWOODFOREST
     * REDWOODLUSH
     * SAVANNA
     * SHRUBLAND
     * SNOWYFOREST
     * SNOWYRAINFOREST
     * TEMPORATERAINFOREST
     * TUNDRA
     * WASTELAND
     * WOODLANDS
     * </pre>
     */
    public static void addSpawn(Class<? extends EntityLiving> mob, int probability, int min, int max, EnumCreatureType type, String... biomeNames) {
        for (String biome : biomeNames) {
            Optional<BiomeGenBase> tmp = Biomes.getBiome(biome);
            if (tmp.isPresent()) {
                EntityRegistry.addSpawn(mob, probability, min, max, type, tmp.orNull());
            } else {
                Log.printSevere("Error while adding monster spawn to biome: " + biome);
            }
        }
    }
}
