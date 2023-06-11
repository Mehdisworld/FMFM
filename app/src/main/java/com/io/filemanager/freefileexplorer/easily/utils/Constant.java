package com.io.filemanager.freefileexplorer.easily.utils;

import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static boolean OPEN_ADS = true;
    public static final String PREF_SDCARD_TREE_URI = "my_file_manager_sd_card_tree_uri";
    public static final String SHARED_PREFS = "my_file_manager";
    public static final String SHARED_PREFS_DIR_LIST_GRID = "my_file_manager_dir_list_grid";
    public static final String SHARED_PREFS_DOCUMENT_LIST_GRID = "my_file_manager_document_list_grid";
    public static final String SHARED_PREFS_FAVOURITE_LIST = "my_file_manager_favourite_list";
    public static final String SHARED_PREFS_RATE_US = "my_file_manager_rate_us";
    public static final String SHARED_PREFS_SHOW_HIDDEN_FILE = "my_file_manager_hidden_file";
    public static final String SHARED_PREFS_SORT_FILE_LIST = "my_file_manager_sort_file_list";
    public static final int STICKER_BTN_HALF_SIZE = 30;
    public static ArrayList<String> arrayListFilePaths = new ArrayList<>();
    public static String darkGallery = "#000000";
    public static List<PhotoData> displayImageList = new ArrayList();
    public static List<PhotoData> displayVideoList = new ArrayList();
    public static boolean isAdLoad = false;
    public static boolean isAdLoadFailed = false;
    public static boolean isAdLoadProcessing = false;
    public static boolean isCopyData = false;
    public static boolean isFileFromSdCard = false;
    public static boolean isOpenImage = false;
    public static boolean isShowFirstTimeOptionADs = false;
    public static boolean isbackImage = false;
    public static String lightGallery = "#5387ED";
    public static ArrayList<File> pastList = new ArrayList<>();
    public static String progressBarDarkGallery = "#FFFFFF";
    public static String progressBarLightGallery = "#5387ED";
    public static String storagePath;
    public static String webViewLink = "#0782C1;";
    public static String webViewLinkDark = "#5387ED;";
    public static String webViewText = "#8b8b8b;";
    public static String webViewTextDark = "#FFFFFF;";


}
