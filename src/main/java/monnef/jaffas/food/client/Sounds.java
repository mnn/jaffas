/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.core.client.ResourcePathHelper;
import monnef.jaffas.food.common.Reference;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

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

    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            for (SoundsEnum sound : SoundsEnum.values()) {
                String[] names = sound.getFileNames();
                for (int i = 0; i < names.length; i++) {
                    String tag = ResourcePathHelper.assemble(names[i], Reference.ModName.toLowerCase(), ResourcePathHelper.ResourceTextureType.SOUND);
                    event.manager.soundPoolSounds.addSound(tag);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
