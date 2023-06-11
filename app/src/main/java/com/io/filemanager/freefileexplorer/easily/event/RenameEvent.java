package com.io.filemanager.freefileexplorer.easily.event;

import java.io.File;

public class RenameEvent {
    File newFile;
    File oldFile;

    public RenameEvent(File file, File file2) {
        this.oldFile = file;
        this.newFile = file2;
    }

    public File getOldFile() {
        return this.oldFile;
    }

    public void setOldFile(File file) {
        this.oldFile = file;
    }

    public File getNewFile() {
        return this.newFile;
    }

    public void setNewFile(File file) {
        this.newFile = file;
    }
}
