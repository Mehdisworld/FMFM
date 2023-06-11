package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MainHeader extends BaseBlock {
    public static final short mainHeaderSize = 6;
    public static final short mainHeaderSizeWithEnc = 7;
    private byte encryptVersion;
    private short highPosAv;
    private int posAv;

    public MainHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        this.highPosAv = Raw.readShortLittleEndian(bArr, 0);
        this.posAv = Raw.readIntLittleEndian(bArr, 2);
        if (hasEncryptVersion()) {
            this.encryptVersion = (byte) (this.encryptVersion | (bArr[6] & -1));
        }
    }

    public boolean hasArchCmt() {
        return (this.flags & 2) != 0;
    }

    public byte getEncryptVersion() {
        return this.encryptVersion;
    }

    public short getHighPosAv() {
        return this.highPosAv;
    }

    public int getPosAv() {
        return this.posAv;
    }

    public boolean isEncrypted() {
        return (this.flags & 128) != 0;
    }

    public boolean isMultiVolume() {
        return (this.flags & 1) != 0;
    }

    public boolean isFirstVolume() {
        return (this.flags & 256) != 0;
    }

    public void print() {
        super.print();
        getPosAv();
        getHighPosAv();
        StringBuilder sb = new StringBuilder();
        sb.append("\nhasencversion: ");
        sb.append(hasEncryptVersion());
        sb.append(hasEncryptVersion() ? Byte.valueOf(getEncryptVersion()) : "");
        hasArchCmt();
        isEncrypted();
        isMultiVolume();
        isFirstVolume();
        isSolid();
        isLocked();
        isProtected();
        isAV();
    }

    public boolean isSolid() {
        return (this.flags & 8) != 0;
    }

    public boolean isLocked() {
        return (this.flags & 4) != 0;
    }

    public boolean isProtected() {
        return (this.flags & 64) != 0;
    }

    public boolean isAV() {
        return (this.flags & 32) != 0;
    }

    public boolean isNewNumbering() {
        return (this.flags & 16) != 0;
    }
}
