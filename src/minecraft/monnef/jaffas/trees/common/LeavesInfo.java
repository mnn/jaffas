/*
 * Copyright (c) 2013 monnef.
 */

package monnef.jaffas.trees.common;

import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.block.BlockFruitSapling;
import monnef.jaffas.trees.item.ItemFruitSeeds;

public class LeavesInfo {
    public BlockFruitLeaves leavesBlock;
    public BlockFruitSapling saplingBlock;
    public ItemFruitSeeds seedsItem;

    public int leavesID;
    public int saplingID;
    public int seedsID;

    public int serialNumber;

    public LeavesInfo(int id, int saplingId, int seedsId, int serialNumber) {
        leavesID = id;
        this.serialNumber = serialNumber;
        this.saplingID = saplingId;
        this.seedsID = seedsId;
    }
}
