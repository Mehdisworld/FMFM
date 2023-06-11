package ir.mahdi.mzip.rar.unpack.ppm;

//import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.io.Raw;

public class PPMContext extends Pointer {
    public static final int[] ExpEscape = {25, 14, 9, 7, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2};
    public static final int size;
    private static final int unionSize;
    private final FreqData freqData;
    private int numStats;
    private final State oneState;
    private final int[] ps = new int[256];
    private int suffix;
    private PPMContext tempPPMContext = null;
    private final State tempState1 = new State((byte[]) null);
    private final State tempState2 = new State((byte[]) null);
    private final State tempState3 = new State((byte[]) null);
    private final State tempState4 = new State((byte[]) null);
    private final State tempState5 = new State((byte[]) null);

    public int getMean(int i, int i2, int i3) {
        return (i + (1 << (i2 - i3))) >>> i2;
    }

    static {
        int max = Math.max(6, 6);
        unionSize = max;
        size = max + 2 + 4;
    }

    public PPMContext(byte[] bArr) {
        super(bArr);
        this.oneState = new State(bArr);
        this.freqData = new FreqData(bArr);
    }

    public PPMContext init(byte[] bArr) {
        this.mem = bArr;
        this.pos = 0;
        this.oneState.init(bArr);
        this.freqData.init(bArr);
        return this;
    }

    public FreqData getFreqData() {
        return this.freqData;
    }

    public void setFreqData(FreqData freqData2) {
        this.freqData.setSummFreq(freqData2.getSummFreq());
        this.freqData.setStats(freqData2.getStats());
    }

    public final int getNumStats() {
        if (this.mem != null) {
            this.numStats = Raw.readShortLittleEndian(this.mem, this.pos) & -1;
        }
        return this.numStats;
    }

    public final void setNumStats(int i) {
        this.numStats = 65535 & i;
        if (this.mem != null) {
            Raw.writeShortLittleEndian(this.mem, this.pos, (short) i);
        }
    }

    public State getOneState() {
        return this.oneState;
    }

    public void setOneState(StateRef stateRef) {
        this.oneState.setValues(stateRef);
    }

    public int getSuffix() {
        if (this.mem != null) {
            this.suffix = Raw.readIntLittleEndian(this.mem, this.pos + 8);
        }
        return this.suffix;
    }

