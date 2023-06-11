package com.io.filemanager.freefileexplorer.easily.model;

import java.util.Date;

public class PhotoData {
    Date date = null;
    long dateValue;
    String duration;
    String fileName;
    String filePath;
    String fileSize;
    public String folderName = "";
    boolean isCheckboxVisible = false;
    boolean isFavorite = false;
    boolean isSelected;
    long size;
    String thumbnails;

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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public boolean isCheckboxVisible() {
        return this.isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean z) {
        this.isCheckboxVisible = z;
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

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String str) {
        this.fileSize = str;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }

    public String getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(String str) {
        this.thumbnails = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }
}
