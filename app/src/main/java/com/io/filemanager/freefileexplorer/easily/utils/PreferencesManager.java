package com.io.filemanager.freefileexplorer.easily.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {
    public static void saveToDirList_Grid(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putBoolean(Constant.SHARED_PREFS_DIR_LIST_GRID, z);
        edit.commit();
    }

    public static boolean getDirList_Grid(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getBoolean(Constant.SHARED_PREFS_DIR_LIST_GRID, false);
    }

    public static void saveToDocList_Grid(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putBoolean(Constant.SHARED_PREFS_DOCUMENT_LIST_GRID, z);
        edit.commit();
    }

    public static boolean getDocList_Grid(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getBoolean(Constant.SHARED_PREFS_DOCUMENT_LIST_GRID, false);
    }

    public static void saveToShowHidden(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putBoolean(Constant.SHARED_PREFS_SHOW_HIDDEN_FILE, z);
        edit.commit();
    }

    public static boolean getShowHidden(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getBoolean(Constant.SHARED_PREFS_SHOW_HIDDEN_FILE, false);
    }

    public static void saveToSortType(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putInt(Constant.SHARED_PREFS_SORT_FILE_LIST, i);
        edit.commit();
    }

    public static int getSortType(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getInt(Constant.SHARED_PREFS_SORT_FILE_LIST, 1);
    }

    public static void setRateUs(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putBoolean(Constant.SHARED_PREFS_RATE_US, z);
        edit.commit();
    }

    public static boolean getRate(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getBoolean(Constant.SHARED_PREFS_RATE_US, false);
    }

    public static String getSDCardTreeUri(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getString(Constant.PREF_SDCARD_TREE_URI, "");
    }

    public static void setSDCardTreeUri(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putString(Constant.PREF_SDCARD_TREE_URI, str);
        edit.apply();
    }

    public static ArrayList<String> getFavouriteList(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            return (ArrayList) new Gson().fromJson(context.getSharedPreferences(Constant.SHARED_PREFS, 0).getString(Constant.SHARED_PREFS_FAVOURITE_LIST, ""), new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    public static void setFavouriteList(Context context, ArrayList<String> arrayList) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
            edit.putString(Constant.SHARED_PREFS_FAVOURITE_LIST, new Gson().toJson((Object) arrayList));
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
