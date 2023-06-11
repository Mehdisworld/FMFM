package com.io.filemanager.freefileexplorer.easily.model;

import java.util.ArrayList;

public class PhotoHeader {
    String folderPath;
    boolean isSelect = false;
    ArrayList<PhotoData> photoList = new ArrayList<>();
    String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public ArrayList<PhotoData> getPhotoList() {
        return this.photoList;
    }

    public void setPhotoList(ArrayList<PhotoData> arrayList) {
        this.photoList = arrayList;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String str) {
        this.folderPath = str;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
