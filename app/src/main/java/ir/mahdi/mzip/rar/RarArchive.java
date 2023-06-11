package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RarArchive {
    public static void extractArchive(String str, String str2) {
        if (str == null || str2 == null) {
            throw new RuntimeException("archive and destination must me set");
        }
        File file = new File(str);
        if (file.exists()) {
            File file2 = new File(str2);
            if (!file2.exists() || !file2.isDirectory()) {
                throw new RuntimeException("the destination must exist and point to a directory: " + str2);
            }
            extractArchive(file, file2);
            return;
        }
        throw new RuntimeException("the archive does not exit: " + str);
    }

    public static void extractArchive(File file, File file2) {
        Archive archive;
        try {
            archive = new Archive(file);
        } catch (RarException | IOException unused) {
            archive = null;
        }
        if (archive != null && !archive.isEncrypted()) {
            while (true) {
                FileHeader nextFileHeader = archive.nextFileHeader();
                if (nextFileHeader == null) {
                    return;
                }
                if (!nextFileHeader.isEncrypted()) {
                    try {
                        if (nextFileHeader.isDirectory()) {
                            createDirectory(nextFileHeader, file2);
                        } else {
                            FileOutputStream fileOutputStream = new FileOutputStream(createFile(nextFileHeader, file2));
                            archive.extractFile(nextFileHeader, fileOutputStream);
                            fileOutputStream.close();
                        }
                    } catch (RarException | IOException unused2) {
                    }
                }
            }
        }
    }

    private static File createFile(FileHeader fileHeader, File file) {
        String str;
        if (!fileHeader.isFileHeader() || !fileHeader.isUnicode()) {
            str = fileHeader.getFileNameString();
        } else {
            str = fileHeader.getFileNameW();
        }
        File file2 = new File(file, str);
        if (file2.exists()) {
            return file2;
        }
        try {
            return makeFile(file, str);
        } catch (IOException unused) {
            return file2;
        }
    }

    private static File makeFile(File file, String str) throws IOException {
        String[] split = str.split("\\\\");
        if (split == null) {
            return null;
        }
        int length = split.length;
        if (length == 1) {
            return new File(file, str);
        }
        if (length <= 1) {
            return null;
        }
        String str2 = "";
        for (int i = 0; i < split.length - 1; i++) {
            str2 = str2 + File.separator + split[i];
            new File(file, str2).mkdir();
        }
        File file2 = new File(file, str2 + File.separator + split[split.length - 1]);
        file2.createNewFile();
        return file2;
    }

    private static void createDirectory(FileHeader fileHeader, File file) {
        if (!fileHeader.isDirectory() || !fileHeader.isUnicode()) {
            if (fileHeader.isDirectory() && !fileHeader.isUnicode() && !new File(file, fileHeader.getFileNameString()).exists()) {
                makeDirectory(file, fileHeader.getFileNameString());
            }
        } else if (!new File(file, fileHeader.getFileNameW()).exists()) {
            makeDirectory(file, fileHeader.getFileNameW());
        }
    }

    private static void makeDirectory(File file, String str) {
        String[] split = str.split("\\\\");
        if (split != null) {
            String str2 = "";
            for (String str3 : split) {
                str2 = str2 + File.separator + str3;
                new File(file, str2).mkdir();
            }
        }
    }
}
