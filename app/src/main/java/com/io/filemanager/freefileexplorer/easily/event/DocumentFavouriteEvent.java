package com.io.filemanager.freefileexplorer.easily.event;

public class DocumentFavouriteEvent {
    boolean isFavourite;
    String type;

    public DocumentFavouriteEvent(String str, boolean z) {
        this.isFavourite = z;
        this.type = str;
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
