package com.io.filemanager.freefileexplorer.easily.event;

import java.io.File;
import java.util.ArrayList;

public class CopyMoveEvent {
    ArrayList<File> copyMoveList = new ArrayList<>();
    ArrayList<String> deleteList = new ArrayList<>();
    int type = 0;

    public CopyMoveEvent(ArrayList<File> arrayList, int i, ArrayList<String> arrayList2) {
        this.deleteList = arrayList2;
        this.copyMoveList = arrayList;
        this.type = i;
    }

    public CopyMoveEvent(String str) {
        ArrayList<File> arrayList = new ArrayList<>();
        arrayList.add(new File(str));
        this.deleteList = new ArrayList<>();
        this.copyMoveList = arrayList;
        this.type = 3;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public ArrayList<File> getCopyMoveList() {
        return this.copyMoveList;
    }

    public void setCopyMoveList(ArrayList<File> arrayList) {
        this.copyMoveList = arrayList;
    }

    public ArrayList<String> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }
}
