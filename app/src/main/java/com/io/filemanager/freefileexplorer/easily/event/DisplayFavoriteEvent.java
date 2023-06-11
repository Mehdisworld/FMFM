package com.io.filemanager.freefileexplorer.easily.event;

import java.util.ArrayList;

public class DisplayFavoriteEvent {
    ArrayList<String> favList = new ArrayList<>();
    String filePath;
    boolean isFavorite = false;

    public DisplayFavoriteEvent(ArrayList<String> arrayList, boolean z) {
        this.favList = arrayList;
        this.isFavorite = z;
    }

    public DisplayFavoriteEvent(String str, boolean z) {
        this.filePath = str;
        this.isFavorite = z;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public ArrayList<String> getFavList() {
        return this.favList;
    }

    public void setFavList(ArrayList<String> arrayList) {
        this.favList = arrayList;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(boolean z) {
        this.isFavorite = z;
    }
}
