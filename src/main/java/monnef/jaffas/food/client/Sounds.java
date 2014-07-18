/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.ResourcePathHelper;
import monnef.jaffas.food.common.Reference;

/* Parts are obsolete, because Mojang introduced those bothering json files. */
public class Sounds {
    public enum SoundsEnum {
        COLLECTOR_NOISE("sharpener.wav"),
        COLLECTOR_SUCK("suck.wav"),
        WATER("water.ogg"),
        WHOOSH("whoosh.wav"), // used?
        DUCK("duck1.ogg", "duck2.ogg"),
        HOMESTONE("homestone.wav"),;

        private final String[] fileNames;
        private final String soundName;

        SoundsEnum(String... fileNames) {
            if (fileNames == null || fileNames.length <= 0) throw new RuntimeException("Incorrect sound file name.");
            this.fileNames = fileNames;
            String fName = fileNames[0];
            fName = fName.substring(0, fName.lastIndexOf('.'));
            while (true) {
                char lastChar = fName.charAt(fName.length() - 1);
                if (Character.isDigit(lastChar)) {
                    fName = fName.substring(0, fName.length() - 1);
                } else {
                    break;
                }
            }
            this.soundName = ResourcePathHelper.assemble(fName, Reference.ModName.toLowerCase(), ResourcePathHelper.ResourceTextureType.SOUND);
        }

        public String[] getFileNames() {
            return fileNames;
        }

        public String getSoundName() {
            return soundName;
        }
    }
}
