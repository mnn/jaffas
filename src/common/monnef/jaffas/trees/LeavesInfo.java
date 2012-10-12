package monnef.jaffas.trees;

/**
 * Created with IntelliJ IDEA.
 * User: moen
 * Date: 13/10/12
 * Time: 00:20
 * To change this template use File | Settings | File Templates.
 */
public class LeavesInfo {
    public BlockFruitLeaves block;
    public int ID;
    public int serialNumber;

    public void LeavesInfo(int id, int serialNumber) {
        ID = id;
        this.serialNumber = serialNumber;
    }
}
