package com.io.filemanager.freefileexplorer.easily.event;

public class ImageDeleteEvent {
    String deletePath;

    public ImageDeleteEvent(String str) {
        this.deletePath = str;
    }

    public String getDeletePath() {
        return this.deletePath;
    }

    public void setDeletePath(String str) {
        this.deletePath = str;
    }
}
