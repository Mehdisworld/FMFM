package com.io.filemanager.freefileexplorer.easily.event;

public class DocumentCloseEvent {
    String type;

    public DocumentCloseEvent(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
