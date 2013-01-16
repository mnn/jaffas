package monnef.test.power;

import monnef.jaffas.power.PowerConsumerManager;
import monnef.jaffas.power.PowerConsumerManagerFactory;
import monnef.jaffas.power.PowerProviderManager;
import monnef.jaffas.power.api.PowerManager;
import net.minecraftforge.common.ForgeDirection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SimpleTests {
    @Test
    public void powerManagerInit() {
        assertFalse(PowerManager.IsInitialized());
        PowerManager.InitializeFactory(new PowerConsumerManagerFactory());
        assertTrue(PowerManager.IsInitialized());
        Assert.assertNotNull(PowerManager.CreatePowerConsumerManager());
    }

    @Test
    public void powerConsumerManager() {
        PowerConsumerManager p = new PowerConsumerManager();
        p.initialize(25, 55, null);
        assertFalse(p.isConnected());
        assertEquals(55, p.getBufferSize());
        assertEquals(25, p.getMaximalPacketSize());
        assertTrue(p.energyNeeded());
        assertFalse(p.hasBuffered(10));
        assertFalse(p.hasBuffered(1));

        assertEquals(25, p.store(25));
        assertEquals(25, p.getCurrentMaximalPacketSize());
        assertEquals(25, p.store(25));
        assertEquals(5, p.getFreeSpaceInBuffer());
        assertEquals(50, p.getCurrentBufferedEnergy());

        assertEquals(5, p.store(25));
        assertEquals(0, p.getCurrentMaximalPacketSize());
        assertEquals(0, p.store(25));
        assertEquals(0, p.getFreeSpaceInBuffer());
        assertEquals(55, p.getCurrentBufferedEnergy());
        assertFalse(p.energyNeeded());
        assertTrue(p.hasBuffered(55));
        assertTrue(p.hasBuffered(1));

        assertEquals(10, p.consume(10));
        assertTrue(p.energyNeeded());
        assertEquals(45, p.getCurrentBufferedEnergy());
        assertEquals(10, p.getCurrentMaximalPacketSize());
        assertEquals(45, p.consume(100));
        assertEquals(25, p.getCurrentMaximalPacketSize());
        assertFalse(p.hasBuffered(1));
        assertTrue(p.energyNeeded());
    }

    @Test
    public void powerProviderManager() {
        PowerProviderManager m = new PowerProviderManager();
        int space = 45;
        m.initialize(20, space, null, true, (byte) 0);

        assertTrue(m.hasFreeSlotForRemotePower());
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            assertFalse(m.sideProvidesPower(dir));
        }
        assertFalse(m.isRemotelyConnected());

        assertEquals(space, m.getFreeSpaceInBuffer());
        assertEquals(0, m.getCurrentBufferedEnergy());

        assertEquals(space, m.storeEnergy(space + 5));
        assertEquals(0, m.getFreeSpaceInBuffer());
        assertEquals(space, m.getCurrentBufferedEnergy());
    }
}



