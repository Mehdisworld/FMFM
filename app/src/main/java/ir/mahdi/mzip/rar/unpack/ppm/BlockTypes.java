package ir.mahdi.mzip.rar.unpack.ppm;

public enum BlockTypes {
    BLOCK_LZ(0),
    BLOCK_PPM(1);
    
    private int blockType;

    private BlockTypes(int i) {
        this.blockType = i;
    }

    public static BlockTypes findBlockType(int i) {
        BlockTypes blockTypes = BLOCK_LZ;
        if (blockTypes.equals(i)) {
            return blockTypes;
        }
        BlockTypes blockTypes2 = BLOCK_PPM;
        if (blockTypes2.equals(i)) {
            return blockTypes2;
        }
        return null;
    }

    public int getBlockType() {
        return this.blockType;
    }

    public boolean equals(int i) {
        return this.blockType == i;
    }
}
