package ir.mahdi.mzip.rar.unpack.ppm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class AnalyzeHeapDump {

    public static void main(String[] strArr) throws IOException {
        BufferedInputStream bufferedInputStream;
        File file = new File("P:\\test\\heapdumpc");
        File file2 = new File("P:\\test\\heapdumpj");
        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
        } else if (!file2.exists()) {
            System.err.println("File not found: " + file2.getAbsolutePath());
        } else {
            long length = file.length();
            long length2 = file2.length();
            if (length != length2) {
                System.out.println("File size mismatch");
                System.out.println("clen = " + length);
                System.out.println("jlen = " + length2);
            }
            long min = Math.min(length, length2);
            BufferedInputStream bufferedInputStream2 = null;
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 262144);
                try {
                    BufferedInputStream bufferedInputStream3 = new BufferedInputStream(new FileInputStream(file2), 262144);
                    long j = 0;
                    long j2 = 0;
                    boolean z = true;
                    boolean z2 = false;
                    while (j < min) {
                        if (bufferedInputStream.read() != bufferedInputStream3.read()) {
                            if (z) {
                                j2 = j;
                                z = false;
                                z2 = true;
                            }
                        } else if (!z) {
                            printMismatch(j2, j);
                            z = true;
                        }
                        j++;
                    }
                    if (!z) {
                        printMismatch(j2, j);
                    }
                    if (!z2) {
                        System.out.println("Files are identical");
                    }
                    System.out.println("Done");
                    bufferedInputStream.close();
                    bufferedInputStream3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Throwable unused) {
                    bufferedInputStream.close();
                    bufferedInputStream2.close();
                }
            } catch (IOException e3) {
                //e = e3;
                bufferedInputStream = bufferedInputStream2;
                e3.printStackTrace();
                bufferedInputStream.close();
                bufferedInputStream2.close();
            } catch (Throwable unused2) {
                bufferedInputStream = bufferedInputStream2;
                bufferedInputStream.close();
                bufferedInputStream2.close();
            }
        }
    }

    private static void printMismatch(long j, long j2) {
        PrintStream printStream = System.out;
        printStream.println("Mismatch: off=" + j + "(0x" + Long.toHexString(j) + "), len=" + (j2 - j));
    }
}
