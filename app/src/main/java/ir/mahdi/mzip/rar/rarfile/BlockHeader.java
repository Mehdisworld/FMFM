package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BlockHeader extends BaseBlock {
    public static final short blockHeaderSize = 4;
    private int dataSize;
    private int packSize;

    public BlockHeader() {
    }

    public BlockHeader(BlockHeader blockHeader) {
        super((BaseBlock) blockHeader);
        int dataSize2 = blockHeader.getDataSize();
        this.packSize = dataSize2;
        this.dataSize = dataSize2;
        this.positionInFile = blockHeader.getPositionInFile();
    }

    public BlockHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        int readIntLittleEndian = Raw.readIntLittleEndian(bArr, 0);
        this.packSize = readIntLittleEndian;
        this.dataSize = readIntLittleEndian;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public int getPackSize() {
        return this.packSize;
    }

    public void print() {
        super.print();
        getDataSize();
        getPackSize();
    }
}
