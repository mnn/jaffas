/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProcessingMachineRegistry {
    public static final String CANNOT_INSTANTIATE_CONTAINER = "Cannot instantiate container.";
    private static HashMap<Class<? extends TileEntityBasicProcessingMachine>, MachineItem> db = new HashMap<Class<? extends TileEntityBasicProcessingMachine>, MachineItem>();

    public static class MachineItem {
        public final Class<? extends TileEntityBasicProcessingMachine> tileClass;
        public final Class<? extends ContainerBasicProcessingMachine> containerClass;
        private Class<?> guiClass; // GuiContainerBasicProcessingMachine

        public final ContainerBasicProcessingMachine container;
        private Constructor<?> guiConstructor; // GuiContainerBasicProcessingMachine
        public final Constructor<? extends ContainerBasicProcessingMachine> containerConstructor;

        private MachineItem(Class<? extends TileEntityBasicProcessingMachine> tileClass, Class<? extends ContainerBasicProcessingMachine> containerClass, ContainerBasicProcessingMachine container) {
            this.tileClass = tileClass;
            this.containerClass = containerClass;
            this.container = container;

            try {
                this.containerConstructor = containerClass.getConstructor(InventoryPlayer.class, TileEntityBasicProcessingMachine.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Cannot get constructor of container of %s.", tileClass.getSimpleName()), e);
            }
        }

        private void setGuiConstructor(Constructor<?> guiConstructor) {
            if (this.guiConstructor != null) throw new RuntimeException("GUI constructor already set");
            this.guiConstructor = guiConstructor;
        }

        public void setGuiClass(Class<?> guiClass) {
            this.guiClass = guiClass;
            if (guiClass != null) {
                try {
                    setGuiConstructor(guiClass.getConstructor(InventoryPlayer.class, TileEntityBasicProcessingMachine.class, ContainerMachine.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(String.format("Cannot get constructor of GUI of %s.", tileClass.getSimpleName()), e);
                }
            } else setGuiConstructor(null);
        }

        public Constructor<?> getGuiConstructor() {
            return guiConstructor;
        }

        public Class<?> getGuiClass() {
            return guiClass;
        }
    }

    public static void register(Class<? extends TileEntityBasicProcessingMachine> clazz, Class<? extends ContainerBasicProcessingMachine> container) {
        if (db.containsKey(clazz)) {
            throw new RuntimeException("containerPrototype already contains this class, cannot re-register");
        }

        ContainerBasicProcessingMachine containerInstance;
        try {
            containerInstance = container.newInstance();
            containerInstance.constructSlots(new InventoryBasic("dummy", false, containerInstance.getSlotsCount()));
        } catch (Throwable e) {
            throw new RuntimeException(CANNOT_INSTANTIATE_CONTAINER, e);
        }

        db.put(clazz, new MachineItem(clazz, container, containerInstance));
    }

    public static void registerOnClient(Class<? extends TileEntityBasicProcessingMachine> clazz, Class<?> gui) {
        MachineItem item = db.get(clazz);
        if (item == null) throw new RuntimeException("Registering GUI container with unknown TE.");
        if (!GuiContainerBasicProcessingMachine.class.isAssignableFrom(gui)) {
            throw new RuntimeException("Class doesn't inherit from proper ancestor!");
        }
        item.setGuiClass(gui);
    }

    public static void assertAllItemsHasGuiClass() {
        for (Map.Entry<Class<? extends TileEntityBasicProcessingMachine>, MachineItem> item : db.entrySet()) {
            if (item.getValue().getGuiClass() == null) {
                throw new RuntimeException("TE " + item.getKey().getSimpleName() + " is missing GUI mapping.");
            }
        }
    }

    public static ContainerBasicProcessingMachine getContainerPrototype(Class<? extends TileEntityBasicProcessingMachine> clazz) {
        return db.get(clazz).container;
    }

    public static Collection<Class<? extends TileEntityBasicProcessingMachine>> getTileClasses() {
        return db.keySet();
    }

    public static ContainerBasicProcessingMachine createContainer(TileEntityBasicProcessingMachine tile, InventoryPlayer inventory) {
        try {
            return db.get(tile.getClass()).containerConstructor.newInstance(inventory, tile);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create new container for tile class: " + tile.getClass().getSimpleName());
        }
    }

    public static MachineItem getItem(Class tileClass) {
        return db.get(tileClass);
    }
}
