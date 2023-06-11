package ir.mahdi.mzip.rar.unpack;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.vm.BitInput;
import java.io.IOException;
import java.util.Arrays;
import kotlinx.coroutines.scheduling.WorkQueueKt;

public abstract class Unpack15 extends BitInput {
    private static int[] DecHf0 = {32768, 49152, 57344, 61952, 61952, 61952, 61952, 61952, 65535};
    private static int[] DecHf1 = {8192, 49152, 57344, 61440, 61952, 61952, 63456, 65535};
    private static int[] DecHf2 = {4096, 9216, 32768, 49152, 64000, 65535, 65535, 65535};
    private static int[] DecHf3 = {2048, 9216, 60928, 65152, 65535, 65535, 65535};
    private static int[] DecHf4 = {MotionEventCompat.ACTION_POINTER_INDEX_MASK, 65535, 65535, 65535, 65535, 65535};
    private static int[] DecL1 = {32768, 40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 61952, 65535};
    private static int[] DecL2 = {40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 62016, 65535};
    private static int[] PosHf0 = {0, 0, 0, 0, 0, 8, 16, 24, 33, 33, 33, 33, 33};
    private static int[] PosHf1 = {0, 0, 0, 0, 0, 0, 4, 44, 60, 76, 80, 80, WorkQueueKt.MASK};
    private static int[] PosHf2 = {0, 0, 0, 0, 0, 0, 2, 7, 53, 117, 233, 0, 0};
    private static int[] PosHf3 = {0, 0, 0, 0, 0, 0, 0, 2, 16, 218, 251, 0, 0};
    private static int[] PosHf4 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0};
    private static int[] PosL1 = {0, 0, 0, 2, 3, 5, 7, 11, 16, 20, 24, 32, 32};
    private static int[] PosL2 = {0, 0, 0, 0, 5, 7, 9, 13, 18, 22, 26, 34, 36};
    private static final int STARTHF0 = 4;
    private static final int STARTHF1 = 5;
    private static final int STARTHF2 = 5;
    private static final int STARTHF3 = 6;
    private static final int STARTHF4 = 8;
    private static final int STARTL1 = 2;
    private static final int STARTL2 = 3;
    static int[] ShortLen1 = {1, 3, 4, 4, 5, 6, 7, 8, 8, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortLen2 = {2, 3, 3, 3, 4, 4, 5, 6, 6, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortXor1 = {0, 160, 208, 224, 240, 248, 252, 254, 255, 192, 128, 144, 152, 156, 176};
    static int[] ShortXor2 = {0, 64, 96, 160, 208, 224, 240, 248, 252, 192, 128, 144, 152, 156, 176};
    protected int AvrLn1;
    protected int AvrLn2;
    protected int AvrLn3;
    protected int AvrPlc;
    protected int AvrPlcB;
    protected int Buf60;
    protected int[] ChSet = new int[256];
    protected int[] ChSetA = new int[256];
    protected int[] ChSetB = new int[256];
    protected int[] ChSetC = new int[256];
    protected int FlagBuf;
    protected int FlagsCnt;
    protected int LCount;
    protected int MaxDist3;
    protected int[] NToPl = new int[256];
    protected int[] NToPlB = new int[256];
    protected int[] NToPlC = new int[256];
    protected int Nhfb;
    protected int Nlzb;
    protected int NumHuf;
    protected int[] Place = new int[256];
    protected int[] PlaceA = new int[256];
    protected int[] PlaceB = new int[256];
    protected int[] PlaceC = new int[256];
    protected int StMode;
    protected long destUnpSize;
    protected int lastDist;
    protected int lastLength;
    protected int[] oldDist = new int[4];
    protected int oldDistPtr;
    protected int readBorder;
    protected int readTop;
    protected boolean suspended;
    protected boolean unpAllBuf;
    protected ComprDataIO unpIO;
    protected int unpPtr;
    protected boolean unpSomeRead;
    protected byte[] window;
    protected int wrPtr;

    public abstract void unpInitData(boolean z);

    public void unpack15(boolean z) throws IOException, RarException {
        if (this.suspended) {
            this.unpPtr = this.wrPtr;
        } else {
            unpInitData(z);
            oldUnpInitData(z);
            unpReadBuf();
            if (!z) {
                initHuff();
                this.unpPtr = 0;
            } else {
                this.unpPtr = this.wrPtr;
            }
            this.destUnpSize--;
        }
        if (this.destUnpSize >= 0) {
            getFlagsBuf();
            this.FlagsCnt = 8;
        }
        while (this.destUnpSize >= 0) {
            this.unpPtr &= Compress.MAXWINMASK;
            if (this.inAddr > this.readTop - 30 && !unpReadBuf()) {
                break;
            }
            int i = this.wrPtr;
            int i2 = this.unpPtr;
            if ((4194303 & (i - i2)) < 270 && i != i2) {
                oldUnpWriteBuf();
                if (this.suspended) {
                    return;
                }
            }
            if (this.StMode != 0) {
                huffDecode();
            } else {
                int i3 = this.FlagsCnt - 1;
                this.FlagsCnt = i3;
                if (i3 < 0) {
                    getFlagsBuf();
                    this.FlagsCnt = 7;
                }
                int i4 = this.FlagBuf;
                if ((i4 & 128) != 0) {
                    this.FlagBuf = i4 << 1;
                    if (this.Nlzb > this.Nhfb) {
                        longLZ();
                    } else {
                        huffDecode();
                    }
                } else {
                    this.FlagBuf = i4 << 1;
                    int i5 = this.FlagsCnt - 1;
                    this.FlagsCnt = i5;
                    if (i5 < 0) {
                        getFlagsBuf();
                        this.FlagsCnt = 7;
                    }
                    int i6 = this.FlagBuf;
                    if ((i6 & 128) != 0) {
                        this.FlagBuf = i6 << 1;
                        if (this.Nlzb > this.Nhfb) {
                            huffDecode();
                        } else {
                            longLZ();
                        }
                    } else {
                        this.FlagBuf = i6 << 1;
                        shortLZ();
                    }
                }
            }
        }
        oldUnpWriteBuf();
    }

    public boolean unpReadBuf() throws IOException, RarException {
        int i = this.readTop - this.inAddr;
        if (i < 0) {
            return false;
        }
        if (this.inAddr > 16384) {
            if (i > 0) {
                System.arraycopy(this.inBuf, this.inAddr, this.inBuf, 0, i);
            }
            this.inAddr = 0;
            this.readTop = i;
        } else {
            i = this.readTop;
        }
        int unpRead = this.unpIO.unpRead(this.inBuf, i, (32768 - i) & -16);
        if (unpRead > 0) {
            this.readTop += unpRead;
        }
        this.readBorder = this.readTop - 30;
        if (unpRead != -1) {
            return true;
        }
        return false;
    }

    private int getShortLen1(int i) {
        return i == 1 ? this.Buf60 + 3 : ShortLen1[i];
    }

    private int getShortLen2(int i) {
        return i == 3 ? this.Buf60 + 3 : ShortLen2[i];
    }

    public void shortLZ() {
        int i = 0;
        this.NumHuf = 0;
        int fgetbits = fgetbits();
        if (this.LCount == 2) {
            faddbits(1);
            if (fgetbits >= 32768) {
                oldCopyString(this.lastDist, this.lastLength);
                return;
            } else {
                fgetbits <<= 1;
                this.LCount = 0;
            }
        }
        int i2 = fgetbits >>> 8;
        if (this.AvrLn1 < 37) {
            i = 0;
            while (((ShortXor1[i] ^ i2) & ((255 >>> getShortLen1(i)) ^ -1)) != 0) {
                i++;
            }
            faddbits(getShortLen1(i));
        } else {
            int i3 = 0;
            while (((ShortXor2[i] ^ i2) & ((255 >> getShortLen2(i)) ^ -1)) != 0) {
                i3 = i + 1;
            }
            faddbits(getShortLen2(i));
        }
        if (i < 9) {
            this.LCount = 0;
            int i4 = this.AvrLn1 + i;
            this.AvrLn1 = i4;
            this.AvrLn1 = i4 - (i4 >> 4);
            int decodeNum = decodeNum(fgetbits(), 5, DecHf2, PosHf2) & 255;
            int[] iArr = this.ChSetA;
            int i5 = iArr[decodeNum];
            int i6 = decodeNum - 1;
            if (i6 != -1) {
                int[] iArr2 = this.PlaceA;
                iArr2[i5] = iArr2[i5] - 1;
                int i7 = iArr[i6];
                iArr2[i7] = iArr2[i7] + 1;
                iArr[i6 + 1] = i7;
                iArr[i6] = i5;
            }
            int i8 = i + 2;
            int[] iArr3 = this.oldDist;
            int i9 = this.oldDistPtr;
            int i10 = i9 + 1;
            this.oldDistPtr = i10;
            int i11 = i5 + 1;
            iArr3[i9] = i11;
            this.oldDistPtr = i10 & 3;
            this.lastLength = i8;
            this.lastDist = i11;
            oldCopyString(i11, i8);
        } else if (i == 9) {
            this.LCount++;
            oldCopyString(this.lastDist, this.lastLength);
        } else if (i == 14) {
            this.LCount = 0;
            int decodeNum2 = decodeNum(fgetbits(), 3, DecL2, PosL2) + 5;
            int fgetbits2 = (fgetbits() >> 1) | 32768;
            faddbits(15);
            this.lastLength = decodeNum2;
            this.lastDist = fgetbits2;
            oldCopyString(fgetbits2, decodeNum2);
        } else {
            this.LCount = 0;
            int i12 = this.oldDist[(this.oldDistPtr - (i - 9)) & 3];
            int decodeNum3 = decodeNum(fgetbits(), 2, DecL1, PosL1) + 2;
            if (decodeNum3 == 257 && i == 10) {
                this.Buf60 ^= 1;
                return;
            }
            if (i12 > 256) {
                decodeNum3++;
            }
            if (i12 >= this.MaxDist3) {
                decodeNum3++;
            }
            int[] iArr4 = this.oldDist;
            int i13 = this.oldDistPtr;
            int i14 = i13 + 1;
            this.oldDistPtr = i14;
            iArr4[i13] = i12;
            this.oldDistPtr = i14 & 3;
            this.lastLength = decodeNum3;
            this.lastDist = i12;
            oldCopyString(i12, decodeNum3);
        }
    }

    public void longLZ() {
        int i;
        int[] iArr;
        int i2;
        int i3;
        int i4 = 0;
        this.NumHuf = 0;
        int i5 = this.Nlzb + 16;
        this.Nlzb = i5;
        if (i5 > 255) {
            this.Nlzb = 144;
            this.Nhfb >>>= 1;
        }
        int i6 = this.AvrLn2;
        int fgetbits = fgetbits();
        int i7 = this.AvrLn2;
        if (i7 >= 122) {
            fgetbits = decodeNum(fgetbits, 3, DecL2, PosL2);
        } else if (i7 >= 64) {
            fgetbits = decodeNum(fgetbits, 2, DecL1, PosL1);
        } else if (fgetbits < 256) {
            faddbits(16);
        } else {
            while (((fgetbits << i4) & 32768) == 0) {
                i4++;
            }
            faddbits(i4 + 1);
            fgetbits = i4;
        }
        int i8 = this.AvrLn2 + fgetbits;
        this.AvrLn2 = i8;
        this.AvrLn2 = i8 - (i8 >>> 5);
        int fgetbits2 = fgetbits();
        int i9 = this.AvrPlcB;
        if (i9 > 10495) {
            i = decodeNum(fgetbits2, 5, DecHf2, PosHf2);
        } else if (i9 > 1791) {
            i = decodeNum(fgetbits2, 5, DecHf1, PosHf1);
        } else {
            i = decodeNum(fgetbits2, 4, DecHf0, PosHf0);
        }
        int i10 = this.AvrPlcB + i;
        this.AvrPlcB = i10;
        this.AvrPlcB = i10 - (i10 >> 8);
        while (true) {
            iArr = this.ChSetB;
            int i11 = iArr[i & 255];
            int[] iArr2 = this.NToPlB;
            i2 = i11 + 1;
            int i12 = i11 & 255;
            i3 = iArr2[i12];
            iArr2[i12] = i3 + 1;
            if ((i2 & 255) != 0) {
                break;
            }
            corrHuff(iArr, iArr2);
        }
        iArr[i] = iArr[i3];
        iArr[i3] = i2;
        int fgetbits3 = ((65280 & i2) | (fgetbits() >>> 8)) >>> 1;
        faddbits(7);
        int i13 = this.AvrLn3;
        if (!(fgetbits == 1 || fgetbits == 4)) {
            if (fgetbits == 0 && fgetbits3 <= this.MaxDist3) {
                int i14 = i13 + 1;
                this.AvrLn3 = i14;
                this.AvrLn3 = i14 - (i14 >> 8);
            } else if (i13 > 0) {
                this.AvrLn3 = i13 - 1;
            }
        }
        int i15 = fgetbits + 3;
        if (fgetbits3 >= this.MaxDist3) {
            i15++;
        }
        if (fgetbits3 <= 256) {
            i15 += 8;
        }
        if (i13 > 176 || (this.AvrPlc >= 10752 && i6 < 64)) {
            this.MaxDist3 = 32512;
        } else {
            this.MaxDist3 = 8193;
        }
        int[] iArr3 = this.oldDist;
        int i16 = this.oldDistPtr;
        int i17 = i16 + 1;
        this.oldDistPtr = i17;
        iArr3[i16] = fgetbits3;
        this.oldDistPtr = i17 & 3;
        this.lastLength = i15;
        this.lastDist = fgetbits3;
        oldCopyString(fgetbits3, i15);
    }

    public void huffDecode() {
        int i;
        int fgetbits = fgetbits();
        int i2 = this.AvrPlc;
        int i3 = 4;
        if (i2 > 30207) {
            i = decodeNum(fgetbits, 8, DecHf4, PosHf4);
        } else if (i2 > 24063) {
            i = decodeNum(fgetbits, 6, DecHf3, PosHf3);
        } else if (i2 > 13823) {
            i = decodeNum(fgetbits, 5, DecHf2, PosHf2);
        } else if (i2 > 3583) {
            i = decodeNum(fgetbits, 5, DecHf1, PosHf1);
        } else {
            i = decodeNum(fgetbits, 4, DecHf0, PosHf0);
        }
        int i4 = i & 255;
        if (this.StMode != 0) {
            if (i4 == 0 && fgetbits > 4095) {
                i4 = 256;
            }
            i4--;
            if (i4 == -1) {
                int fgetbits2 = fgetbits();
                faddbits(1);
                if ((32768 & fgetbits2) != 0) {
                    this.StMode = 0;
                    this.NumHuf = 0;
                    return;
                }
                if ((fgetbits2 & 16384) == 0) {
                    i3 = 3;
                }
                faddbits(1);
                faddbits(5);
                oldCopyString((decodeNum(fgetbits(), 5, DecHf2, PosHf2) << 5) | (fgetbits() >>> 11), i3);
                return;
            }
        } else {
            int i5 = this.NumHuf;
            this.NumHuf = i5 + 1;
            if (i5 >= 16 && this.FlagsCnt == 0) {
                this.StMode = 1;
            }
        }
        int i6 = this.AvrPlc + i4;
        this.AvrPlc = i6;
        this.AvrPlc = i6 - (i6 >>> 8);
        int i7 = this.Nhfb + 16;
        this.Nhfb = i7;
        if (i7 > 255) {
            this.Nhfb = 144;
            this.Nlzb >>>= 1;
        }
        byte[] bArr = this.window;
        int i8 = this.unpPtr;
        this.unpPtr = i8 + 1;
        bArr[i8] = (byte) (this.ChSet[i4] >>> 8);
        this.destUnpSize--;
        while (true) {
            int[] iArr = this.ChSet;
            int i9 = iArr[i4];
            int[] iArr2 = this.NToPl;
            int i10 = i9 + 1;
            int i11 = i9 & 255;
            int i12 = iArr2[i11];
            iArr2[i11] = i12 + 1;
            if ((i10 & 255) > 161) {
                corrHuff(iArr, iArr2);
            } else {
                iArr[i4] = iArr[i12];
                iArr[i12] = i10;
                return;
            }
        }
    }

    public void getFlagsBuf() {
        int decodeNum = decodeNum(fgetbits(), 5, DecHf2, PosHf2);
        while (true) {
            int[] iArr = this.ChSetC;
            int i = iArr[decodeNum];
            this.FlagBuf = i >>> 8;
            int[] iArr2 = this.NToPlC;
            int i2 = i + 1;
            int i3 = i & 255;
            int i4 = iArr2[i3];
            iArr2[i3] = i4 + 1;
            if ((i2 & 255) != 0) {
                iArr[decodeNum] = iArr[i4];
                iArr[i4] = i2;
                return;
            }
            corrHuff(iArr, iArr2);
        }
    }

    public void oldUnpInitData(boolean z) {
        if (!z) {
            this.Buf60 = 0;
            this.NumHuf = 0;
            this.AvrLn3 = 0;
            this.AvrLn2 = 0;
            this.AvrLn1 = 0;
            this.AvrPlcB = 0;
            this.AvrPlc = 13568;
            this.MaxDist3 = 8193;
            this.Nlzb = 128;
            this.Nhfb = 128;
        }
        this.FlagsCnt = 0;
        this.FlagBuf = 0;
        this.StMode = 0;
        this.LCount = 0;
        this.readTop = 0;
    }

    public void initHuff() {
        for (int i = 0; i < 256; i++) {
            int[] iArr = this.Place;
            int[] iArr2 = this.PlaceA;
            this.PlaceB[i] = i;
            iArr2[i] = i;
            iArr[i] = i;
            int i2 = ((i ^ -1) + 1) & 255;
            this.PlaceC[i] = i2;
            int[] iArr3 = this.ChSet;
            int i3 = i << 8;
            this.ChSetB[i] = i3;
            iArr3[i] = i3;
            this.ChSetA[i] = i;
            this.ChSetC[i] = i2 << 8;
        }
        Arrays.fill(this.NToPl, 0);
        Arrays.fill(this.NToPlB, 0);
        Arrays.fill(this.NToPlC, 0);
        corrHuff(this.ChSetB, this.NToPlB);
    }

    public void corrHuff(int[] iArr, int[] iArr2) {
        int i = 0;
        for (int i2 = 7; i2 >= 0; i2--) {
            int i3 = 0;
            while (i3 < 32) {
                iArr[i] = (iArr[i] & InputDeviceCompat.SOURCE_ANY) | i2;
                i3++;
                i++;
            }
        }
        Arrays.fill(iArr2, 0);
        for (int i4 = 6; i4 >= 0; i4--) {
            iArr2[i4] = (7 - i4) * 32;
        }
    }

    public void oldCopyString(int i, int i2) {
        this.destUnpSize -= (long) i2;
        while (true) {
            int i3 = i2 - 1;
            if (i2 != 0) {
                byte[] bArr = this.window;
                int i4 = this.unpPtr;
                bArr[i4] = bArr[(i4 - i) & Compress.MAXWINMASK];
                this.unpPtr = (i4 + 1) & Compress.MAXWINMASK;
                i2 = i3;
            } else {
                return;
            }
        }
    }

    public int decodeNum(int i, int i2, int[] iArr, int[] iArr2) {
        int i3 = i & 65520;
        int i4 = 0;
        int i5 = 0;
        while (iArr[i5] <= i3) {
            i2++;
            i5++;
        }
        faddbits(i2);
        if (i5 != 0) {
            i4 = iArr[i5 - 1];
        }
        return ((i3 - i4) >>> (16 - i2)) + iArr2[i2];
    }

    public void oldUnpWriteBuf() throws IOException {
        int i = this.unpPtr;
        int i2 = this.wrPtr;
        if (i != i2) {
            this.unpSomeRead = true;
        }
        if (i < i2) {
            this.unpIO.unpWrite(this.window, i2, (-i2) & Compress.MAXWINMASK);
            this.unpIO.unpWrite(this.window, 0, this.unpPtr);
            this.unpAllBuf = true;
        } else {
            this.unpIO.unpWrite(this.window, i2, i - i2);
        }
        this.wrPtr = this.unpPtr;
    }
}
