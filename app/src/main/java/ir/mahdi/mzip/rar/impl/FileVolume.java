package ir.mahdi.mzip.rar.impl;

import ir.mahdi.mzip.rar.Archive;
import ir.mahdi.mzip.rar.Volume;
import ir.mahdi.mzip.rar.io.IReadOnlyAccess;
import ir.mahdi.mzip.rar.io.ReadOnlyAccessFile;
import java.io.File;
import java.io.IOException;

public class FileVolume implements Volume {
    private final Archive archive;
    private final File file;

    public FileVolume(Archive archive2, File file2) {
        this.archive = archive2;
        this.file = file2;
    }

    public IReadOnlyAccess getReadOnlyAccess() throws IOException {
        return new ReadOnlyAccessFile(this.file);
    }

    public long getLength() {
        return this.file.length();
    }

    public Archive getArchive() {
        return this.archive;
    }

    public File getFile() {
        return this.file;
    }
}
