/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.power.block.common;

import monnef.jaffas.power.client.common.GuiContainerBasicProcessingMachine;
import net.minecraft.entity.player.InventoryPlayer;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;

public class ProcessingMachineRegistry {
    public static final String CANNOT_INSTANTIATE_CONTAINER = "Cannot instantiate container.";
    private static HashMap<Class<? extends TileEntityBasicProcessingMachine>, machineItem> db = new HashMap<Class<? extends TileEntityBasicProcessingMachine>, machineItem>();

    private static class machineItem {
        public final Class<? extends TileEntityBasicProcessingMachine> tileClass;
        public final Class<? extends ContainerBasicProcessingMachine> containerClass;
        public final Class<? extends GuiContainerBasicProcessingMachine> guiClass;

        public final ContainerBasicProcessingMachine container;
        public final Constructor<? extends GuiContainerBasicProcessingMachine> guiConstructor;
        public final Constructor<? extends ContainerBasicProcessingMachine> containerConstructor;

        private machineItem(Class<? extends TileEntityBasicProcessingMachine> tileClass, Class<? extends ContainerBasicProcessingMachine> containerClass, Class<? extends GuiContainerBasicProcessingMachine> guiClass, ContainerBasicProcessingMachine container) {
            this.tileClass = tileClass;
            this.containerClass = containerClass;
            this.guiClass = guiClass;
            this.container = container;
            try {
                this.guiConstructor = guiClass.getConstructor(InventoryPlayer.class, TileEntityBasicProcessingMachine.class, ContainerMachine.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Cannot get constructor of GUI of %s.", tileClass.getSimpleName()), e);
            }
            try {
                this.containerConstructor = containerClass.getConstructor(InventoryPlayer.class, TileEntityBasicProcessingMachine.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Cannot get constructor of container of %s.", tileClass.getSimpleName()), e);
            }
        }
    }

    public static void register(Class<? extends TileEntityBasicProcessingMachine> clazz, Class<? extends ContainerBasicProcessingMachine> container, Class<? extends GuiContainerBasicProcessingMachine> gui) {
        if (db.containsKey(clazz)) {
            throw new RuntimeException("containerPrototype already contains this class, cannot re-register");
        }

        ContainerBasicProcessingMachine containerInstance;
        try {
            containerInstance = container.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(CANNOT_INSTANTIATE_CONTAINER, e);
        }

        db.put(clazz, new machineItem(clazz, container, gui, containerInstance));
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

    public static GuiContainerBasicProcessingMachine createGui(TileEntityBasicProcessingMachine tile, InventoryPlayer inventory) {
        try {
            return db.get(tile.getClass()).guiConstructor.newInstance(inventory, tile, createContainer(tile, inventory));
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create new GUI for container for tile class: " + tile.getClass().getSimpleName());
        }
    }
}
