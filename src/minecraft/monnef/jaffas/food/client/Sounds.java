/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.JaffasFood;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class Sounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            event.manager.soundPoolSounds.addSound("sharpener.wav", JaffasFood.class.getResource("/sharpener.wav"));
            event.manager.soundPoolSounds.addSound("suck.wav", JaffasFood.class.getResource("/suck.wav"));
            event.manager.soundPoolSounds.addSound("water.ogg", JaffasFood.class.getResource("/water.ogg"));
            event.manager.soundPoolSounds.addSound("whoosh.wav", JaffasFood.class.getResource("/whoosh.wav"));
            event.manager.soundPoolSounds.addSound("duck1.ogg", JaffasFood.class.getResource("/duck1.ogg"));
            event.manager.soundPoolSounds.addSound("duck2.ogg", JaffasFood.class.getResource("/duck2.ogg"));
            event.manager.soundPoolSounds.addSound("homestone.wav", JaffasFood.class.getResource("/homestone.wav"));
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
