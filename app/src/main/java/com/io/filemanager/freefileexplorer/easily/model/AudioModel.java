package com.io.filemanager.freefileexplorer.easily.model;

import android.graphics.Bitmap;

public class AudioModel {
    String album;
    long albumId;
    String artist;
    Bitmap bitmap;
    long dateValue;
    boolean isCheckboxVisible = false;
    boolean isFavorite = false;
    boolean isPlay;
    boolean isSelected;
    String name;
    String path;
    long size;

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long j) {
        this.albumId = j;
    }

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

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public boolean isCheckboxVisible() {
        return this.isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean z) {
        this.isCheckboxVisible = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String str) {
        this.album = str;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String str) {
        this.artist = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
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
}
