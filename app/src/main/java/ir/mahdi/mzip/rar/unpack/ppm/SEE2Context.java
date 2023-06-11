package ir.mahdi.mzip.rar.unpack.ppm;

public class SEE2Context {
    public static final int size = 4;
    private int count;
    private int shift;
    private int summ;

    public void init(int i) {
        this.shift = 3;
        this.summ = (i << 3) & 65535;
        this.count = 4;
    }

    public int getMean() {
        int i = this.summ;
        int i2 = i >>> this.shift;
        this.summ = i - i2;
        return i2 + (i2 == 0 ? 1 : 0);
    }

    public void update() {
        int i = this.shift;
        if (i < 7) {
            int i2 = this.count - 1;
            this.count = i2;
            if (i2 == 0) {
                int i3 = this.summ;
                this.summ = i3 + i3;
                this.shift = i + 1;
                this.count = 3 << i;
            }
        }
        this.summ &= 65535;
        this.count &= 255;
        this.shift &= 255;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i & 255;
    }

    public int getShift() {
        return this.shift;
    }

    public void setShift(int i) {
        this.shift = i & 255;
    }

    public int getSumm() {
        return this.summ;
    }

    public void setSumm(int i) {
        this.summ = i & 65535;
    }

    public void incSumm(int i) {
        setSumm(getSumm() + i);
    }

    public String toString() {
        return "SEE2Context[\n  size=4\n  summ=" + this.summ + "\n  shift=" + this.shift + "\n  count=" + this.count + "\n]";
    }
}
