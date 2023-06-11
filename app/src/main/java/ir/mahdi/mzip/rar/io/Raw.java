package ir.mahdi.mzip.rar.io;

import ir.mahdi.mzip.rar.unpack.vm.VMCmdFlags;
import kotlin.UByte;

public class Raw {
    public static final short readShortBigEndian(byte[] bArr, int i) {
        return (short) (((short) (((short) ((bArr[i] & -1) | 0)) << 8)) | (bArr[i + 1] & -1));
    }

    public static final int readIntBigEndian(byte[] bArr, int i) {
        return (((bArr[i + 2] & -1) | (((((bArr[i] & -1) | 0) << 8) | (bArr[i + 1] & -1)) << 8)) << 8) | (bArr[i + 3] & -1);
    }

    public static final long readLongBigEndian(byte[] bArr, int i) {
        return (long) ((((bArr[i + 6] & -1) | (((((((((((((bArr[i] & -1) | 0) << 8) | (bArr[i + 1] & -1)) << 8) | (bArr[i + 2] & -1)) << 8) | (bArr[i + 3] & -1)) << 8) | (bArr[i + 4] & -1)) << 8) | (bArr[i + 5] & -1)) << 8)) << 8) | (bArr[i + 7] & -1));
    }

    public static final short readShortLittleEndian(byte[] bArr, int i) {
        return (short) (((short) (((short) ((bArr[i + 1] & -1) + 0)) << 8)) + (bArr[i] & -1));
    }

    public static final int readIntLittleEndian(byte[] bArr, int i) {
        return ((bArr[i + 1] & -1) << 8) | (bArr[i] & -1) | ((bArr[i + 3] & -1) << 24) | ((bArr[i + 2] & -1) << VMCmdFlags.VMCF_PROC);
    }

    public static final long readIntLittleEndianAsLong(byte[] bArr, int i) {
        return ((((long) bArr[i + 1]) & 255) << 8) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 2]) & 255) << 16);
    }

    public static final long readLongLittleEndian(byte[] bArr, int i) {
        return (long) ((((bArr[i + 1] & -1) | (((((((((((((bArr[i + 7] & -1) | 0) << 8) | (bArr[i + 6] & -1)) << 8) | (bArr[i + 5] & -1)) << 8) | (bArr[i + 4] & -1)) << 8) | (bArr[i + 3] & -1)) << 8) | (bArr[i + 2] & -1)) << 8)) << 8) | bArr[i]);
    }

    public static final void writeShortBigEndian(byte[] bArr, int i, short s) {
        bArr[i] = (byte) (s >>> 8);
        bArr[i + 1] = (byte) (s & 255);
    }

    public static final void writeIntBigEndian(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) ((i2 >>> 24) & 255);
        bArr[i + 1] = (byte) ((i2 >>> 16) & 255);
        bArr[i + 2] = (byte) ((i2 >>> 8) & 255);
        bArr[i + 3] = (byte) (i2 & 255);
    }

    public static final void writeLongBigEndian(byte[] bArr, int i, long j) {
        bArr[i] = (byte) ((int) (j >>> 56));
        bArr[i + 1] = (byte) ((int) (j >>> 48));
        bArr[i + 2] = (byte) ((int) (j >>> 40));
        bArr[i + 3] = (byte) ((int) (j >>> 32));
        bArr[i + 4] = (byte) ((int) (j >>> 24));
        bArr[i + 5] = (byte) ((int) (j >>> 16));
        bArr[i + 6] = (byte) ((int) (j >>> 8));
        bArr[i + 7] = (byte) ((int) (j & 255));
    }

    public static final void writeShortLittleEndian(byte[] bArr, int i, short s) {
        bArr[i + 1] = (byte) (s >>> 8);
        bArr[i] = (byte) (s & 255);
    }

    public static final void incShortLittleEndian(byte[] bArr, int i, int i2) {
        byte b = (byte) (i2 & UByte.MAX_VALUE);
        int i3 = ((bArr[i] & -1) + b) >>> 8;
        bArr[i] = (byte) (bArr[i] + b);
        if (i3 > 0 || (65280 & i2) != 0) {
            int i4 = i + 1;
            bArr[i4] = (byte) (bArr[i4] + ((i2 >>> 8) & UByte.MAX_VALUE) + i3);
        }
    }

    public static final void writeIntLittleEndian(byte[] bArr, int i, int i2) {
        bArr[i + 3] = (byte) (i2 >>> 24);
        bArr[i + 2] = (byte) (i2 >>> 16);
        bArr[i + 1] = (byte) (i2 >>> 8);
        bArr[i] = (byte) (i2 & 255);
    }

    public static final void writeLongLittleEndian(byte[] bArr, int i, long j) {
        bArr[i + 7] = (byte) ((int) (j >>> 56));
        bArr[i + 6] = (byte) ((int) (j >>> 48));
        bArr[i + 5] = (byte) ((int) (j >>> 40));
        bArr[i + 4] = (byte) ((int) (j >>> 32));
        bArr[i + 3] = (byte) ((int) (j >>> 24));
        bArr[i + 2] = (byte) ((int) (j >>> 16));
        bArr[i + 1] = (byte) ((int) (j >>> 8));
        bArr[i] = (byte) ((int) (j & 255));
    }
}