    public void setSuffix(int i) {
        this.suffix = i;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos + 8, i);
        }
    }

    public void setSuffix(PPMContext pPMContext) {
        setSuffix(pPMContext.getAddress());
    }

    public void setAddress(int i) {
        super.setAddress(i);
        int i2 = i + 2;
        this.oneState.setAddress(i2);
        this.freqData.setAddress(i2);
    }

    private PPMContext getTempPPMContext(byte[] bArr) {
        if (this.tempPPMContext == null) {
            this.tempPPMContext = new PPMContext((byte[]) null);
        }
        return this.tempPPMContext.init(bArr);
    }

    public int createChild(ModelPPM modelPPM, State state, StateRef stateRef) {
        PPMContext tempPPMContext2 = getTempPPMContext(modelPPM.getSubAlloc().getHeap());
        tempPPMContext2.setAddress(modelPPM.getSubAlloc().allocContext());
        if (tempPPMContext2 != null) {
            tempPPMContext2.setNumStats(1);
            tempPPMContext2.setOneState(stateRef);
            tempPPMContext2.setSuffix(this);
            state.setSuccessor(tempPPMContext2);
        }
        return tempPPMContext2.getAddress();
    }

    public void rescale(ModelPPM modelPPM) {
        int numStats2 = getNumStats();
        int numStats3 = getNumStats() - 1;
        State state = new State(modelPPM.getHeap());
        State state2 = new State(modelPPM.getHeap());
        State state3 = new State(modelPPM.getHeap());
        state2.setAddress(modelPPM.getFoundState().getAddress());
        while (state2.getAddress() != this.freqData.getStats()) {
            state3.setAddress(state2.getAddress() - 6);
            State.ppmdSwap(state2, state3);
            state2.decAddress();
        }
        state3.setAddress(this.freqData.getStats());
        state3.incFreq(4);
        this.freqData.incSummFreq(4);
        int summFreq = this.freqData.getSummFreq() - state2.getFreq();
        int i = modelPPM.getOrderFall() != 0 ? 1 : 0;
        state2.setFreq((state2.getFreq() + i) >>> 1);
        this.freqData.setSummFreq(state2.getFreq());
        do {
            state2.incAddress();
            summFreq -= state2.getFreq();
            state2.setFreq((state2.getFreq() + i) >>> 1);
            this.freqData.incSummFreq(state2.getFreq());
            state3.setAddress(state2.getAddress() - 6);
            if (state2.getFreq() > state3.getFreq()) {
                state.setAddress(state2.getAddress());
                StateRef stateRef = new StateRef();
                stateRef.setValues(state);
                State state4 = new State(modelPPM.getHeap());
                State state5 = new State(modelPPM.getHeap());
                do {
                    state4.setAddress(state.getAddress() - 6);
                    state.setValues(state4);
                    state.decAddress();
                    state5.setAddress(state.getAddress() - 6);
                    if (state.getAddress() == this.freqData.getStats() || stateRef.getFreq() <= state5.getFreq()) {
                        state.setValues(stateRef);
                    }
                    state4.setAddress(state.getAddress() - 6);
                    state.setValues(state4);
                    state.decAddress();
                    state5.setAddress(state.getAddress() - 6);
                    break;
                } while (stateRef.getFreq() <= state5.getFreq());
                state.setValues(stateRef);
            }
            numStats3--;
        } while (numStats3 != 0);
        if (state2.getFreq() == 0) {
            do {
                numStats3++;
                state2.decAddress();
            } while (state2.getFreq() == 0);
            summFreq += numStats3;
            setNumStats(getNumStats() - numStats3);
            if (getNumStats() == 1) {
                StateRef stateRef2 = new StateRef();
                state3.setAddress(this.freqData.getStats());
                stateRef2.setValues(state3);
                do {
                    stateRef2.decFreq(stateRef2.getFreq() >>> 1);
                    summFreq >>>= 1;
                } while (summFreq > 1);
                modelPPM.getSubAlloc().freeUnits(this.freqData.getStats(), (numStats2 + 1) >>> 1);
                this.oneState.setValues(stateRef2);
                modelPPM.getFoundState().setAddress(this.oneState.getAddress());
                return;
            }
        }
        this.freqData.incSummFreq(summFreq - (summFreq >>> 1));
        int i2 = (numStats2 + 1) >>> 1;
        int numStats4 = (getNumStats() + 1) >>> 1;
        if (i2 != numStats4) {
            this.freqData.setStats(modelPPM.getSubAlloc().shrinkUnits(this.freqData.getStats(), i2, numStats4));
        }
        modelPPM.getFoundState().setAddress(this.freqData.getStats());
    }

    private int getArrayIndex(ModelPPM modelPPM, State state) {
        PPMContext tempPPMContext2 = getTempPPMContext(modelPPM.getSubAlloc().getHeap());
        tempPPMContext2.setAddress(getSuffix());
        return modelPPM.getPrevSuccess() + 0 + modelPPM.getNS2BSIndx()[tempPPMContext2.getNumStats() - 1] + modelPPM.getHiBitsFlag() + (modelPPM.getHB2Flag()[state.getSymbol()] * 2) + ((modelPPM.getRunLength() >>> 26) & 32);
    }

    public void decodeBinSymbol(ModelPPM modelPPM) {
        State init = this.tempState1.init(modelPPM.getHeap());
        init.setAddress(this.oneState.getAddress());
        modelPPM.setHiBitsFlag(modelPPM.getHB2Flag()[modelPPM.getFoundState().getSymbol()]);
        int freq = init.getFreq() - 1;
        int arrayIndex = getArrayIndex(modelPPM, init);
        int i = modelPPM.getBinSumm()[freq][arrayIndex];
        long j = (long) i;
        int i2 = 0;
        if (modelPPM.getCoder().getCurrentShiftCount(14) < j) {
            modelPPM.getFoundState().setAddress(init.getAddress());
            if (init.getFreq() < 128) {
                i2 = 1;
            }
            init.incFreq(i2);
            modelPPM.getCoder().getSubRange().setLowCount(0);
            modelPPM.getCoder().getSubRange().setHighCount(j);
            modelPPM.getBinSumm()[freq][arrayIndex] = ((i + 128) - getMean(i, 7, 2)) & 65535;
            modelPPM.setPrevSuccess(1);
            modelPPM.incRunLength(1);
            return;
        }
        modelPPM.getCoder().getSubRange().setLowCount(j);
        int mean = (i - getMean(i, 7, 2)) & 65535;
        modelPPM.getBinSumm()[freq][arrayIndex] = mean;
        modelPPM.getCoder().getSubRange().setHighCount(16384);
        modelPPM.setInitEsc(ExpEscape[mean >>> 10]);
        modelPPM.setNumMasked(1);
        modelPPM.getCharMask()[init.getSymbol()] = modelPPM.getEscCount();
        modelPPM.setPrevSuccess(0);
        modelPPM.getFoundState().setAddress(0);
    }

    public void update1(ModelPPM modelPPM, int i) {
        modelPPM.getFoundState().setAddress(i);
        modelPPM.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        State init = this.tempState3.init(modelPPM.getHeap());
        State init2 = this.tempState4.init(modelPPM.getHeap());
        init.setAddress(i);
        init2.setAddress(i - 6);
        if (init.getFreq() > init2.getFreq()) {
            State.ppmdSwap(init, init2);
            modelPPM.getFoundState().setAddress(init2.getAddress());
            if (init2.getFreq() > 124) {
                rescale(modelPPM);
            }
        }
    }

    public boolean decodeSymbol2(ModelPPM modelPPM) {
        long j;
        int numStats2 = getNumStats() - modelPPM.getNumMasked();
        SEE2Context makeEscFreq2 = makeEscFreq2(modelPPM, numStats2);
        RangeCoder coder = modelPPM.getCoder();
        State init = this.tempState1.init(modelPPM.getHeap());
        State init2 = this.tempState2.init(modelPPM.getHeap());
        init.setAddress(this.freqData.getStats() - 6);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            init.incAddress();
            if (modelPPM.getCharMask()[init.getSymbol()] != modelPPM.getEscCount()) {
                i2 += init.getFreq();
                int i4 = i3 + 1;
                this.ps[i3] = init.getAddress();
                numStats2--;
                if (numStats2 == 0) {
                    break;
                }
                i3 = i4;
            }
        }
        coder.getSubRange().incScale(i2);
        long currentCount = (long) coder.getCurrentCount();
        if (currentCount >= coder.getSubRange().getScale()) {
            return false;
        }
        init.setAddress(this.ps[0]);
        long j2 = (long) i2;
        if (currentCount < j2) {
            int i5 = 0;
            while (true) {
                i += init.getFreq();
                j = (long) i;
                if (j > currentCount) {
                    break;
                }
                i5++;
                init.setAddress(this.ps[i5]);
            }
            coder.getSubRange().setHighCount(j);
            coder.getSubRange().setLowCount((long) (i - init.getFreq()));
            makeEscFreq2.update();
            update2(modelPPM, init.getAddress());
        } else {
            coder.getSubRange().setLowCount(j2);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            int numStats3 = getNumStats() - modelPPM.getNumMasked();
            int i6 = -1;
            do {
                i6++;
                init2.setAddress(this.ps[i6]);
                modelPPM.getCharMask()[init2.getSymbol()] = modelPPM.getEscCount();
                numStats3--;
            } while (numStats3 != 0);
            makeEscFreq2.incSumm((int) coder.getSubRange().getScale());
            modelPPM.setNumMasked(getNumStats());
        }
        return true;
    }

    public void update2(ModelPPM modelPPM, int i) {
        State init = this.tempState5.init(modelPPM.getHeap());
        init.setAddress(i);
        modelPPM.getFoundState().setAddress(i);
        modelPPM.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        if (init.getFreq() > 124) {
            rescale(modelPPM);
        }
        modelPPM.incEscCount(1);
        modelPPM.setRunLength(modelPPM.getInitRL());
    }

    private SEE2Context makeEscFreq2(ModelPPM modelPPM, int i) {
        int numStats2 = getNumStats();
        if (numStats2 != 256) {
            PPMContext tempPPMContext2 = getTempPPMContext(modelPPM.getHeap());
            tempPPMContext2.setAddress(getSuffix());
            int i2 = modelPPM.getNS2Indx()[i - 1];
            int i3 = 0;
            int i4 = (i < tempPPMContext2.getNumStats() - numStats2 ? 1 : 0) + 0 + ((this.freqData.getSummFreq() < numStats2 * 11 ? 1 : 0) * 2);
            if (modelPPM.getNumMasked() > i) {
                i3 = 1;
            }
            SEE2Context sEE2Context = modelPPM.getSEE2Cont()[i2][i4 + (i3 * 4) + modelPPM.getHiBitsFlag()];
            modelPPM.getCoder().getSubRange().setScale((long) sEE2Context.getMean());
            return sEE2Context;
        }
        SEE2Context dummySEE2Cont = modelPPM.getDummySEE2Cont();
        modelPPM.getCoder().getSubRange().setScale(1);
        return dummySEE2Cont;
    }

    public boolean decodeSymbol1(ModelPPM modelPPM) {
        long j;
        RangeCoder coder = modelPPM.getCoder();
        coder.getSubRange().setScale((long) this.freqData.getSummFreq());
        State state = new State(modelPPM.getHeap());
        state.setAddress(this.freqData.getStats());
        long currentCount = (long) coder.getCurrentCount();
        int i = 0;
        if (currentCount >= coder.getSubRange().getScale()) {
            return false;
        }
        int freq = state.getFreq();
        long j2 = (long) freq;
        if (currentCount < j2) {
            coder.getSubRange().setHighCount(j2);
            if (((long) (freq * 2)) > coder.getSubRange().getScale()) {
                i = 1;
            }
            modelPPM.setPrevSuccess(i);
            modelPPM.incRunLength(modelPPM.getPrevSuccess());
            int i2 = freq + 4;
            modelPPM.getFoundState().setAddress(state.getAddress());
            modelPPM.getFoundState().setFreq(i2);
            this.freqData.incSummFreq(4);
            if (i2 > 124) {
                rescale(modelPPM);
            }
            coder.getSubRange().setLowCount(0);
            return true;
        } else if (modelPPM.getFoundState().getAddress() == 0) {
            return false;
        } else {
            modelPPM.setPrevSuccess(0);
            int numStats2 = getNumStats();
            int i3 = numStats2 - 1;
            int i4 = i3;
            do {
                freq += state.incAddress().getFreq();
                j = (long) freq;
                if (j <= currentCount) {
                    i4--;
                } else {
                    coder.getSubRange().setLowCount((long) (freq - state.getFreq()));
                    coder.getSubRange().setHighCount(j);
                    update1(modelPPM, state.getAddress());
                    return true;
                }
            } while (i4 != 0);
            modelPPM.setHiBitsFlag(modelPPM.getHB2Flag()[modelPPM.getFoundState().getSymbol()]);
            coder.getSubRange().setLowCount(j);
            modelPPM.getCharMask()[state.getSymbol()] = modelPPM.getEscCount();
            modelPPM.setNumMasked(numStats2);
            modelPPM.getFoundState().setAddress(0);
            do {
                modelPPM.getCharMask()[state.decAddress().getSymbol()] = modelPPM.getEscCount();
                i3--;
            } while (i3 != 0);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            return true;
        }
    }

    public String toString() {
        return "PPMContext[\n  pos=" + this.pos + "\n  size=" + size + "\n  numStats=" + getNumStats() + "\n  Suffix=" + getSuffix() + "\n  freqData=" + this.freqData + "\n  oneState=" + this.oneState + "\n]";
    }
}
