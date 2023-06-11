package ir.mahdi.mzip.rar.unpack.vm;

//import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.view.InputDeviceCompat;
import ir.mahdi.mzip.rar.crc.RarCRC;
import ir.mahdi.mzip.rar.io.Raw;
import java.util.List;
import java.util.Vector;

public class RarVM extends BitInput {
    private static final long UINT_MASK = -1;
    public static final int VM_FIXEDGLOBALSIZE = 64;
    public static final int VM_GLOBALMEMADDR = 245760;
    public static final int VM_GLOBALMEMSIZE = 8192;
    public static final int VM_MEMMASK = 262143;
    public static final int VM_MEMSIZE = 262144;
    private static final int regCount = 8;
    private int IP;
    private int[] R = new int[8];
    private int codeSize;
    private int flags;
    private int maxOpCount = 25000000;
    private byte[] mem = null;

    public static int ReadData(BitInput bitInput) {
        int fgetbits = bitInput.fgetbits();
        int i = 49152 & fgetbits;
        if (i == 0) {
            bitInput.faddbits(6);
            return (fgetbits >> 10) & 15;
        } else if (i != 16384) {
            if (i != 32768) {
                bitInput.faddbits(2);
                bitInput.faddbits(16);
                int fgetbits2 = (bitInput.fgetbits() << 16) | bitInput.fgetbits();
                bitInput.faddbits(16);
                return fgetbits2;
            }
            bitInput.faddbits(2);
            int fgetbits3 = bitInput.fgetbits();
            bitInput.faddbits(16);
            return fgetbits3;
        } else if ((fgetbits & 15360) == 0) {
            int i2 = ((fgetbits >> 2) & 255) | InputDeviceCompat.SOURCE_ANY;
            bitInput.faddbits(14);
            return i2;
        } else {
            int i3 = (fgetbits >> 6) & 255;
            bitInput.faddbits(10);
            return i3;
        }
    }

    public void init() {
        if (this.mem == null) {
            this.mem = new byte[262148];
        }
    }

    private boolean isVMMem(byte[] bArr) {
        return this.mem == bArr;
    }

    private int getValue(boolean z, byte[] bArr, int i) {
        if (z) {
            if (isVMMem(bArr)) {
                return bArr[i];
            }
            return bArr[i] & -1;
        } else if (isVMMem(bArr)) {
            return Raw.readIntLittleEndian(bArr, i);
        } else {
            return Raw.readIntBigEndian(bArr, i);
        }
    }

    private void setValue(boolean z, byte[] bArr, int i, int i2) {
        if (z) {
            if (isVMMem(bArr)) {
                bArr[i] = (byte) i2;
            } else {
                bArr[i] = (byte) ((bArr[i] & 0) | ((byte) (i2 & 255)));
            }
        } else if (isVMMem(bArr)) {
            Raw.writeIntLittleEndian(bArr, i, i2);
        } else {
            Raw.writeIntBigEndian(bArr, i, i2);
        }
    }

    public void setLowEndianValue(byte[] bArr, int i, int i2) {
        Raw.writeIntLittleEndian(bArr, i, i2);
    }

    public void setLowEndianValue(Vector<Byte> vector, int i, int i2) {
        vector.set(i + 0, Byte.valueOf((byte) (i2 & 255)));
        vector.set(i + 1, Byte.valueOf((byte) ((i2 >>> 8) & 255)));
        vector.set(i + 2, Byte.valueOf((byte) ((i2 >>> 16) & 255)));
        vector.set(i + 3, Byte.valueOf((byte) ((i2 >>> 24) & 255)));
    }

    private int getOperand(VMPreparedOperand vMPreparedOperand) {
        if (vMPreparedOperand.getType() == VMOpType.VM_OPREGMEM) {
            return Raw.readIntLittleEndian(this.mem, (vMPreparedOperand.getOffset() + vMPreparedOperand.getBase()) & VM_MEMMASK);
        }
        return Raw.readIntLittleEndian(this.mem, vMPreparedOperand.getOffset());
    }

    public void execute(VMPreparedProgram vMPreparedProgram) {
        List<VMPreparedCommand> list;
        for (int i = 0; i < vMPreparedProgram.getInitR().length; i++) {
            this.R[i] = vMPreparedProgram.getInitR()[i];
        }
        long min = (long) (Math.min(vMPreparedProgram.getGlobalData().size(), 8192) & -1);
        if (min != 0) {
            for (int i2 = 0; ((long) i2) < min; i2++) {
                this.mem[i2 + VM_GLOBALMEMADDR] = vMPreparedProgram.getGlobalData().get(i2).byteValue();
            }
        }
        long min2 = Math.min((long) vMPreparedProgram.getStaticData().size(), 8192 - min) & -1;
        if (min2 != 0) {
            for (int i3 = 0; ((long) i3) < min2; i3++) {
                this.mem[((int) min) + VM_GLOBALMEMADDR + i3] = vMPreparedProgram.getStaticData().get(i3).byteValue();
            }
        }
        this.R[7] = 262144;
        this.flags = 0;
        if (vMPreparedProgram.getAltCmd().size() != 0) {
            list = vMPreparedProgram.getAltCmd();
        } else {
            list = vMPreparedProgram.getCmd();
        }
        if (!ExecuteCode(list, vMPreparedProgram.getCmdCount())) {
            list.get(0).setOpCode(VMCommands.VM_RET);
        }
        int value = getValue(false, this.mem, 245792) & VM_MEMMASK;
        int value2 = 262143 & getValue(false, this.mem, 245788);
        if (value + value2 >= 262144) {
            value = 0;
            value2 = 0;
        }
        vMPreparedProgram.setFilteredDataOffset(value);
        vMPreparedProgram.setFilteredDataSize(value2);
        vMPreparedProgram.getGlobalData().clear();
        int min3 = Math.min(getValue(false, this.mem, 245808), 8128);
        if (min3 != 0) {
            int i4 = min3 + 64;
            vMPreparedProgram.getGlobalData().setSize(i4);
            for (int i5 = 0; i5 < i4; i5++) {
                vMPreparedProgram.getGlobalData().set(i5, Byte.valueOf(this.mem[i5 + VM_GLOBALMEMADDR]));
            }
        }
    }

