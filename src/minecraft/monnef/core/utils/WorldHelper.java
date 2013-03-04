package monnef.core.utils;

import net.minecraft.world.World;

import java.util.List;

public class WorldHelper {
    public static final int WORLD_HEIGHT = 256;

    public static void getBlocksInBox(List<IntegerCoordinates> res, World w, int x, int y, int z, int radius, int bottomSize, int topSize, int blockId, int metadata) {
        if (bottomSize == -1) bottomSize = y;
        if (topSize == -1) topSize = WORLD_HEIGHT - y;

        for (int ax = x - radius; ax <= x + radius; ax++)
            for (int ay = y - bottomSize; ay <= y + topSize; ay++)
                for (int az = z - radius; az <= z + radius; az++) {
                    int id = w.getBlockId(ax, ay, az);
                    if (id == blockId) {
                        if (metadata == -1 || w.getBlockMetadata(ax, ay, az) == metadata) {
                            res.add(new IntegerCoordinates(ax, ay, az, w));
                        }
                    }
                }
    }
}
