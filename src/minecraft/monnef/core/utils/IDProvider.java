package monnef.core.utils;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.util.HashSet;

public class IDProvider {
    public final int startBlockID;
    private int actualBlockID;

    public final int startItemID;
    private int actualItemID;
    private Configuration config;

    private HashSet<Integer> BlockIDsAssigned;
    private HashSet<Integer> ItemIDsAssigned;

    public IDProvider(int startBlockID, int startItemID) {
        this.startBlockID = startBlockID;
        this.actualBlockID = this.startBlockID;

        this.startItemID = startItemID;
        this.actualItemID = this.startItemID;

        this.BlockIDsAssigned = new HashSet<Integer>();
        this.ItemIDsAssigned = new HashSet<Integer>();
    }

    public int getItemID() {
        int newId = this.actualItemID;
        while (ItemIDsAssigned.contains(newId)) {
            newId++;
        }
        return this.actualItemID = newId;
    }

    public int getBlockID() {
        int newId = this.actualBlockID;
        while (BlockIDsAssigned.contains(newId)) {
            newId++;
        }
        return this.actualBlockID = newId;
    }

    public int getBlockIDFromConfig(String name) {
        int newUsedId = this.config.getBlock(name, this.getBlockID()).getInt();
        BlockIDsAssigned.add(newUsedId);
        return newUsedId;
    }

    public int getItemIDFromConfig(String name) {
        int newUsedId = this.config.getItem(name, this.getItemID()).getInt();
        ItemIDsAssigned.add(newUsedId);
        return newUsedId;
    }

    public int getEntityIDFromConfig(String name) {
        return this.config.get("entity", name, EntityRegistry.findGlobalUniqueEntityId()).getInt();
    }

    public void linkWithConfig(Configuration config) {
        this.config = config;
        loadDataFromConfig(config.getCategory(Configuration.CATEGORY_BLOCK), BlockIDsAssigned);
        loadDataFromConfig(config.getCategory(Configuration.CATEGORY_ITEM), ItemIDsAssigned);
    }

    protected void loadDataFromConfig(ConfigCategory category, HashSet<Integer> used) {
        for (Property entry : category.getValues().values()) {
            int id = entry.getInt();
            if (id != -1) {
                used.add(id);
            }
        }
    }
}
