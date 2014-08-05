/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.jaffas.trees.common;

import monnef.jaffas.trees.block.BlockFruitLeaves;
import monnef.jaffas.trees.block.BlockFruitSapling;
import monnef.jaffas.trees.item.ItemFruitSeeds;

public class LeavesInfo {
    public BlockFruitLeaves leavesBlock;
    public BlockFruitSapling saplingBlock;
    public ItemFruitSeeds seedsItem;

    public int serialNumber;

    public LeavesInfo(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
