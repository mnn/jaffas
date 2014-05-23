/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.world;

import monnef.jaffas.food.common.ConfigurationManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WorldGenHelper {
    private static final Set<Integer> defaultDimensions = new HashSet<Integer>(Arrays.asList(-1, 0, 1));

    public static boolean isGenerationEnabled(int dimensionID) {
        if (ConfigurationManager.genDisabled) {
            return false;
        }
        if (defaultDimensions.contains(dimensionID)) {
            return true;
        }

        if (ConfigurationManager.genDisabledForNonStandardDimensions) {
            return false;
        } else {
            return true;
        }
    }
}
