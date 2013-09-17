package monnef.jaffas.food.block;

import monnef.core.MonnefCorePlugin;
import monnef.core.utils.BitHelper;
import monnef.core.utils.DirectionHelper;
import monnef.core.utils.IntegerCoordinates;
import monnef.jaffas.food.ContentHolder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;

import static monnef.core.utils.DirectionHelper.applyRotationsInverted;

public class BlockJDirectional extends BlockJaffas {
    private final TextureMappingType type;
    private int texturesCountPerSet;
    private int textureSetsCount;
    private boolean multipleTextureSets;

    private static HashMap<TextureMappingType, Integer[][]> sidesRotated;

    static {
        sidesRotated = new HashMap<TextureMappingType, Integer[][]>();

        Integer[][] six = new Integer[6][6];
        Integer[] seq = new Integer[]{0, 1, 2, 3, 4, 5};
        six[0] = applyRotationsInverted(seq, 4, 4);
        six[1] = applyRotationsInverted(seq);
        six[2] = applyRotationsInverted(seq, 5);
        six[3] = applyRotationsInverted(seq, 5, 1, 1);
        six[4] = applyRotationsInverted(seq, 5, 1, 1, 1);
        six[5] = applyRotationsInverted(seq, 5, 1);
        sidesRotated.put(TextureMappingType.ALL_SIDES, six);

        Integer[][] log = new Integer[3][6];
        log[0] = applyRotationsInverted(seq);
        log[1] = applyRotationsInverted(seq, 4);
        log[2] = applyRotationsInverted(seq, 4, 1);
        sidesRotated.put(TextureMappingType.LOG_LIKE, log);
    }

    public int getInventoryRenderRotation() {
        return 0;
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
        this.multipleTextureSets = textureSetsCount > 1;
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

    @Override
    public int getRenderType() {
        return ContentHolder.renderDirectionalBlockID;
    }

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
        return (meta << type.getBitsForRotationInfo()) | dirNumber;
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
        return sidesRotated.get(type)[rotation][side];
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

    public IntegerCoordinates calculateFacingBlock(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = ForgeDirection.getOrientation(getRotation(meta));
        return new IntegerCoordinates(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, world);
    }
}
