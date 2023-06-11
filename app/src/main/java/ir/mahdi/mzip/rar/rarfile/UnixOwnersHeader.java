package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class UnixOwnersHeader extends SubBlockHeader {
    private String group;
    private int groupNameSize;
    private String owner;
    private int ownerNameSize;

    public UnixOwnersHeader(SubBlockHeader subBlockHeader, byte[] bArr) {
        super(subBlockHeader);
        this.ownerNameSize = Raw.readShortLittleEndian(bArr, 0) & -1;
        this.groupNameSize = Raw.readShortLittleEndian(bArr, 2) & -1;
        int i = this.ownerNameSize;
        if (i + 4 < bArr.length) {
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 4, bArr2, 0, i);
            this.owner = new String(bArr2);
        }
        int i2 = this.ownerNameSize + 4;
        int i3 = this.groupNameSize;
        if (i2 + i3 < bArr.length) {
            byte[] bArr3 = new byte[i3];
            System.arraycopy(bArr, i2, bArr3, 0, i3);
            this.group = new String(bArr3);
        }
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public int getGroupNameSize() {
        return this.groupNameSize;
    }

    public void setGroupNameSize(int i) {
        this.groupNameSize = i;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String str) {
        this.owner = str;
    }

    public int getOwnerNameSize() {
        return this.ownerNameSize;
    }

    public void setOwnerNameSize(int i) {
        this.ownerNameSize = i;
    }

    public void print() {
        super.print();
    }
}
