package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;

public final class RandomAccessStream extends InputStream {
    private static final int BLOCK_MASK = 511;
    private static final int BLOCK_SHIFT = 9;
    private static final int BLOCK_SIZE = 512;
    private Vector data;
    private boolean foundEOS;
    private int length;
    private long pointer;
    private RandomAccessFile ras;
    private InputStream src;

    public RandomAccessStream(InputStream inputStream) {
        this.pointer = 0;
        this.data = new Vector();
        this.length = 0;
        this.foundEOS = false;
        this.src = inputStream;
    }

    public RandomAccessStream(RandomAccessFile randomAccessFile) {
        this.ras = randomAccessFile;
    }

    public int getFilePointer() throws IOException {
        long j;
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            j = randomAccessFile.getFilePointer();
        } else {
            j = this.pointer;
        }
        return (int) j;
    }

    public long getLongFilePointer() throws IOException {
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            return randomAccessFile.getFilePointer();
        }
        return this.pointer;
    }

    public int read() throws IOException {
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            return randomAccessFile.read();
        }
        long j = this.pointer + 1;
        if (readUntil(j) < j) {
            return -1;
        }
        long j2 = this.pointer;
        long j3 = 1 + j2;
        this.pointer = j3;
        return ((byte[]) this.data.elementAt((int) (j3 >> 9)))[(int) (j2 & 511)] & -1;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        bArr.getClass();
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            return randomAccessFile.read(bArr, i, i2);
        }
        if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            long readUntil = readUntil(this.pointer + ((long) i2));
            long j = this.pointer;
            if (readUntil <= j) {
                return -1;
            }
            int min = Math.min(i2, 512 - ((int) (j & 511)));
            System.arraycopy((byte[]) this.data.elementAt((int) (j >> 9)), (int) (511 & this.pointer), bArr, i, min);
            this.pointer += (long) min;
            return min;
        }
    }

    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, bArr.length);
    }

    public final void readFully(byte[] bArr, int i) throws IOException {
        int read;
        int i2 = 0;
        do {
            read = read(bArr, i2, i - i2);
            if (read < 0 || (i2 = i2 + read) >= i) {
            }
            read = read(bArr, i2, i - i2);
            return;
        } while ((i2 = i2 + read) >= i);
    }

    private long readUntil(long j) throws IOException {
        int i;
        int i2 = this.length;
        long j2 = (long) i2;
        if (j < j2) {
            return j;
        }
        if (this.foundEOS) {
            return j2;
        }
        int i3 = (int) (j >> 9);
        int i4 = i2 >> 9;
        loop0:
        while (true) {
            if (i4 > i3) {
                i = this.length;
                break;
            }
            int i5 = 512;
            byte[] bArr = new byte[512];
            this.data.addElement(bArr);
            int i6 = 0;
            while (i5 > 0) {
                int read = this.src.read(bArr, i6, i5);
                if (read == -1) {
                    this.foundEOS = true;
                    i = this.length;
                    break loop0;
                }
                i6 += read;
                i5 -= read;
                this.length += read;
            }
            i4++;
        }
        return (long) i;
    }

    public void seek(long j) throws IOException {
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            randomAccessFile.seek(j);
        } else if (j < 0) {
            this.pointer = 0;
        } else {
            this.pointer = j;
        }
    }


    public final int readInt() throws IOException {
        int read = read();
        int read2 = read();
        int read3 = read();
        int read4 = read();
        if ((read | read2 | read3 | read4) >= 0) {
            return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
        }
        throw new EOFException();
    }


    public final short readShort() throws IOException {
        int read = read();
        int read2 = read();
        if ((read | read2) >= 0) {
            return (short) ((read << 8) + read2);
        }
        throw new EOFException();
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void close() throws IOException {
        RandomAccessFile randomAccessFile = this.ras;
        if (randomAccessFile != null) {
            randomAccessFile.close();
            return;
        }
        this.data.removeAllElements();
        this.src.close();
    }
}
