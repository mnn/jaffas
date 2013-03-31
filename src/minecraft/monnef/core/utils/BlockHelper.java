package monnef.core.utils;

import net.minecraft.world.World;

public class BlockHelper {
    /* MCP comment
     * Flag 0x01 will pass the original block ID when notifying adjacent blocks, otherwise it will pass 0.
     * Flag 0x02 will trigger a block update both on server and on client.
     * Flag 0x04, if used with 0x02, will prevent a block update on client worlds.
     */
    public static final int NOTIFY_FLAG = 2;
    public static final int SEND_ID_OF_CHANGED_BLOCK_FLAG = 1;
    public static final int NOT_UPDATE_ON_CLIENT = 4;
    public static final int NOTIFY_NONE = 0;

    public static final int NOTIFY_ALL = NOTIFY_FLAG | SEND_ID_OF_CHANGED_BLOCK_FLAG;

    public static boolean setBlockMetadata(World world, int x, int y, int z, int metadata) {
        return world.setBlockMetadataWithNotify(x, y, z, metadata, NOTIFY_ALL);
    }

    // with notify
    public static boolean setBlock(World world, int x, int y, int z, int id) {
        return world.setBlock(x, y, z, id);
    }

    // with notify
    public static boolean setBlock(World world, int x, int y, int z, int id, int meta) {
        return world.setBlock(x, y, z, id, meta, NOTIFY_ALL);
    }

    public static boolean setBlockWithoutNotify(World world, int x, int y, int z, int id, int meta) {
        return world.setBlock(x, y, z, id, meta, NOTIFY_NONE);
    }
}
