/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class Sounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            event.manager.soundPoolSounds.addSound("sharpener.wav");
            event.manager.soundPoolSounds.addSound("suck.wav");
            event.manager.soundPoolSounds.addSound("water.ogg");
            event.manager.soundPoolSounds.addSound("whoosh.wav");
            event.manager.soundPoolSounds.addSound("duck1.ogg");
            event.manager.soundPoolSounds.addSound("duck2.ogg");
            event.manager.soundPoolSounds.addSound("homestone.wav");
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
