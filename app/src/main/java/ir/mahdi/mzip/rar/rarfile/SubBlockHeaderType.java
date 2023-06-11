package ir.mahdi.mzip.rar.rarfile;

public enum SubBlockHeaderType {
    EA_HEAD( 256),
    UO_HEAD( 257),
    MAC_HEAD( 258),
    BEEA_HEAD( 259),
    NTACL_HEAD( 260),
    STREAM_HEAD( 261);
    
    private short subblocktype;

    private SubBlockHeaderType(short s) {
        this.subblocktype = s;
    }

    private SubBlockHeaderType(int i) {
    }

    public static SubBlockHeaderType findSubblockHeaderType(short s) {
        SubBlockHeaderType subBlockHeaderType = EA_HEAD;
        if (subBlockHeaderType.equals(s)) {
            return subBlockHeaderType;
        }
        SubBlockHeaderType subBlockHeaderType2 = UO_HEAD;
        if (subBlockHeaderType2.equals(s)) {
            return subBlockHeaderType2;
        }
        SubBlockHeaderType subBlockHeaderType3 = MAC_HEAD;
        if (subBlockHeaderType3.equals(s)) {
            return subBlockHeaderType3;
        }
        SubBlockHeaderType subBlockHeaderType4 = BEEA_HEAD;
        if (subBlockHeaderType4.equals(s)) {
            return subBlockHeaderType4;
        }
        SubBlockHeaderType subBlockHeaderType5 = NTACL_HEAD;
        if (subBlockHeaderType5.equals(s)) {
            return subBlockHeaderType5;
        }
        SubBlockHeaderType subBlockHeaderType6 = STREAM_HEAD;
        if (subBlockHeaderType6.equals(s)) {
            return subBlockHeaderType6;
        }
        return null;
    }

    public boolean equals(short s) {
        return this.subblocktype == s;
    }

    public short getSubblocktype() {
        return this.subblocktype;
    }
}
