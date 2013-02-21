package monnef.jaffas.food.client;

import monnef.jaffas.food.mod_jaffas_food;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class jaffas_EventSounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
        try {
            event.manager.soundPoolSounds.addSound("sharpener.wav", mod_jaffas_food.class.getResource("/sharpener.wav"));
            event.manager.soundPoolSounds.addSound("suck.wav", mod_jaffas_food.class.getResource("/suck.wav"));
            event.manager.soundPoolSounds.addSound("water.ogg", mod_jaffas_food.class.getResource("/water.ogg"));
            event.manager.soundPoolSounds.addSound("whoosh.wav", mod_jaffas_food.class.getResource("/whoosh.wav"));
        } catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
