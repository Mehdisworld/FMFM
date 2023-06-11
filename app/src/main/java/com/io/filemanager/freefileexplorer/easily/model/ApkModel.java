package com.io.filemanager.freefileexplorer.easily.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.io.Serializable;
import java.util.Date;

public class ApkModel implements Serializable {
    String apkName;
    Drawable appIcon;
    Bitmap appIconBitmap = null;
    Date date = null;
    boolean isCheckboxVisible;
    boolean isFavorite = false;
    boolean isSelected;
    String packageName;
    String path;
    long size;

    public Bitmap getAppIconBitmap() {
        return this.appIconBitmap;
    }

    public void setAppIconBitmap(Bitmap bitmap) {
        this.appIconBitmap = bitmap;
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

    public String getApkName() {
        return this.apkName;
    }

    public void setApkName(String str) {
        this.apkName = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable drawable) {
        this.appIcon = drawable;
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
