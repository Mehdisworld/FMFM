package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class State extends Pointer {
    public static final int size = 6;

    public State(byte[] bArr) {
        super(bArr);
    }

    public static void ppmdSwap(State state, State state2) {
        byte[] bArr = state.mem;
        byte[] bArr2 = state2.mem;
        int i = state.pos;
        int i2 = state2.pos;
        int i3 = 0;
        while (i3 < 6) {
            byte b = bArr[i];
            bArr[i] = bArr2[i2];
            bArr2[i2] = b;
            i3++;
            i++;
            i2++;
        }
    }

    public State init(byte[] bArr) {
        this.mem = bArr;
        this.pos = 0;
        return this;
    }

    public int getSymbol() {
        return this.mem[this.pos] & -1;
    }

    public void setSymbol(int i) {
        this.mem[this.pos] = (byte) i;
    }

    public int getFreq() {
        return this.mem[this.pos + 1] & -1;
    }

    public void setFreq(int i) {
        this.mem[this.pos + 1] = (byte) i;
    }

    public void incFreq(int i) {
        byte[] bArr = this.mem;
        int i2 = this.pos + 1;
        bArr[i2] = (byte) (bArr[i2] + i);
    }

    public int getSuccessor() {
        return Raw.readIntLittleEndian(this.mem, this.pos + 2);
    }

    public void setSuccessor(int i) {
        Raw.writeIntLittleEndian(this.mem, this.pos + 2, i);
    }

    public void setSuccessor(PPMContext pPMContext) {
        setSuccessor(pPMContext.getAddress());
    }

    public void setValues(StateRef stateRef) {
        setSymbol(stateRef.getSymbol());
        setFreq(stateRef.getFreq());
        setSuccessor(stateRef.getSuccessor());
    }

    public void setValues(State state) {
        System.arraycopy(state.mem, state.pos, this.mem, this.pos, 6);
    }

    public State decAddress() {
        setAddress(this.pos - 6);
        return this;
    }

    public State incAddress() {
        setAddress(this.pos + 6);
        return this;
    }

    public String toString() {
        return "State[\n  pos=" + this.pos + "\n  size=" + 6 + "\n  symbol=" + getSymbol() + "\n  freq=" + getFreq() + "\n  successor=" + getSuccessor() + "\n]";
    }
}
