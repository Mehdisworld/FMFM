package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BaseBlock {
    public static final short BaseBlockSize = 7;
    public static final short EARC_DATACRC = 2;
    public static final short EARC_NEXT_VOLUME = 1;
    public static final short EARC_REVSPACE = 4;
    public static final short EARC_VOLNUMBER = 8;
    public static final short LHD_COMMENT = 8;
    public static final short LHD_DIRECTORY = 224;
    public static final short LHD_EXTFLAGS = 8192;
    public static final short LHD_EXTTIME = 4096;
    public static final short LHD_LARGE = 256;
    public static final short LHD_PASSWORD = 4;
    public static final short LHD_SALT = 1024;
    public static final short LHD_SOLID = 16;
    public static final short LHD_SPLIT_AFTER = 2;
    public static final short LHD_SPLIT_BEFORE = 1;
    public static final short LHD_UNICODE = 512;
    public static final short LHD_VERSION = 2048;
    public static final short LHD_WINDOW1024 = 128;
    public static final short LHD_WINDOW128 = 32;
    public static final short LHD_WINDOW2048 = 160;
    public static final short LHD_WINDOW256 = 64;
    public static final short LHD_WINDOW4096 = 192;
    public static final short LHD_WINDOW512 = 96;
    public static final short LHD_WINDOW64 = 0;
    public static final short LHD_WINDOWMASK = 224;
    public static final short LONG_BLOCK = Short.MIN_VALUE;
    public static final short MHD_AV = 32;
    public static final short MHD_COMMENT = 2;
    public static final short MHD_ENCRYPTVER = 512;
    public static final short MHD_FIRSTVOLUME = 256;
    public static final short MHD_LOCK = 4;
    public static final short MHD_NEWNUMBERING = 16;
    public static final short MHD_PACK_COMMENT = 16;
    public static final short MHD_PASSWORD = 128;
    public static final short MHD_PROTECT = 64;
    public static final short MHD_SOLID = 8;
    public static final short MHD_VOLUME = 1;
    public static final short SKIP_IF_UNKNOWN = 16384;
    protected short flags = 0;
    protected short headCRC = 0;
    protected short headerSize = 0;
    protected byte headerType = 0;
    protected long positionInFile;

    public BaseBlock() {
    }

    public BaseBlock(BaseBlock baseBlock) {
        this.flags = baseBlock.getFlags();
        this.headCRC = baseBlock.getHeadCRC();
        this.headerType = baseBlock.getHeaderType().getHeaderByte();
        this.headerSize = baseBlock.getHeaderSize();
        this.positionInFile = baseBlock.getPositionInFile();
    }

    public BaseBlock(byte[] bArr) {
        this.headCRC = Raw.readShortLittleEndian(bArr, 0);
        this.headerType = (byte) (this.headerType | (bArr[2] & -1));
        this.flags = Raw.readShortLittleEndian(bArr, 3);
        this.headerSize = Raw.readShortLittleEndian(bArr, 5);
    }

    public boolean hasArchiveDataCRC() {
        return (this.flags & 2) != 0;
    }

    public boolean hasVolumeNumber() {
        return (this.flags & 8) != 0;
    }

    public boolean hasEncryptVersion() {
        return (this.flags & 512) != 0;
    }

    public boolean isSubBlock() {
        if (UnrarHeadertype.SubHeader.equals(this.headerType)) {
            return true;
        }
        if (!UnrarHeadertype.NewSubHeader.equals(this.headerType) || (this.flags & 16) == 0) {
            return false;
        }
        return true;
    }

    public long getPositionInFile() {
        return this.positionInFile;
    }

    public void setPositionInFile(long j) {
        this.positionInFile = j;
    }

    public short getFlags() {
        return this.flags;
    }

    public short getHeadCRC() {
        return this.headCRC;
    }

    public short getHeaderSize() {
        return this.headerSize;
    }

    public UnrarHeadertype getHeaderType() {
        return UnrarHeadertype.findType(this.headerType);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("HeaderType: ");
        sb.append(getHeaderType());
        Integer.toHexString(getHeadCRC());
        Integer.toHexString(getFlags());
        getHeaderSize();
        getPositionInFile();
    }
}
