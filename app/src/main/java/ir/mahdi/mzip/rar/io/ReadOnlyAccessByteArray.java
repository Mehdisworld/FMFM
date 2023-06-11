package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

public class ReadOnlyAccessByteArray implements IReadOnlyAccess {
    private byte[] file;
    private int positionInFile = 0;

    public void close() throws IOException {
    }

    public ReadOnlyAccessByteArray(byte[] bArr) {
        Objects.requireNonNull(bArr, "file must not be null!!");
        this.file = bArr;
    }

    public long getPosition() throws IOException {
        return (long) this.positionInFile;
    }

    public void setPosition(long j) throws IOException {
        if (j >= ((long) this.file.length) || j < 0) {
            throw new EOFException();
        }
        this.positionInFile = (int) j;
    }

    public int read() throws IOException {
        byte[] bArr = this.file;
        int i = this.positionInFile;
        this.positionInFile = i + 1;
        return bArr[i];
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int min = Math.min(i2, this.file.length - this.positionInFile);
        System.arraycopy(this.file, this.positionInFile, bArr, i, min);
        this.positionInFile += min;
        return min;
    }

    public int readFully(byte[] bArr, int i) throws IOException {
        Objects.requireNonNull(bArr, "buffer must not be null");
        if (i != 0) {
            int min = Math.min(i, (this.file.length - this.positionInFile) - 1);
            System.arraycopy(this.file, this.positionInFile, bArr, 0, min);
            this.positionInFile += min;
            return min;
        }
        throw new IllegalArgumentException("cannot read 0 bytes ;-)");
    }
}
