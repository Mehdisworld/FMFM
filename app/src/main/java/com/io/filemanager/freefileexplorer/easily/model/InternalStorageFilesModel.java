package com.io.filemanager.freefileexplorer.easily.model;

import java.util.Date;

public class InternalStorageFilesModel {
    Date date = null;
    private String fileName;
    private String filePath;
    boolean isCheckboxVisible = false;
    private boolean isDir;
    boolean isFavorite = false;
    public boolean isPlay;
    private boolean isSelected;
    private String mineType;
    long size;

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

    public boolean isPlay() {
        return this.isPlay;
    }

    public void setPlay(boolean z) {
        this.isPlay = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public boolean isDir() {
        return this.isDir;
    }

    public void setDir(boolean z) {
        this.isDir = z;
    }

    public String getMineType() {
        return this.mineType;
    }

    public void setMineType(String str) {
        this.mineType = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }
}
