package ir.mahdi.mzip.rar.io;

import java.io.IOException;
import java.io.InputStream;

public class ReadOnlyAccessInputStream extends InputStream {
    private long curPos;
    private final long endPos;
    private IReadOnlyAccess file;
    private final long startPos;

    public ReadOnlyAccessInputStream(IReadOnlyAccess iReadOnlyAccess, long j, long j2) throws IOException {
        this.file = iReadOnlyAccess;
        this.startPos = j;
        this.curPos = j;
        this.endPos = j2;
        iReadOnlyAccess.setPosition(j);
    }

    public int read() throws IOException {
        if (this.curPos == this.endPos) {
            return -1;
        }
        int read = this.file.read();
        this.curPos++;
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (i2 == 0) {
            return 0;
        }
        long j = this.curPos;
        long j2 = this.endPos;
        if (j == j2) {
            return -1;
        }
        int read = this.file.read(bArr, i, (int) Math.min((long) i2, j2 - j));
        this.curPos += (long) read;
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }
}
