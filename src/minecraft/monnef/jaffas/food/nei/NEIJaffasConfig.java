/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.nei;

import codechicken.nei.api.IConfigureNEI;
import monnef.jaffas.food.common.Reference;

public class NEIJaffasConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {

    }

    @Override
    public String getName() {
        return Reference.ModName;
    }

    @Override
    public String getVersion() {
        return Reference.Version;
    }
}
