package com.io.filemanager.freefileexplorer.easily.event;

import java.util.ArrayList;

public class DocumentFavouriteUpdateEvent {
    ArrayList<String> fileList = new ArrayList<>();
    boolean isFavourite;
    String type;

    public DocumentFavouriteUpdateEvent(String str, boolean z, ArrayList<String> arrayList) {
        this.isFavourite = z;
        this.type = str;
        this.fileList = arrayList;
    }

    public ArrayList<String> getFileList() {
        return this.fileList;
    }

    public void setFileList(ArrayList<String> arrayList) {
        this.fileList = arrayList;
    }

    public boolean isFavourite() {
        return this.isFavourite;
    }

    public void setFavourite(boolean z) {
        this.isFavourite = z;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
