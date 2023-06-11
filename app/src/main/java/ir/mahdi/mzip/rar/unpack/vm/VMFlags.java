package ir.mahdi.mzip.rar.unpack.vm;

public enum VMFlags {
    VM_FC(1),
    VM_FZ(2),
    VM_FS(Integer.MIN_VALUE);
    
    private int flag;

    private VMFlags(int i) {
        this.flag = i;
    }

    public static VMFlags findFlag(int i) {
        VMFlags vMFlags = VM_FC;
        if (vMFlags.equals(i)) {
            return vMFlags;
        }
        VMFlags vMFlags2 = VM_FS;
        if (vMFlags2.equals(i)) {
            return vMFlags2;
        }
        VMFlags vMFlags3 = VM_FZ;
        if (vMFlags3.equals(i)) {
            return vMFlags3;
        }
        return null;
    }

    public boolean equals(int i) {
        return this.flag == i;
    }

    public int getFlag() {
        return this.flag;
    }
}
