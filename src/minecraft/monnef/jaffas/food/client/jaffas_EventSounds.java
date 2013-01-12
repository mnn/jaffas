package monnef.jaffas.food.client;

import monnef.jaffas.food.mod_jaffas;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class jaffas_EventSounds {
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        try
        {
            event.manager.soundPoolSounds.addSound("sharpener.wav", mod_jaffas.class.getResource("/sharpener.wav"));
            event.manager.soundPoolSounds.addSound("suck.wav", mod_jaffas.class.getResource("/suck.wav"));
            event.manager.soundPoolSounds.addSound("water.ogg", mod_jaffas.class.getResource("/water.ogg"));
        }
        catch (Exception e)
        {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}
