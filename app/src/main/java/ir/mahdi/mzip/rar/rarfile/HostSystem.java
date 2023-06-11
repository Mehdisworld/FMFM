package ir.mahdi.mzip.rar.rarfile;

public enum HostSystem {
    msdos((byte) 0),
    os2((byte) 1),
    win32((byte) 2),
    unix((byte) 3),
    macos((byte) 4),
    beos((byte) 5);
    
    private byte hostByte;

    private HostSystem(byte b) {
        this.hostByte = b;
    }

    public static HostSystem findHostSystem(byte b) {
        HostSystem hostSystem = msdos;
        if (hostSystem.equals(b)) {
            return hostSystem;
        }
        HostSystem hostSystem2 = os2;
        if (hostSystem2.equals(b)) {
            return hostSystem2;
        }
        HostSystem hostSystem3 = win32;
        if (hostSystem3.equals(b)) {
            return hostSystem3;
        }
        HostSystem hostSystem4 = unix;
        if (hostSystem4.equals(b)) {
            return hostSystem4;
        }
        HostSystem hostSystem5 = macos;
        if (hostSystem5.equals(b)) {
            return hostSystem5;
        }
        HostSystem hostSystem6 = beos;
        if (hostSystem6.equals(b)) {
            return hostSystem6;
        }
        return null;
    }

    public boolean equals(byte b) {
        return this.hostByte == b;
    }

    public byte getHostByte() {
        return this.hostByte;
    }
}
