/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.food.client;

import monnef.jaffas.food.JaffasFood;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class jaffas_EventSounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            event.manager.soundPoolSounds.addSound("sharpener.wav", JaffasFood.class.getResource("/sharpener.wav"));
            event.manager.soundPoolSounds.addSound("suck.wav", JaffasFood.class.getResource("/suck.wav"));
            event.manager.soundPoolSounds.addSound("water.ogg", JaffasFood.class.getResource("/water.ogg"));
            event.manager.soundPoolSounds.addSound("whoosh.wav", JaffasFood.class.getResource("/whoosh.wav"));
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
