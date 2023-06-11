package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class EndArcHeader extends BaseBlock {
    private static final short EARC_DATACRC = 2;
    private static final short EARC_NEXT_VOLUME = 1;
    private static final short EARC_REVSPACE = 4;
    private static final short EARC_VOLNUMBER = 8;
    public static final short endArcArchiveDataCrcSize = 4;
    private static final short endArcHeaderSize = 6;
    public static final short endArcVolumeNumberSize = 2;
    private int archiveDataCRC;
    private short volumeNumber;

    public EndArcHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        int i = 0;
        if (hasArchiveDataCRC()) {
            this.archiveDataCRC = Raw.readIntLittleEndian(bArr, 0);
            i = 4;
        }
        if (hasVolumeNumber()) {
            this.volumeNumber = Raw.readShortLittleEndian(bArr, i);
        }
    }

    public int getArchiveDataCRC() {
        return this.archiveDataCRC;
    }

    public short getVolumeNumber() {
        return this.volumeNumber;
    }
}
