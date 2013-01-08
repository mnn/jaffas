package monnef.core;

import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;

public class IDProvider {
    public final int startBlockID;
    private int actualBlockID;

    public final int startItemID;
    private int actualItemID;
    private Configuration config;

    public IDProvider(int startBlockID, int startItemID) {
        this.startBlockID = startBlockID;
        this.actualBlockID = this.startBlockID;

        this.startItemID = startItemID;
        this.actualItemID = this.startItemID;
    }

    public int getItemID() {
        return this.actualItemID++;
    }

    public int getBlockID() {
        return this.actualBlockID++;
    }

    public int getBlockIDFromConfig(String name) {
        return this.config.getBlock(name, this.getBlockID()).getInt();
    }

    public int getItemIDFromConfig(String name) {
        return this.config.getItem(name, this.getItemID()).getInt();
    }

    public int getEntityIDFromConfig(String name) {
        return this.config.get("entity", name, ModLoader.getUniqueEntityId()).getInt();
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }
}