    public byte[] getMem() {
        return this.mem;
    }

    private boolean setIP(int i) {
        if (i >= this.codeSize) {
            return true;
        }
        int i2 = this.maxOpCount - 1;
        this.maxOpCount = i2;
        if (i2 <= 0) {
            return false;
        }
        this.IP = i;
        return true;
    }

    private boolean ExecuteCode(List<VMPreparedCommand> list, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        this.maxOpCount = 25000000;
        this.codeSize = i;
        this.IP = 0;
        while (true) {
            VMPreparedCommand vMPreparedCommand = list.get(this.IP);
            int operand = getOperand(vMPreparedCommand.getOp1());
            int operand2 = getOperand(vMPreparedCommand.getOp2());
            switch (AnonymousClass1.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands[vMPreparedCommand.getOpCode().ordinal()]) {
                case 1:
                    i2 = 1;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, getValue(vMPreparedCommand.isByteMode(), this.mem, operand2));
                    break;
                case 2:
                    byte[] bArr = this.mem;
                    i2 = 1;
                    setValue(true, bArr, operand, getValue(true, bArr, operand2));
                    break;
                case 3:
                    byte[] bArr2 = this.mem;
                    setValue(false, bArr2, operand, getValue(false, bArr2, operand2));
                    break;
                case 4:
                    int value = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value2 = value - getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    if (value2 == 0) {
                        this.flags = VMFlags.VM_FZ.getFlag();
                        break;
                    } else {
                        if (value2 > value) {
                            i3 = 1;
                        } else {
                            i3 = (value2 & VMFlags.VM_FS.getFlag()) | 0;
                        }
                        this.flags = i3;
                        break;
                    }
                case 5:
                    int value3 = getValue(true, this.mem, operand);
                    int value4 = value3 - getValue(true, this.mem, operand2);
                    if (value4 == 0) {
                        this.flags = VMFlags.VM_FZ.getFlag();
                        break;
                    } else {
                        if (value4 > value3) {
                            i4 = 1;
                        } else {
                            i4 = (VMFlags.VM_FS.getFlag() & value4) | 0;
                        }
                        this.flags = i4;
                        break;
                    }
                case 6:
                    int value5 = getValue(false, this.mem, operand);
                    int value6 = value5 - getValue(false, this.mem, operand2);
                    if (value6 == 0) {
                        this.flags = VMFlags.VM_FZ.getFlag();
                        break;
                    } else {
                        if (value6 > value5) {
                            i5 = 1;
                        } else {
                            i5 = (VMFlags.VM_FS.getFlag() & value6) | 0;
                        }
                        this.flags = i5;
                        break;
                    }
                case 7:
                    int value7 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value8 = (int) ((((long) value7) + ((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand2))) & -1);
                    if (vMPreparedCommand.isByteMode()) {
                        value8 &= 255;
                        if (value8 < value7) {
                            i8 = 1;
                        } else {
                            if (value8 == 0) {
                                i9 = VMFlags.VM_FZ.getFlag();
                            } else {
                                i9 = (value8 & 128) != 0 ? VMFlags.VM_FS.getFlag() : 0;
                            }
                            i8 = i9 | 0;
                        }
                        this.flags = i8;
                    } else {
                        if (value8 < value7) {
                            i6 = 1;
                        } else {
                            if (value8 == 0) {
                                i7 = VMFlags.VM_FZ.getFlag();
                            } else {
                                i7 = VMFlags.VM_FS.getFlag() & value8;
                            }
                            i6 = i7 | 0;
                        }
                        this.flags = i6;
                    }
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value8);
                    break;
                case 8:
                    byte[] bArr3 = this.mem;
                    i2 = 1;
                    setValue(true, bArr3, operand, (int) (((long) getValue(true, bArr3, operand)) & (((long) getValue(true, this.mem, operand2)) - 1) & -1));
                    break;
                case 9:
                    byte[] bArr4 = this.mem;
                    setValue(false, bArr4, operand, (int) (((long) getValue(false, bArr4, operand)) & (((long) getValue(false, this.mem, operand2)) - 1) & -1));
                    break;
                case 10:
                    int value9 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value10 = (int) (((long) value9) & (-1 - ((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand2))) & -1);
                    this.flags = value10 == 0 ? VMFlags.VM_FZ.getFlag() : value10 > value9 ? 1 : (VMFlags.VM_FS.getFlag() & value10) | 0;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value10);
                    break;
                case 11:
                    byte[] bArr5 = this.mem;
                    i2 = 1;
                    setValue(true, bArr5, operand, (int) (((long) getValue(true, bArr5, operand)) & (-1 - ((long) getValue(true, this.mem, operand2))) & -1));
                    break;
                case 12:
                    byte[] bArr6 = this.mem;
                    setValue(false, bArr6, operand, (int) (((long) getValue(false, bArr6, operand)) & (-1 - ((long) getValue(false, this.mem, operand2))) & -1));
                    break;
                case 13:
                    if ((this.flags & VMFlags.VM_FZ.getFlag()) != 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 14:
                    if ((this.flags & VMFlags.VM_FZ.getFlag()) == 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 15:
                    int value11 = (int) (((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand)) & 0);
                    if (vMPreparedCommand.isByteMode()) {
                        value11 &= 255;
                    }
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value11);
                    if (value11 == 0) {
                        i10 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i10 = VMFlags.VM_FS.getFlag() & value11;
                    }
                    this.flags = i10;
                    break;
                case 16:
                    byte[] bArr7 = this.mem;
                    setValue(true, bArr7, operand, (int) (0 & ((long) getValue(true, bArr7, operand))));
                    break;
                case 17:
                    byte[] bArr8 = this.mem;
                    setValue(false, bArr8, operand, (int) (((long) getValue(false, bArr8, operand)) & 0));
                    break;
                case 18:
                    int value12 = (int) (((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand)) & -2);
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value12);
                    if (value12 == 0) {
                        i11 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i11 = VMFlags.VM_FS.getFlag() & value12;
                    }
                    this.flags = i11;
                    break;
                case 19:
                    byte[] bArr9 = this.mem;
                    setValue(true, bArr9, operand, (int) (((long) getValue(true, bArr9, operand)) & -2));
                    break;
                case 20:
                    byte[] bArr10 = this.mem;
                    setValue(false, bArr10, operand, (int) (((long) getValue(false, bArr10, operand)) & -2));
                    break;
                case 21:
                    setIP(getValue(false, this.mem, operand));
                    continue;
                case 22:
                    int value13 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2) ^ getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    if (value13 == 0) {
                        i12 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i12 = VMFlags.VM_FS.getFlag() & value13;
                    }
                    this.flags = i12;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value13);
                    break;
                case 23:
                    int value14 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2) & getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    if (value14 == 0) {
                        i13 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i13 = VMFlags.VM_FS.getFlag() & value14;
                    }
                    this.flags = i13;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value14);
                    break;
                case 24:
                    int value15 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2) | getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    if (value15 == 0) {
                        i14 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i14 = VMFlags.VM_FS.getFlag() & value15;
                    }
                    this.flags = i14;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value15);
                    break;
                case 25:
                    int value16 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand) & getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    if (value16 == 0) {
                        i15 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i15 = value16 & VMFlags.VM_FS.getFlag();
                    }
                    this.flags = i15;
                    break;
                case 26:
                    if ((this.flags & VMFlags.VM_FS.getFlag()) != 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 27:
                    if ((this.flags & VMFlags.VM_FS.getFlag()) == 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 28:
                    if ((this.flags & VMFlags.VM_FC.getFlag()) != 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 29:
                    if ((this.flags & (VMFlags.VM_FC.getFlag() | VMFlags.VM_FZ.getFlag())) != 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 30:
                    if ((this.flags & (VMFlags.VM_FC.getFlag() | VMFlags.VM_FZ.getFlag())) == 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 31:
                    if ((this.flags & VMFlags.VM_FC.getFlag()) == 0) {
                        setIP(getValue(false, this.mem, operand));
                        continue;
                    }
                    break;
                case 32:
                    int[] iArr = this.R;
                    iArr[7] = iArr[7] - 4;
                    byte[] bArr11 = this.mem;
                    setValue(false, bArr11, iArr[7] & VM_MEMMASK, getValue(false, bArr11, operand));
                    break;
                case 33:
                    byte[] bArr12 = this.mem;
                    setValue(false, bArr12, operand, getValue(false, bArr12, this.R[7] & VM_MEMMASK));
                    int[] iArr2 = this.R;
                    iArr2[7] = iArr2[7] + 4;
                    break;
                case 34:
                    int[] iArr3 = this.R;
                    iArr3[7] = iArr3[7] - 4;
                    setValue(false, this.mem, iArr3[7] & VM_MEMMASK, this.IP + 1);
                    setIP(getValue(false, this.mem, operand));
                    continue;
                case 35:
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, getValue(vMPreparedCommand.isByteMode(), this.mem, operand) ^ -1);
                    break;
                case 36:
                    int value17 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value18 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    int i24 = value17 << value18;
                    if (i24 == 0) {
                        i16 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i16 = VMFlags.VM_FS.getFlag() & i24;
                    }
                    this.flags = (((value17 << (value18 + -1)) & Integer.MIN_VALUE) != 0 ? VMFlags.VM_FC.getFlag() : 0) | i16;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, i24);
                    break;
                case 37:
                    int value19 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value20 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    int i25 = value19 >>> value20;
                    if (i25 == 0) {
                        i17 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i17 = VMFlags.VM_FS.getFlag() & i25;
                    }
                    this.flags = ((value19 >>> (value20 - 1)) & VMFlags.VM_FC.getFlag()) | i17;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, i25);
                    break;
                case 38:
                    int value21 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int value22 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    int i26 = value21 >> value22;
                    if (i26 == 0) {
                        i18 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i18 = VMFlags.VM_FS.getFlag() & i26;
                    }
                    this.flags = ((value21 >> (value22 - 1)) & VMFlags.VM_FC.getFlag()) | i18;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, i26);
                    break;
                case 39:
                    int i27 = -getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    if (i27 == 0) {
                        i19 = VMFlags.VM_FZ.getFlag();
                    } else {
                        i19 = VMFlags.VM_FC.getFlag() | (VMFlags.VM_FS.getFlag() & i27);
                    }
                    this.flags = i19;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, i27);
                    break;
                case 40:
                    byte[] bArr13 = this.mem;
                    setValue(true, bArr13, operand, -getValue(true, bArr13, operand));
                    break;
                case 41:
                    byte[] bArr14 = this.mem;
                    setValue(false, bArr14, operand, -getValue(false, bArr14, operand));
                    break;
                case 42:
                    int i28 = this.R[7] - 4;
                    int i29 = 0;
                    while (i29 < 8) {
                        setValue(false, this.mem, i28 & VM_MEMMASK, this.R[i29]);
                        i29++;
                        i28 -= 4;
                    }
                    int[] iArr4 = this.R;
                    iArr4[7] = iArr4[7] - 32;
                    break;
                case 43:
                    int i30 = this.R[7];
                    int i31 = 0;
                    while (i31 < 8) {
                        this.R[7 - i31] = getValue(false, this.mem, i30 & VM_MEMMASK);
                        i31++;
                        i30 += 4;
                    }
                    break;
                case 44:
                    int[] iArr5 = this.R;
                    iArr5[7] = iArr5[7] - 4;
                    setValue(false, this.mem, iArr5[7] & VM_MEMMASK, this.flags);
                    break;
                case 45:
                    this.flags = getValue(false, this.mem, this.R[7] & VM_MEMMASK);
                    int[] iArr6 = this.R;
                    iArr6[7] = iArr6[7] + 4;
                    break;
                case 46:
                    byte[] bArr15 = this.mem;
                    setValue(false, bArr15, operand, getValue(true, bArr15, operand2));
                    break;
                case 47:
                    byte[] bArr16 = this.mem;
                    i2 = 1;
                    setValue(false, bArr16, operand, (byte) getValue(true, bArr16, operand2));
                    break;
                case 48:
                    int value23 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, getValue(vMPreparedCommand.isByteMode(), this.mem, operand2));
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand2, value23);
                    break;
                case 49:
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, (int) (((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand)) & (((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand2)) * -1) & -1 & -1));
                    break;
                case 50:
                    int value24 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand2);
                    if (value24 != 0) {
                        setValue(vMPreparedCommand.isByteMode(), this.mem, operand, getValue(vMPreparedCommand.isByteMode(), this.mem, operand) / value24);
                        break;
                    }
                    break;
                case 51:
                    int value25 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int flag = this.flags & VMFlags.VM_FC.getFlag();
                    int value26 = (int) (((long) value25) & (((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand2)) - 1) & (((long) flag) - 1) & -1);
                    if (vMPreparedCommand.isByteMode()) {
                        value26 &= 255;
                    }
                    if (value26 < value25 || (value26 == value25 && flag != 0)) {
                        i20 = 1;
                    } else {
                        if (value26 == 0) {
                            i21 = VMFlags.VM_FZ.getFlag();
                        } else {
                            i21 = VMFlags.VM_FS.getFlag() & value26;
                        }
                        i20 = i21 | 0;
                    }
                    this.flags = i20;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value26);
                    break;
                case 52:
                    int value27 = getValue(vMPreparedCommand.isByteMode(), this.mem, operand);
                    int flag2 = this.flags & VMFlags.VM_FC.getFlag();
                    int value28 = (int) (((long) value27) & (-1 - ((long) getValue(vMPreparedCommand.isByteMode(), this.mem, operand2))) & (-1 - ((long) flag2)) & -1);
                    if (vMPreparedCommand.isByteMode()) {
                        value28 &= 255;
                    }
                    if (value28 > value27 || (value28 == value27 && flag2 != 0)) {
                        i22 = 1;
                    } else {
                        if (value28 == 0) {
                            i23 = VMFlags.VM_FZ.getFlag();
                        } else {
                            i23 = VMFlags.VM_FS.getFlag() & value28;
                        }
                        i22 = i23 | 0;
                    }
                    this.flags = i22;
                    setValue(vMPreparedCommand.isByteMode(), this.mem, operand, value28);
                    break;
                case 53:
                    int[] iArr7 = this.R;
                    if (iArr7[7] >= 262144) {
                        return true;
                    }
                    setIP(getValue(false, this.mem, iArr7[7] & VM_MEMMASK));
                    int[] iArr8 = this.R;
                    iArr8[7] = iArr8[7] + 4;
                    continue;
                case 54:
                    ExecuteStandardFilter(VMStandardFilters.findFilter(vMPreparedCommand.getOp1().getData()));
                    break;
            }
            i2 = 1;
            this.IP += i2;
            this.maxOpCount -= i2;
        }
    }

    public void prepare(byte[] bArr, int i, VMPreparedProgram vMPreparedProgram) {
        int i2;
        InitBitInput();
        int min = Math.min(32768, i);
        for (int i3 = 0; i3 < min; i3++) {
            byte[] bArr2 = this.inBuf;
            bArr2[i3] = (byte) (bArr2[i3] | bArr[i3]);
        }
        byte b = 0;
        for (int i4 = 1; i4 < i; i4++) {
            b = (byte) (b ^ bArr[i4]);
        }
        faddbits(8);
        vMPreparedProgram.setCmdCount(0);
        if (b == bArr[0]) {
            VMStandardFilters IsStandardFilter = IsStandardFilter(bArr, i);
            if (IsStandardFilter != VMStandardFilters.VMSF_NONE) {
                VMPreparedCommand vMPreparedCommand = new VMPreparedCommand();
                vMPreparedCommand.setOpCode(VMCommands.VM_STANDARD);
                vMPreparedCommand.getOp1().setData(IsStandardFilter.getFilter());
                vMPreparedCommand.getOp1().setType(VMOpType.VM_OPNONE);
                vMPreparedCommand.getOp2().setType(VMOpType.VM_OPNONE);
                vMPreparedProgram.getCmd().add(vMPreparedCommand);
                vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
                i = 0;
            }
            int fgetbits = fgetbits();
            faddbits(1);
            if ((fgetbits & 32768) != 0) {
                long ReadData = ((long) ReadData(this)) & 0;
                int i5 = 0;
                while (this.inAddr < i && ((long) i5) < ReadData) {
                    vMPreparedProgram.getStaticData().add(Byte.valueOf((byte) (fgetbits() >> 8)));
                    faddbits(8);
                    i5++;
                }
            }
            while (this.inAddr < i) {
                VMPreparedCommand vMPreparedCommand2 = new VMPreparedCommand();
                int fgetbits2 = fgetbits();
                if ((fgetbits2 & 32768) == 0) {
                    vMPreparedCommand2.setOpCode(VMCommands.findVMCommand(fgetbits2 >> 12));
                    faddbits(4);
                } else {
                    vMPreparedCommand2.setOpCode(VMCommands.findVMCommand((fgetbits2 >> 10) - 24));
                    faddbits(6);
                }
                if ((VMCmdFlags.VM_CmdFlags[vMPreparedCommand2.getOpCode().getVMCommand()] & 4) != 0) {
                    vMPreparedCommand2.setByteMode((fgetbits() >> 15) == 1);
                    faddbits(1);
                } else {
                    vMPreparedCommand2.setByteMode(false);
                }
                vMPreparedCommand2.getOp1().setType(VMOpType.VM_OPNONE);
                vMPreparedCommand2.getOp2().setType(VMOpType.VM_OPNONE);
                byte b2 = (byte) (VMCmdFlags.VM_CmdFlags[vMPreparedCommand2.getOpCode().getVMCommand()] & 3);
                if (b2 > 0) {
                    decodeArg(vMPreparedCommand2.getOp1(), vMPreparedCommand2.isByteMode());
                    if (b2 == 2) {
                        decodeArg(vMPreparedCommand2.getOp2(), vMPreparedCommand2.isByteMode());
                    } else if (vMPreparedCommand2.getOp1().getType() == VMOpType.VM_OPINT && (VMCmdFlags.VM_CmdFlags[vMPreparedCommand2.getOpCode().getVMCommand()] & 24) != 0) {
                        int data = vMPreparedCommand2.getOp1().getData();
                        if (data >= 256) {
                            i2 = data + InputDeviceCompat.SOURCE_ANY;
                        } else {
                            if (data >= 136) {
                                data -= 264;
                            } else if (data >= 16) {
                                data -= 8;
                            } else if (data >= 8) {
                                data -= 16;
                            }
                            i2 = data + vMPreparedProgram.getCmdCount();
                        }
                        vMPreparedCommand2.getOp1().setData(i2);
                    }
                }
                vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
                vMPreparedProgram.getCmd().add(vMPreparedCommand2);
            }
        }
        VMPreparedCommand vMPreparedCommand3 = new VMPreparedCommand();
        vMPreparedCommand3.setOpCode(VMCommands.VM_RET);
        vMPreparedCommand3.getOp1().setType(VMOpType.VM_OPNONE);
        vMPreparedCommand3.getOp2().setType(VMOpType.VM_OPNONE);
        vMPreparedProgram.getCmd().add(vMPreparedCommand3);
        vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
        if (i != 0) {
            optimize(vMPreparedProgram);
        }
    }

    private void decodeArg(VMPreparedOperand vMPreparedOperand, boolean z) {
        int fgetbits = fgetbits();
        if ((32768 & fgetbits) != 0) {
            vMPreparedOperand.setType(VMOpType.VM_OPREG);
            vMPreparedOperand.setData((fgetbits >> 12) & 7);
            vMPreparedOperand.setOffset(vMPreparedOperand.getData());
            faddbits(4);
        } else if ((49152 & fgetbits) == 0) {
            vMPreparedOperand.setType(VMOpType.VM_OPINT);
            if (z) {
                vMPreparedOperand.setData((fgetbits >> 6) & 255);
                faddbits(10);
                return;
            }
            faddbits(2);
            vMPreparedOperand.setData(ReadData(this));
        } else {
            vMPreparedOperand.setType(VMOpType.VM_OPREGMEM);
            if ((fgetbits & 8192) == 0) {
                vMPreparedOperand.setData((fgetbits >> 10) & 7);
                vMPreparedOperand.setOffset(vMPreparedOperand.getData());
                vMPreparedOperand.setBase(0);
                faddbits(6);
                return;
            }
            if ((fgetbits & 4096) == 0) {
                vMPreparedOperand.setData((fgetbits >> 9) & 7);
                vMPreparedOperand.setOffset(vMPreparedOperand.getData());
                faddbits(7);
            } else {
                vMPreparedOperand.setData(0);
                faddbits(4);
            }
            vMPreparedOperand.setBase(ReadData(this));
        }
    }

    private void optimize(VMPreparedProgram vMPreparedProgram) {
        List<VMPreparedCommand> cmd = vMPreparedProgram.getCmd();
        for (VMPreparedCommand next : cmd) {
            int i = AnonymousClass1.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands[next.getOpCode().ordinal()];
            if (i == 1) {
                next.setOpCode(next.isByteMode() ? VMCommands.VM_MOVB : VMCommands.VM_MOVD);
            } else if (i == 4) {
                next.setOpCode(next.isByteMode() ? VMCommands.VM_CMPB : VMCommands.VM_CMPD);
            } else if ((VMCmdFlags.VM_CmdFlags[next.getOpCode().getVMCommand()] & VMCmdFlags.VMCF_CHFLAGS) != 0) {
                for (int indexOf = cmd.indexOf(next) + 1; indexOf < cmd.size(); indexOf++) {
                    byte b = VMCmdFlags.VM_CmdFlags[cmd.get(indexOf).getOpCode().getVMCommand()];
                    if ((b & 56) != 0 || (b & VMCmdFlags.VMCF_CHFLAGS) != 0) {
                        break;
                    }
                }
                int i2 = AnonymousClass1.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands[next.getOpCode().ordinal()];
                if (i2 == 7) {
                    next.setOpCode(next.isByteMode() ? VMCommands.VM_ADDB : VMCommands.VM_ADDD);
                } else if (i2 == 10) {
                    next.setOpCode(next.isByteMode() ? VMCommands.VM_SUBB : VMCommands.VM_SUBD);
                } else if (i2 == 15) {
                    next.setOpCode(next.isByteMode() ? VMCommands.VM_INCB : VMCommands.VM_INCD);
                } else if (i2 == 18) {
                    next.setOpCode(next.isByteMode() ? VMCommands.VM_DECB : VMCommands.VM_DECD);
                } else if (i2 == 39) {
                    next.setOpCode(next.isByteMode() ? VMCommands.VM_NEGB : VMCommands.VM_NEGD);
                }
            }
        }
    }

    private VMStandardFilters IsStandardFilter(byte[] bArr, int i) {
        VMStandardFilterSignature[] vMStandardFilterSignatureArr = {new VMStandardFilterSignature(53, -1386780537, VMStandardFilters.VMSF_E8), new VMStandardFilterSignature(57, 1020781950, VMStandardFilters.VMSF_E8E9), new VMStandardFilterSignature(120, 929663295, VMStandardFilters.VMSF_ITANIUM), new VMStandardFilterSignature(29, 235276157, VMStandardFilters.VMSF_DELTA), new VMStandardFilterSignature(149, 472669640, VMStandardFilters.VMSF_RGB), new VMStandardFilterSignature(216, -1132075263, VMStandardFilters.VMSF_AUDIO), new VMStandardFilterSignature(40, 1186579808, VMStandardFilters.VMSF_UPCASE)};
        int checkCrc = RarCRC.checkCrc(-1, bArr, 0, bArr.length) ^ -1;
        for (int i2 = 0; i2 < 7; i2++) {
            if (vMStandardFilterSignatureArr[i2].getCRC() == checkCrc && vMStandardFilterSignatureArr[i2].getLength() == bArr.length) {
                return vMStandardFilterSignatureArr[i2].getType();
            }
        }
        return VMStandardFilters.VMSF_NONE;
    }

    public static class AnonymousClass1 {
        static final int[] $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands;
        static final int[] $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters;

        static {
            int[] iArr = new int[VMStandardFilters.values().length];
            $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters = iArr;
            try {
                iArr[VMStandardFilters.VMSF_E8.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_E8E9.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_ITANIUM.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_DELTA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_RGB.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_AUDIO.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[VMStandardFilters.VMSF_UPCASE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr2 = new int[VMCommands.values().length];
            $SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands = iArr2;
            iArr2[VMCommands.VM_MOV.ordinal()] = 1;
            iArr2[VMCommands.VM_MOVB.ordinal()] = 2;
            iArr2[VMCommands.VM_MOVD.ordinal()] = 3;
            iArr2[VMCommands.VM_CMP.ordinal()] = 4;
            iArr2[VMCommands.VM_CMPB.ordinal()] = 5;
            iArr2[VMCommands.VM_CMPD.ordinal()] = 6;
            iArr2[VMCommands.VM_ADD.ordinal()] = 7;
            iArr2[VMCommands.VM_ADDB.ordinal()] = 8;
            iArr2[VMCommands.VM_ADDD.ordinal()] = 9;
            iArr2[VMCommands.VM_SUB.ordinal()] = 10;
            iArr2[VMCommands.VM_SUBB.ordinal()] = 11;
            iArr2[VMCommands.VM_SUBD.ordinal()] = 12;
            iArr2[VMCommands.VM_JZ.ordinal()] = 13;
            iArr2[VMCommands.VM_JNZ.ordinal()] = 14;
            iArr2[VMCommands.VM_INC.ordinal()] = 15;
            iArr2[VMCommands.VM_INCB.ordinal()] = 16;
            iArr2[VMCommands.VM_INCD.ordinal()] = 17;
            iArr2[VMCommands.VM_DEC.ordinal()] = 18;
            iArr2[VMCommands.VM_DECB.ordinal()] = 19;
            iArr2[VMCommands.VM_DECD.ordinal()] = 20;
            iArr2[VMCommands.VM_JMP.ordinal()] = 21;
            iArr2[VMCommands.VM_XOR.ordinal()] = 22;
            iArr2[VMCommands.VM_AND.ordinal()] = 23;
            iArr2[VMCommands.VM_OR.ordinal()] = 24;
            iArr2[VMCommands.VM_TEST.ordinal()] = 25;
            iArr2[VMCommands.VM_JS.ordinal()] = 26;
            iArr2[VMCommands.VM_JNS.ordinal()] = 27;
            iArr2[VMCommands.VM_JB.ordinal()] = 28;
            iArr2[VMCommands.VM_JBE.ordinal()] = 29;
            iArr2[VMCommands.VM_JA.ordinal()] = 30;
            iArr2[VMCommands.VM_JAE.ordinal()] = 31;
            iArr2[VMCommands.VM_PUSH.ordinal()] = 32;
            iArr2[VMCommands.VM_POP.ordinal()] = 33;
            iArr2[VMCommands.VM_CALL.ordinal()] = 34;
            iArr2[VMCommands.VM_NOT.ordinal()] = 35;
            iArr2[VMCommands.VM_SHL.ordinal()] = 36;
            iArr2[VMCommands.VM_SHR.ordinal()] = 37;
            iArr2[VMCommands.VM_SAR.ordinal()] = 38;
            iArr2[VMCommands.VM_NEG.ordinal()] = 39;
            iArr2[VMCommands.VM_NEGB.ordinal()] = 40;
            iArr2[VMCommands.VM_NEGD.ordinal()] = 41;
            iArr2[VMCommands.VM_PUSHA.ordinal()] = 42;
            iArr2[VMCommands.VM_POPA.ordinal()] = 43;
            iArr2[VMCommands.VM_PUSHF.ordinal()] = 44;
            iArr2[VMCommands.VM_POPF.ordinal()] = 45;
            iArr2[VMCommands.VM_MOVZX.ordinal()] = 46;
            iArr2[VMCommands.VM_MOVSX.ordinal()] = 47;
            iArr2[VMCommands.VM_XCHG.ordinal()] = 48;
            iArr2[VMCommands.VM_MUL.ordinal()] = 49;
            iArr2[VMCommands.VM_DIV.ordinal()] = 50;
            iArr2[VMCommands.VM_ADC.ordinal()] = 51;
            iArr2[VMCommands.VM_SBB.ordinal()] = 52;
            iArr2[VMCommands.VM_RET.ordinal()] = 53;
            iArr2[VMCommands.VM_STANDARD.ordinal()] = 54;
            try {
                iArr2[VMCommands.VM_PRINT.ordinal()] = 55;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private void ExecuteStandardFilter(VMStandardFilters vMStandardFilters) {
        byte b;
        int i;
        int i2;
        RarVM rarVM = this;
        char c = 3;
        switch (AnonymousClass1.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMStandardFilters[vMStandardFilters.ordinal()]) {
            case 1:
            case 2:
                int[] iArr = rarVM.R;
                int i3 = iArr[4];
                long j = (long) (iArr[6] & -1);
                if (i3 < 245760) {
                    byte b2 = (byte) (vMStandardFilters == VMStandardFilters.VMSF_E8E9 ? 233 : 232);
                    int i4 = 0;
                    while (i4 < i3 - 4) {
                        byte[] bArr = rarVM.mem;
                        int i5 = i4 + 1;
                        byte b3 = bArr[i4];
                        if (b3 == 232 || b3 == b2) {
                            long j2 = ((long) i5) + j;
                            long value = (long) rarVM.getValue(false, bArr, i5);
                            if ((value & -2147483648L) != 0) {
                                if (((j2 + value) & -2147483648L) == 0) {
                                    rarVM.setValue(false, rarVM.mem, i5, ((int) value) + 16777216);
                                }
                            } else if (((value - 16777216) & -2147483648L) != 0) {
                                rarVM.setValue(false, rarVM.mem, i5, (int) (value - j2));
                            }
                            i5 += 4;
                        }
                        i4 = i5;
                    }
                    return;
                }
                return;
            case 3:
                int[] iArr2 = rarVM.R;
                int i6 = iArr2[4];
                long j3 = (long) (iArr2[6] & -1);
                if (i6 < 245760) {
                    byte[] bArr2 = {4, 4, 6, 6, 0, 0, 7, 7, 4, 4, 0, 0, 4, 4, 0, 0};
                    long j4 = j3 >>> 4;
                    int i7 = 0;
                    while (i7 < i6 - 21) {
                        int i8 = (rarVM.mem[i7] & 31) - VMCmdFlags.VMCF_PROC;
                        if (i8 >= 0 && (b = bArr2[i8]) != 0) {
                            for (int i9 = 0; i9 <= 2; i9++) {
                                if (((1 << i9) & b) != 0) {
                                    int i10 = (i9 * 41) + 5;
                                    if (rarVM.filterItanium_GetBits(i7, i10 + 37, 4) == 5) {
                                        int i11 = i10 + 13;
                                        rarVM.filterItanium_SetBits(i7, ((int) (((long) rarVM.filterItanium_GetBits(i7, i11, 20)) - j4)) & 1048575, i11, 20);
                                    }
                                }
                            }
                        }
                        i7 += 16;
                        j4++;
                    }
                    return;
                }
                return;
            case 4:
                int[] iArr3 = rarVM.R;
                int i12 = iArr3[4] & -1;
                int i13 = iArr3[0] & -1;
                int i14 = (i12 * 2) & -1;
                rarVM.setValue(false, rarVM.mem, 245792, i12);
                if (i12 < 122880) {
                    int i15 = 0;
                    for (int i16 = 0; i16 < i13; i16++) {
                        int i17 = i12 + i16;
                        byte b4 = 0;
                        while (i17 < i14) {
                            byte[] bArr3 = rarVM.mem;
                            b4 = (byte) (b4 - bArr3[i15]);
                            bArr3[i17] = b4;
                            i17 += i13;
                            i15++;
                        }
                    }
                    return;
                }
                return;
            case 5:
                int[] iArr4 = rarVM.R;
                int i18 = iArr4[4];
                int i19 = 3;
                int i20 = iArr4[0] - 3;
                int i21 = iArr4[1];
                rarVM.setValue(false, rarVM.mem, 245792, i18);
                if (i18 < 122880 && i21 >= 0) {
                    int i22 = 0;
                    int i23 = 0;
                    while (i22 < i19) {
                        int i24 = i22;
                        long j5 = 0;
                        while (i24 < i18) {
                            int i25 = i24 - i20;
                            if (i25 >= i19) {
                                int i26 = i25 + i18;
                                byte[] bArr4 = rarVM.mem;
                                long j6 = (long) (bArr4[i26] & -1);
                                long j7 = (long) (bArr4[i26 - i19] & -1);
                                long j8 = (j5 + j6) - j7;
                                i2 = i22;
                                i = i20;
                                int abs = Math.abs((int) (j8 - j5));
                                long j9 = j5;
                                int abs2 = Math.abs((int) (j8 - j6));
                                int abs3 = Math.abs((int) (j8 - j7));
                                j5 = (abs > abs2 || abs > abs3) ? abs2 <= abs3 ? j6 : j7 : j9;
                            } else {
                                i2 = i22;
                                i = i20;
                                long j10 = j5;
                            }
                            byte[] bArr5 = rarVM.mem;
                            j5 = (j5 - ((long) bArr5[i23])) & 255 & 255;
                            bArr5[i18 + i24] = (byte) ((int) (j5 & 255));
                            i24 += 3;
                            i23++;
                            i20 = i;
                            i22 = i2;
                            i19 = 3;
                        }
                        i22++;
                        i20 = i20;
                        i19 = 3;
                    }
                    int i27 = i18 - 2;
                    while (i21 < i27) {
                        byte[] bArr6 = rarVM.mem;
                        int i28 = i18 + i21;
                        byte b5 = bArr6[i28 + 1];
                        bArr6[i28] = (byte) (bArr6[i28] + b5);
                        int i29 = i28 + 2;
                        bArr6[i29] = (byte) (bArr6[i29] + b5);
                        i21 += 3;
                    }
                    return;
                }
                return;
            case 6:
                int[] iArr5 = rarVM.R;
                int i30 = iArr5[4];
                int i31 = iArr5[0];
                rarVM.setValue(false, rarVM.mem, 245792, i30);
                if (i30 < 122880) {
                    int i32 = 0;
                    int i33 = 0;
                    while (i32 < i31) {
                        long[] jArr = new long[7];
                        int i34 = i32;
                        int i35 = 0;
                        long j11 = 0;
                        int i36 = 0;
                        int i37 = 0;
                        int i38 = 0;
                        long j12 = 0;
                        int i39 = 0;
                        int i40 = 0;
                        while (i34 < i30) {
                            int i41 = (int) j11;
                            int i42 = i41 - i38;
                            byte[] bArr7 = rarVM.mem;
                            int i43 = i33 + 1;
                            long j13 = (long) (bArr7[i33] & -1);
                            int i44 = i32;
                            long j14 = (((((((8 * j12) + ((long) (i35 * i41))) + ((long) (i36 * i42))) + ((long) (i37 * i39))) >>> c) & 255) - j13) & -1;
                            bArr7[i30 + i34] = (byte) ((int) j14);
                            int i45 = ((byte) ((int) j13)) << 3;
                            long j15 = (long) ((byte) ((int) (j14 - j12)));
                            jArr[0] = jArr[0] + ((long) Math.abs(i45));
                            long j16 = j14;
                            jArr[1] = jArr[1] + ((long) Math.abs(i45 - i41));
                            int i46 = i31;
                            jArr[2] = jArr[2] + ((long) Math.abs(i45 + i41));
                            jArr[3] = jArr[3] + ((long) Math.abs(i45 - i42));
                            jArr[4] = jArr[4] + ((long) Math.abs(i45 + i42));
                            jArr[5] = jArr[5] + ((long) Math.abs(i45 - i39));
                            jArr[6] = jArr[6] + ((long) Math.abs(i45 + i39));
                            if ((i40 & 31) == 0) {
                                long j17 = jArr[0];
                                long j18 = 0;
                                jArr[0] = 0;
                                long j19 = 0;
                                int i47 = 1;
                                while (i47 < 7) {
                                    if (jArr[i47] < j17) {
                                        j17 = jArr[i47];
                                        j19 = (long) i47;
                                    }
                                    jArr[i47] = j18;
                                    i47++;
                                    j18 = 0;
                                }
                                switch ((int) j19) {
                                    case 1:
                                        if (i35 < -16) {
                                            break;
                                        } else {
                                            i35--;
                                            break;
                                        }
                                    case 2:
                                        if (i35 >= 16) {
                                            break;
                                        } else {
                                            i35++;
                                            break;
                                        }
                                    case 3:
                                        if (i36 < -16) {
                                            break;
                                        } else {
                                            i36--;
                                            break;
                                        }
                                    case 4:
                                        if (i36 >= 16) {
                                            break;
                                        } else {
                                            i36++;
                                            break;
                                        }
                                    case 5:
                                        if (i37 < -16) {
                                            break;
                                        } else {
                                            i37--;
                                            break;
                                        }
                                    case 6:
                                        if (i37 >= 16) {
                                            break;
                                        } else {
                                            i37++;
                                            break;
                                        }
                                }
                            }
                            i34 += i46;
                            i40++;
                            rarVM = this;
                            i39 = i42;
                            i31 = i46;
                            i33 = i43;
                            i32 = i44;
                            j12 = j16;
                            c = 3;
                            i38 = i41;
                            j11 = j15;
                        }
                        int i48 = i31;
                        i32++;
                        rarVM = this;
                        c = 3;
                    }
                    return;
                }
                return;
            case 7:
                int i49 = rarVM.R[4];
                if (i49 < 122880) {
                    int i50 = i49;
                    int i51 = 0;
                    while (i51 < i49) {
                        byte[] bArr8 = rarVM.mem;
                        int i52 = i51 + 1;
                        byte b6 = bArr8[i51];
                        if (b6 == 2) {
                            int i53 = i52 + 1;
                            byte b7 = bArr8[i52];
                            if (b7 != 2) {
                                b7 = (byte) (b7 - 32);
                            }
                            byte b8 = b7;
                            i52 = i53;
                            b6 = b8;
                        }
                        bArr8[i50] = b6;
                        i50++;
                        i51 = i52;
                    }
                    rarVM.setValue(false, rarVM.mem, 245788, i50 - i49);
                    rarVM.setValue(false, rarVM.mem, 245792, i49);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void filterItanium_SetBits(int i, int i2, int i3, int i4) {
        int i5 = i3 / 8;
        int i6 = i3 & 7;
        byte b = (byte) (((-1 >>> (32 - i4)) << i6) ^ -1);
        int i7 = i2 << i6;
        for (int i8 = 0; i8 < 4; i8++) {
            byte[] bArr = this.mem;
            int i9 = i + i5 + i8;
            bArr[i9] = (byte) (bArr[i9] & b);
            bArr[i9] = (byte) (bArr[i9] | i7);
            b = (byte) ((b >>> 8) | -16777216);
            i7 >>>= 8;
        }
    }

    private int filterItanium_GetBits(int i, int i2, int i3) {
        int i4 = i2 / 8;
        byte[] bArr = this.mem;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        return (((((bArr[i6 + i] & -1) << VMCmdFlags.VMCF_PROC) | ((bArr[i4 + i] & -1) | ((bArr[i5 + i] & -1) << 8))) | ((bArr[(i6 + 1) + i] & -1) << 24)) >>> (i2 & 7)) & (-1 >>> (32 - i3));
    }

    public void setMemory(int i, byte[] bArr, int i2, int i3) {
        if (i < 262144) {
            int i4 = 0;
            while (i4 < Math.min(bArr.length - i2, i3) && 262144 - i >= i4) {
                this.mem[i + i4] = bArr[i2 + i4];
                i4++;
            }
        }
    }
}
