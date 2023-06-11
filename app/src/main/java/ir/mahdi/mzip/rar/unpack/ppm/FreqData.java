package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class FreqData extends Pointer {
    public static final int size = 6;

    public FreqData(byte[] bArr) {
        super(bArr);
    }

    public FreqData init(byte[] bArr) {
        this.mem = bArr;
        this.pos = 0;
        return this;
    }

    public int getSummFreq() {
        return Raw.readShortLittleEndian(this.mem, this.pos) & -1;
    }

    public void setSummFreq(int i) {
        Raw.writeShortLittleEndian(this.mem, this.pos, (short) i);
    }

    public void incSummFreq(int i) {
        Raw.incShortLittleEndian(this.mem, this.pos, i);
    }

    public int getStats() {
        return Raw.readIntLittleEndian(this.mem, this.pos + 2);
    }

    public void setStats(int i) {
        Raw.writeIntLittleEndian(this.mem, this.pos + 2, i);
    }

    public void setStats(State state) {
        setStats(state.getAddress());
    }

    public String toString() {
        return "FreqData[\n  pos=" + this.pos + "\n  size=" + 6 + "\n  summFreq=" + getSummFreq() + "\n  stats=" + getStats() + "\n]";
    }
}
