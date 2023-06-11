package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ModelPPM {
    public static final int BIN_SCALE = 16384;
    public static final int INTERVAL = 128;
    public static final int INT_BITS = 7;
    private static int[] InitBinEsc = {15581, 7999, 22975, 18675, 25761, 23228, 26162, 24657};
    public static final int MAX_FREQ = 124;
    public static final int MAX_O = 64;
    public static final int PERIOD_BITS = 7;
    public static final int TOT_BITS = 14;
    private int[] HB2Flag = new int[256];
    private int[] NS2BSIndx = new int[256];
    private int[] NS2Indx = new int[256];
    private SEE2Context[][] SEE2Cont = ((SEE2Context[][]) Array.newInstance(SEE2Context.class, new int[]{25, 16}));
    private int[][] binSumm = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{128, 64}));
    private int[] charMask = new int[256];
    private RangeCoder coder = new RangeCoder();
    private SEE2Context dummySEE2Cont;
    private int escCount;
    private State foundState;
    private int hiBitsFlag;
    private int initEsc;
    private int initRL;
    private PPMContext maxContext = null;
    private int maxOrder;
    private PPMContext medContext = null;
    private PPMContext minContext = null;
    private int numMasked;
    private int orderFall;
    private int prevSuccess;
    private final int[] ps = new int[64];
    private int runLength;
    private SubAllocator subAlloc = new SubAllocator();
    private final PPMContext tempPPMContext1 = new PPMContext((byte[]) null);
    private final PPMContext tempPPMContext2 = new PPMContext((byte[]) null);
    private final PPMContext tempPPMContext3 = new PPMContext((byte[]) null);
    private final PPMContext tempPPMContext4 = new PPMContext((byte[]) null);
    private final State tempState1 = new State((byte[]) null);
    private final State tempState2 = new State((byte[]) null);
    private final State tempState3 = new State((byte[]) null);
    private final State tempState4 = new State((byte[]) null);
    private final StateRef tempStateRef1 = new StateRef();
    private final StateRef tempStateRef2 = new StateRef();

    public SubAllocator getSubAlloc() {
        return this.subAlloc;
    }

    private void restartModelRare() {
        Arrays.fill(this.charMask, 0);
        this.subAlloc.initSubAllocator();
        int i = this.maxOrder;
        if (i >= 12) {
            i = 12;
        }
        this.initRL = (-i) - 1;
        int allocContext = this.subAlloc.allocContext();
        this.minContext.setAddress(allocContext);
        this.maxContext.setAddress(allocContext);
        this.minContext.setSuffix(0);
        this.orderFall = this.maxOrder;
        this.minContext.setNumStats(256);
        this.minContext.getFreqData().setSummFreq(this.minContext.getNumStats() + 1);
        int allocUnits = this.subAlloc.allocUnits(128);
        this.foundState.setAddress(allocUnits);
        this.minContext.getFreqData().setStats(allocUnits);
        State state = new State(this.subAlloc.getHeap());
        int stats = this.minContext.getFreqData().getStats();
        this.runLength = this.initRL;
        this.prevSuccess = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            state.setAddress((i2 * 6) + stats);
            state.setSymbol(i2);
            state.setFreq(1);
            state.setSuccessor(0);
        }
        for (int i3 = 0; i3 < 128; i3++) {
            for (int i4 = 0; i4 < 8; i4++) {
                for (int i5 = 0; i5 < 64; i5 += 8) {
                    this.binSumm[i3][i4 + i5] = 16384 - (InitBinEsc[i4] / (i3 + 2));
                }
            }
        }
        for (int i6 = 0; i6 < 25; i6++) {
            for (int i7 = 0; i7 < 16; i7++) {
                this.SEE2Cont[i6][i7].init((i6 * 5) + 10);
            }
        }
    }

    private void startModelRare(int i) {
        this.escCount = 1;
        this.maxOrder = i;
        restartModelRare();
        int[] iArr = this.NS2BSIndx;
        iArr[0] = 0;
        iArr[1] = 2;
        for (int i2 = 0; i2 < 9; i2++) {
            this.NS2BSIndx[i2 + 2] = 4;
        }
        for (int i3 = 0; i3 < 245; i3++) {
            this.NS2BSIndx[i3 + 11] = 6;
        }
        int i4 = 0;
        while (i4 < 3) {
            this.NS2Indx[i4] = i4;
            i4++;
        }
        int i5 = i4;
        int i6 = 1;
        int i7 = 1;
        while (i4 < 256) {
            this.NS2Indx[i4] = i5;
            i6--;
            if (i6 == 0) {
                i7++;
                i5++;
                i6 = i7;
            }
            i4++;
        }
        for (int i8 = 0; i8 < 64; i8++) {
            this.HB2Flag[i8] = 0;
        }
        for (int i9 = 0; i9 < 192; i9++) {
            this.HB2Flag[i9 + 64] = 8;
        }
        this.dummySEE2Cont.setShift(7);
    }

    private void clearMask() {
        this.escCount = 1;
        Arrays.fill(this.charMask, 0);
    }

    public boolean decodeInit(Unpack unpack, int i) throws IOException, RarException {
        int i2;
        int i3 = unpack.getChar() & 255;
        boolean z = (i3 & 32) != 0;
        if (z) {
            i2 = unpack.getChar();
        } else if (this.subAlloc.GetAllocatedMemory() == 0) {
            return false;
        } else {
            i2 = 0;
        }
        if ((i3 & 64) != 0) {
            unpack.setPpmEscChar(unpack.getChar());
        }
        this.coder.initDecoder(unpack);
        if (z) {
            int i4 = (i3 & 31) + 1;
            if (i4 > 16) {
                i4 = ((i4 - 16) * 3) + 16;
            }
            if (i4 == 1) {
                this.subAlloc.stopSubAllocator();
                return false;
            }
            this.subAlloc.startSubAllocator(i2 + 1);
            this.minContext = new PPMContext(getHeap());
            this.medContext = new PPMContext(getHeap());
            this.maxContext = new PPMContext(getHeap());
            this.foundState = new State(getHeap());
            this.dummySEE2Cont = new SEE2Context();
            for (int i5 = 0; i5 < 25; i5++) {
                for (int i6 = 0; i6 < 16; i6++) {
                    this.SEE2Cont[i5][i6] = new SEE2Context();
                }
            }
            startModelRare(i4);
        }
        return this.minContext.getAddress() != 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0058, code lost:
        if (r4.minContext.decodeSymbol1(r4) != false) goto L_0x005c;
     */
    public int decodeChar() throws IOException, RarException {
        if (this.minContext.getAddress() > this.subAlloc.getPText() && this.minContext.getAddress() <= this.subAlloc.getHeapEnd()) {
            if (this.minContext.getNumStats() == 1) {
                this.minContext.decodeBinSymbol(this);
            } else if (this.minContext.getFreqData().getStats() > this.subAlloc.getPText()) {
                if (this.minContext.getFreqData().getStats() <= this.subAlloc.getHeapEnd()) {
                }
            }
            this.coder.decode();
            while (this.foundState.getAddress() == 0) {
                this.coder.ariDecNormalize();
                do {
                    this.orderFall++;
                    PPMContext pPMContext = this.minContext;
                    pPMContext.setAddress(pPMContext.getSuffix());
                    if (this.minContext.getAddress() <= this.subAlloc.getPText() || this.minContext.getAddress() > this.subAlloc.getHeapEnd()) {
                        return -1;
                    }
                } while (this.minContext.getNumStats() == this.numMasked);
                if (!this.minContext.decodeSymbol2(this)) {
                    return -1;
                }
                this.coder.decode();
            }
            int symbol = this.foundState.getSymbol();
            if (this.orderFall != 0 || this.foundState.getSuccessor() <= this.subAlloc.getPText()) {
                updateModel();
                if (this.escCount == 0) {
                    clearMask();
                }
            } else {
                int successor = this.foundState.getSuccessor();
                this.minContext.setAddress(successor);
                this.maxContext.setAddress(successor);
            }
            this.coder.ariDecNormalize();
            return symbol;
        }
        return -1;
    }

    public SEE2Context[][] getSEE2Cont() {
        return this.SEE2Cont;
    }

    public SEE2Context getDummySEE2Cont() {
        return this.dummySEE2Cont;
    }

    public int getInitRL() {
        return this.initRL;
    }

    public int getEscCount() {
        return this.escCount;
    }

    public void setEscCount(int i) {
        this.escCount = i & 255;
    }

    public void incEscCount(int i) {
        setEscCount(getEscCount() + i);
    }

    public int[] getCharMask() {
        return this.charMask;
    }

    public int getNumMasked() {
        return this.numMasked;
    }

    public void setNumMasked(int i) {
        this.numMasked = i;
    }

    public int getInitEsc() {
        return this.initEsc;
    }

    public void setInitEsc(int i) {
        this.initEsc = i;
    }

    public int getRunLength() {
        return this.runLength;
    }

    public void setRunLength(int i) {
        this.runLength = i;
    }

    public void incRunLength(int i) {
        setRunLength(getRunLength() + i);
    }

    public int getPrevSuccess() {
        return this.prevSuccess;
    }

    public void setPrevSuccess(int i) {
        this.prevSuccess = i & 255;
    }

    public int getHiBitsFlag() {
        return this.hiBitsFlag;
    }

    public void setHiBitsFlag(int i) {
        this.hiBitsFlag = i & 255;
    }

    public int[][] getBinSumm() {
        return this.binSumm;
    }

    public RangeCoder getCoder() {
        return this.coder;
    }

    public int[] getHB2Flag() {
        return this.HB2Flag;
    }

    public int[] getNS2BSIndx() {
        return this.NS2BSIndx;
    }

    public int[] getNS2Indx() {
        return this.NS2Indx;
    }

    public State getFoundState() {
        return this.foundState;
    }

    public byte[] getHeap() {
        return this.subAlloc.getHeap();
    }

    public int getOrderFall() {
        return this.orderFall;
    }

    private int createSuccessors(boolean z, State state) {
        boolean z2;
        int i;
        int i2;
        StateRef stateRef = this.tempStateRef2;
        State init = this.tempState1.init(getHeap());
        PPMContext init2 = this.tempPPMContext1.init(getHeap());
        init2.setAddress(this.minContext.getAddress());
        PPMContext init3 = this.tempPPMContext2.init(getHeap());
        init3.setAddress(this.foundState.getSuccessor());
        State init4 = this.tempState2.init(getHeap());
        if (!z) {
            this.ps[0] = this.foundState.getAddress();
            z2 = init2.getSuffix() == 0;
            i = 1;
        } else {
            z2 = false;
            i = 0;
        }
        if (!z2) {
            if (state.getAddress() != 0) {
                init4.setAddress(state.getAddress());
                init2.setAddress(init2.getSuffix());
                if (init4.getSuccessor() == init3.getAddress()) {
                    init2.setAddress(init4.getSuccessor());
                } else {
                    this.ps[i] = init4.getAddress();
                    i++;
                }
            }
            init4.getSuccessor();
            init3.getAddress();
        }
        if (i == 0) {
            return init2.getAddress();
        }
        stateRef.setSymbol(getHeap()[init3.getAddress()]);
        stateRef.setSuccessor(init3.getAddress() + 1);
        if (init2.getNumStats() == 1) {
            stateRef.setFreq(init2.getOneState().getFreq());
        } else if (init2.getAddress() <= this.subAlloc.getPText()) {
            return 0;
        } else {
            init4.setAddress(init2.getFreqData().getStats());
            if (init4.getSymbol() != stateRef.getSymbol()) {
                do {
                    init4.incAddress();
                } while (init4.getSymbol() != stateRef.getSymbol());
            }
            int freq = init4.getFreq() - 1;
            int summFreq = (init2.getFreqData().getSummFreq() - init2.getNumStats()) - freq;
            int i3 = freq * 2;
            if (i3 <= summFreq) {
                i2 = freq * 5 > summFreq ? 1 : 0;
            } else {
                i2 = ((i3 + (summFreq * 3)) - 1) / (summFreq * 2);
            }
            stateRef.setFreq(i2 + 1);
        }
        do {
            i--;
            init.setAddress(this.ps[i]);
            init2.setAddress(init2.createChild(this, init, stateRef));
            if (init2.getAddress() == 0) {
                return 0;
            }
        } while (i != 0);
        return init2.getAddress();
    }

    private void updateModelRestart() {
        restartModelRare();
        this.escCount = 0;
    }

    private void updateModel() {
        int i;
        StateRef stateRef = this.tempStateRef1;
        stateRef.setValues(this.foundState);
        State init = this.tempState3.init(getHeap());
        State init2 = this.tempState4.init(getHeap());
        PPMContext init3 = this.tempPPMContext3.init(getHeap());
        PPMContext init4 = this.tempPPMContext4.init(getHeap());
        init3.setAddress(this.minContext.getSuffix());
        if (stateRef.getFreq() < 31 && init3.getAddress() != 0) {
            if (init3.getNumStats() != 1) {
                init.setAddress(init3.getFreqData().getStats());
                if (init.getSymbol() != stateRef.getSymbol()) {
                    do {
                        init.incAddress();
                    } while (init.getSymbol() != stateRef.getSymbol());
                    init2.setAddress(init.getAddress() - 6);
                    if (init.getFreq() >= init2.getFreq()) {
                        State.ppmdSwap(init, init2);
                        init.decAddress();
                    }
                }
                if (init.getFreq() < 115) {
                    init.incFreq(2);
                    init3.getFreqData().incSummFreq(2);
                }
            } else {
                init.setAddress(init3.getOneState().getAddress());
                if (init.getFreq() < 32) {
                    init.incFreq(1);
                }
            }
        }
        if (this.orderFall == 0) {
            this.foundState.setSuccessor(createSuccessors(true, init));
            this.minContext.setAddress(this.foundState.getSuccessor());
            this.maxContext.setAddress(this.foundState.getSuccessor());
            if (this.minContext.getAddress() == 0) {
                updateModelRestart();
                return;
            }
            return;
        }
        this.subAlloc.getHeap()[this.subAlloc.getPText()] = (byte) stateRef.getSymbol();
        this.subAlloc.incPText();
        init4.setAddress(this.subAlloc.getPText());
        if (this.subAlloc.getPText() >= this.subAlloc.getFakeUnitsStart()) {
            updateModelRestart();
            return;
        }
        if (stateRef.getSuccessor() != 0) {
            if (stateRef.getSuccessor() <= this.subAlloc.getPText()) {
                stateRef.setSuccessor(createSuccessors(false, init));
                if (stateRef.getSuccessor() == 0) {
                    updateModelRestart();
                    return;
                }
            }
            int i2 = this.orderFall - 1;
            this.orderFall = i2;
            if (i2 == 0) {
                init4.setAddress(stateRef.getSuccessor());
                if (this.maxContext.getAddress() != this.minContext.getAddress()) {
                    this.subAlloc.decPText(1);
                }
            }
        } else {
            this.foundState.setSuccessor(init4.getAddress());
            stateRef.setSuccessor(this.minContext);
        }
        int numStats = this.minContext.getNumStats();
        int summFreq = (this.minContext.getFreqData().getSummFreq() - numStats) - (stateRef.getFreq() - 1);
        init3.setAddress(this.maxContext.getAddress());
        while (init3.getAddress() != this.minContext.getAddress()) {
            int numStats2 = init3.getNumStats();
            if (numStats2 != 1) {
                if ((numStats2 & 1) == 0) {
                    init3.getFreqData().setStats(this.subAlloc.expandUnits(init3.getFreqData().getStats(), numStats2 >>> 1));
                    if (init3.getFreqData().getStats() == 0) {
                        updateModelRestart();
                        return;
                    }
                }
                init3.getFreqData().incSummFreq((numStats2 * 2 < numStats ? 1 : 0) + (((init3.getFreqData().getSummFreq() <= numStats2 * 8 ? 1 : 0) & (numStats2 * 4 <= numStats ? 1 : 0)) * 2));
            } else {
                init.setAddress(this.subAlloc.allocUnits(1));
                if (init.getAddress() == 0) {
                    updateModelRestart();
                    return;
                }
                init.setValues(init3.getOneState());
                init3.getFreqData().setStats(init);
                if (init.getFreq() < 30) {
                    init.incFreq(init.getFreq());
                } else {
                    init.setFreq(120);
                }
                init3.getFreqData().setSummFreq(init.getFreq() + this.initEsc + (numStats > 3 ? 1 : 0));
            }
            int freq = stateRef.getFreq() * 2 * (init3.getFreqData().getSummFreq() + 6);
            int summFreq2 = init3.getFreqData().getSummFreq() + summFreq;
            if (freq < summFreq2 * 6) {
                i = (freq > summFreq2 ? 1 : 0) + 1 + (freq >= summFreq2 * 4 ? 1 : 0);
                init3.getFreqData().incSummFreq(3);
            } else {
                i = (freq >= summFreq2 * 9 ? 1 : 0) + 4 + (freq >= summFreq2 * 12 ? 1 : 0) + (freq >= summFreq2 * 15 ? 1 : 0);
                init3.getFreqData().incSummFreq(i);
            }
            init.setAddress(init3.getFreqData().getStats() + (numStats2 * 6));
            init.setSuccessor(init4);
            init.setSymbol(stateRef.getSymbol());
            init.setFreq(i);
            init3.setNumStats(numStats2 + 1);
            init3.setAddress(init3.getSuffix());
        }
        int successor = stateRef.getSuccessor();
        this.maxContext.setAddress(successor);
        this.minContext.setAddress(successor);
    }

    public String toString() {
        return "ModelPPM[\n  numMasked=" + this.numMasked + "\n  initEsc=" + this.initEsc + "\n  orderFall=" + this.orderFall + "\n  maxOrder=" + this.maxOrder + "\n  runLength=" + this.runLength + "\n  initRL=" + this.initRL + "\n  escCount=" + this.escCount + "\n  prevSuccess=" + this.prevSuccess + "\n  foundState=" + this.foundState + "\n  coder=" + this.coder + "\n  subAlloc=" + this.subAlloc + "\n]";
    }
}
