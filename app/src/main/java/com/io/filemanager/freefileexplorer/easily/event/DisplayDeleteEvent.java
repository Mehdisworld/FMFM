package com.io.filemanager.freefileexplorer.easily.event;

import java.util.ArrayList;

public class DisplayDeleteEvent {
    ArrayList<String> deleteList = new ArrayList<>();

    public DisplayDeleteEvent(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }

    public ArrayList<String> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }
}
