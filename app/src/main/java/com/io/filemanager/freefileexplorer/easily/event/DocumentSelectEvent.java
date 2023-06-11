package com.io.filemanager.freefileexplorer.easily.event;

public class DocumentSelectEvent {
    boolean isSelected;
    String type;

    public DocumentSelectEvent(String str, boolean z) {
        this.isSelected = z;
        this.type = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
