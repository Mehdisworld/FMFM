package com.io.filemanager.freefileexplorer.easily.model;

public class DocumentModel {
    String appType;
    long dateValue;
    boolean isCheckboxVisible = false;
    boolean isFavorite = false;
    boolean isSelected;
    String name;
    String path;
    long size;

    public long getDateValue() {
        return this.dateValue;
    }

    public void setDateValue(long j) {
        this.dateValue = j;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    public boolean isCheckboxVisible() {
        return this.isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean z) {
        this.isCheckboxVisible = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getAppType() {
        return this.appType;
    }

    public void setAppType(String str) {
        this.appType = str;
    }
}
