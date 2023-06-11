package ir.mahdi.mzip.rar.rarfile;

import kotlinx.coroutines.scheduling.WorkQueueKt;

public class FileNameDecoder {
    public static int getChar(byte[] bArr, int i) {
        return bArr[i] & -1;
    }

    public static String decode(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = getChar(bArr, i);
        StringBuffer stringBuffer = new StringBuffer();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i2 < bArr.length) {
            if (i4 == 0) {
                i5 = getChar(bArr, i2);
                i2++;
                i4 = 8;
            }
            int i8 = i5 >> 6;
            if (i8 == 0) {
                i7 = i2 + 1;
                stringBuffer.append((char) getChar(bArr, i2));
            } else if (i8 != 1) {
                if (i8 == 2) {
                    stringBuffer.append((char) ((getChar(bArr, i2 + 1) << 8) + getChar(bArr, i2)));
                    i6++;
                } else if (i8 == 3) {
                    i7 = i2 + 1;
                    int i9 = getChar(bArr, i2);
                    if ((i9 & 128) != 0) {
                        int i10 = getChar(bArr, i7);
                        int i11 = (i9 & WorkQueueKt.MASK) + 2;
                        while (i11 > 0 && i6 < bArr.length) {
                            stringBuffer.append((char) ((i3 << 8) + ((getChar(bArr, i6) + i10) & 255)));
                            i11--;
                            i6++;
                        }
                    } else {
                        int i12 = i9 + 2;
                        while (i12 > 0 && i6 < bArr.length) {
                            stringBuffer.append((char) getChar(bArr, i6));
                            i12--;
                            i6++;
                        }
                    }
                }
                i5 = (i5 << 2) & 255;
                i4 -= 2;
            } else {
                i7 = i2 + 1;
                stringBuffer.append((char) (getChar(bArr, i2) + (i3 << 8)));
            }
            i6++;
            i5 = (i5 << 2) & 255;
            i4 -= 2;
            i2 = i7;
        }
        return stringBuffer.toString();
    }
}
