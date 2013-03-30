package monnef.jaffas.food.client;

import monnef.jaffas.food.jaffasFood;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class jaffas_EventSounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            event.manager.soundPoolSounds.addSound("sharpener.wav", jaffasFood.class.getResource("/sharpener.wav"));
            event.manager.soundPoolSounds.addSound("suck.wav", jaffasFood.class.getResource("/suck.wav"));
            event.manager.soundPoolSounds.addSound("water.ogg", jaffasFood.class.getResource("/water.ogg"));
            event.manager.soundPoolSounds.addSound("whoosh.wav", jaffasFood.class.getResource("/whoosh.wav"));
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
