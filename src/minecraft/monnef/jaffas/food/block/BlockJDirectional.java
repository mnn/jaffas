package monnef.jaffas.food.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BitHelper;
import monnef.core.utils.DirectionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockJDirectional extends BlockJaffas {
    private final TextureMappingType type;
    private int texturesCountPerSet;
    private int textureSetsCount;
    private boolean multipleTextureSets;

    private static Integer[][] sidesRotated;

    static {
        sidesRotated = new Integer[6][6];

        Integer[] seq = new Integer[]{0, 1, 2, 3, 4, 5};
        sidesRotated[0] = DirectionHelper.applyRotations(seq, new Integer[]{4, 4});
        sidesRotated[1] = DirectionHelper.applyRotations(seq, new Integer[]{});
        sidesRotated[2] = DirectionHelper.applyRotations(seq, new Integer[]{5});
        sidesRotated[3] = DirectionHelper.applyRotations(seq, new Integer[]{5, 1, 1});
        sidesRotated[4] = DirectionHelper.applyRotations(seq, new Integer[]{5, 1, 1, 1});
        sidesRotated[5] = DirectionHelper.applyRotations(seq, new Integer[]{5, 1});
    }

    public enum TextureMappingType {
        ALL_SIDES(3),  // 8 rotations
        LOG_LIKE(2),   // 3 rotations
        ;
        private int bitsForRotationInfo;
        private final int rotationMask;

        TextureMappingType(int bitsForRotationInfo) {
            this.bitsForRotationInfo = bitsForRotationInfo;
            this.rotationMask = BitHelper.generateOnes(bitsForRotationInfo);
        }

        public int getBitsForRotationInfo() {
            return bitsForRotationInfo;
        }

        public int getRotationMask() {
            return rotationMask;
        }

    }

    public BlockJDirectional(int id, int textureStart, int texturesCountPerSet, Material material, TextureMappingType type) {
        super(id, textureStart, material);
        this.type = type;
        this.multipleTextureSets = false;
        setIconsCount(texturesCountPerSet);
    }

    public BlockJDirectional(int id, int textureStart, int texturesCountPerSet, Material material, TextureMappingType type, int textureSetsCount) {
        this(id, textureStart, texturesCountPerSet, material, type);
        this.texturesCountPerSet = texturesCountPerSet;
        this.textureSetsCount = textureSetsCount;
        this.multipleTextureSets = true;
        setIconsCount(textureSetsCount * texturesCountPerSet);
    }

    public TextureMappingType getType() {
        return type;
    }

    public int getRotation(int meta) {
        return meta & type.getRotationMask();
    }

    public int getCustomData(int meta) {
        return meta >> type.getBitsForRotationInfo();
    }

    /*
    @Override
    public int getRenderType() {
        return JaffasFood.renderDirectionalBlockID;
    }
    */

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (MonnefCorePlugin.debugEnv) {
            if (!world.isRemote) {
                int meta = world.getBlockMetadata(x, y, z);
                int rotation = getRotation(meta);
                int idx = getCustomIconMapping(side, rotation, getCustomData(meta));
                player.addChatMessage(String.format("meta: %d, side: %d, rot: %d, gcim: %d", meta, side, rotation, idx));
            }
        }

        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        ForgeDirection realDirection = ForgeDirection.getOrientation(side).getOpposite();
        if (realDirection == ForgeDirection.UNKNOWN) {
            throw new RuntimeException("wrong side in directional block in onPlaced handler");
        }

        int dirNumber = convertDirectionToMappingDirection(realDirection, type);
        return meta << type.getBitsForRotationInfo() | dirNumber;
    }

    protected static int convertDirectionToMappingDirection(ForgeDirection direction, TextureMappingType type) {
        int dirNumber = -1;

        switch (type) {
            case LOG_LIKE:
                switch (direction) {
                    case UP:
                    case DOWN:
                        dirNumber = 0;
                        break;

                    case NORTH:
                    case SOUTH:
                        dirNumber = 1;
                        break;

                    case WEST:
                    case EAST:
                        dirNumber = 2;
                        break;
                }
                break;

            case ALL_SIDES:
                dirNumber = direction.ordinal();
                break;

            default:
                throw new RuntimeException("non-defined behaviour for " + type + " in onPlaced handler");
        }

        if (dirNumber == -1) {
            throw new RuntimeException("direction not set");
        }

        return dirNumber;
    }

    @Override
    public Icon getIcon(int side, int meta) {
        int idx = getCustomIconMapping(side, getRotation(meta), getCustomData(meta));

        return icons[idx];
    }

    /**
     * @param side       Block side.
     * @param rotation   Block rotation (from metadata).
     * @param customData Extracted from metadata (without side info).
     * @return An index to icons field.
     */
    public int getCustomIconMapping(int side, int rotation, int customData) {
        int rotatedSide = processRotation(side, rotation, type);
        int resultSide = getCustomIconMappingWithoutRotation(rotatedSide, customData);
        return processSideToIconIndexMapping(resultSide);
    }

    /**
     * Applies rotation to the side.
     *
     * @param side     Input side.
     * @param rotation Rotation.
     * @param type     Direction type.
     * @return Rotated side.
     */
    public static int processRotation(int side, int rotation, TextureMappingType type) {
        return sidesRotated[rotation][side];
    }

    /**
     * Maps side number from template (e.g. log: 0,1) to a texture index.
     *
     * @param side The side.
     * @return Texture index.
     */
    protected int processSideToIconIndexMapping(int side) {
        return side;
    }

    /**
     * Maps block side to type side (e.g. log has only 0 - hearth and 1 - bark).
     *
     * @param side       Input side.
     * @param customData Leftover data bits from metadata.
     * @return Logical (type) side.
     */
    public int getCustomIconMappingWithoutRotation(int side, int customData) {
        int multiTextureOffset = multipleTextureSets ? customData * texturesCountPerSet : 0;

        ForgeDirection dir = ForgeDirection.getOrientation(side);
        if (dir == ForgeDirection.UNKNOWN) {
            throw new RuntimeException("unknown direction detected");
        }
        int textureIndex = -1;

        switch (type) {
            case LOG_LIKE:
                textureIndex = DirectionHelper.isYAxis(dir) ? 0 : 1;
                break;

            case ALL_SIDES:
                textureIndex = dir.ordinal();
                break;

            default:
                throw new RuntimeException("non-defined behaviour for " + type);
        }

        if (textureIndex == -1) {
            throw new RuntimeException("cannot map un-rotated side -> texture");
        }

        return textureIndex + multiTextureOffset;
    }
}
