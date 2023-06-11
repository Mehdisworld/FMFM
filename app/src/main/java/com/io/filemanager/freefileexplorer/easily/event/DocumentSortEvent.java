package com.io.filemanager.freefileexplorer.easily.event;

public class DocumentSortEvent {
    int type;

    public DocumentSortEvent(int i) {
        this.type = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
