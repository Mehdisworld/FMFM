package com.io.filemanager.freefileexplorer.easily.event;

import java.util.ArrayList;

public class DocumentDeleteEvent {
    ArrayList<String> deleteList = new ArrayList<>();
    String type;

    public DocumentDeleteEvent(String str, ArrayList<String> arrayList) {
        this.deleteList = arrayList;
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public ArrayList<String> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }
}
