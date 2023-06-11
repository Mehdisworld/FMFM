package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MVTest {

    public static void main(String[] strArr) {
        Archive archive = null;
        try {
            archive = new Archive((VolumeManager) new FileVolumeManager(new File("/home/rogiel/fs/home/ae721273-eade-45e7-8112-d14115ebae56/Village People - Y.M.C.A.mp3.part1.rar")));
        } catch (RarException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (archive != null) {
            archive.getMainHeader().print();
            while (true) {
                FileHeader nextFileHeader = archive.nextFileHeader();
                if (nextFileHeader != null) {
                    try {
                        File file = new File("/home/rogiel/fs/test/" + nextFileHeader.getFileNameString().trim());
                        System.out.println(file.getAbsolutePath());
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        archive.extractFile(nextFileHeader, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e3) {
                        e3.printStackTrace();
                    } catch (RarException e4) {
                        e4.printStackTrace();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        } else {
            return;
        }

    }
}
