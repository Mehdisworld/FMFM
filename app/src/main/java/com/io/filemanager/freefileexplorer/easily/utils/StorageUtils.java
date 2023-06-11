package com.io.filemanager.freefileexplorer.easily.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import com.io.filemanager.freefileexplorer.easily.R;
/*import com.safedk.android.analytics.AppLovinBridge;
import com.safedk.android.analytics.brandsafety.creatives.infos.CreativeInfo;
import com.safedk.android.analytics.events.RedirectEvent;
import com.safedk.android.utils.Logger;*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.FileUtils;

public class StorageUtils {
    public static final long A_DAY = 86400000;
    public static final int REQUEST_SDCARD_WRITE_PERMISSION = 300;
    public static final long USAGE_TIME_MIX = 500;

    public static void safedk_Activity_startActivityForResult_206f42f0b65887e835d87ee52d14d221(Activity p0, Intent p1, int p2) {
        if (p1 != null) {
            p0.startActivityForResult(p1, p2);
        }
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= 22;
    }

    public static boolean openable(PackageManager packageManager, String str) {
        return packageManager.getLaunchIntentForPackage(str) != null;
    }

    public static Drawable parsePackageIcon(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationIcon(str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getResources().getDrawable(R.drawable.ic_app_icon);
        }
    }

    public static Drawable getAppIcon(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(str, 0);
            packageArchiveInfo.applicationInfo.sourceDir = str;
            packageArchiveInfo.applicationInfo.publicSourceDir = str;
            return packageArchiveInfo.applicationInfo.loadIcon(packageManager);
        } catch (Exception e) {
            e.printStackTrace();
            return context.getResources().getDrawable(R.drawable.ic_app_icon);
        }
    }

    public static String isMyLauncherDefault(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        return packageManager.resolveActivity(intent, 65536).activityInfo.packageName;
    }

    public static boolean checkWriteSettings(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.System.canWrite(context);
        }
        return ContextCompat.checkSelfPermission(context, "android.permission.WRITE_SETTINGS") == 0;
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ArrayList arrayList = new ArrayList();
        if (hasOreo()) {
            for (PackageInfo next : context.getPackageManager().getInstalledPackages(getAppListFlag())) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo(next.packageName, next.applicationInfo.uid, (String[]) null);
                runningAppProcessInfo.uid = next.applicationInfo.uid;
                runningAppProcessInfo.importance = 400;
                arrayList.add(runningAppProcessInfo);
            }
            return arrayList;
        } else if (!hasNougat()) {
            return hasLollipopMR1() ? arrayList : activityManager.getRunningAppProcesses();
        } else {
            String str = "";
            for (ActivityManager.RunningServiceInfo next2 : activityManager.getRunningServices(1000)) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 = new ActivityManager.RunningAppProcessInfo(next2.process, next2.pid, (String[]) null);
                runningAppProcessInfo2.uid = next2.uid;
                runningAppProcessInfo2.importance = next2.foreground ? 100 : 400;
                if (!str.equals(next2.process)) {
                    str = next2.process;
                    arrayList.add(runningAppProcessInfo2);
                }
            }
            return arrayList;
        }
    }

    private static int getAppListFlag() {
        hasNougat();
        return 8192;
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman(long j) {
        if (j < 1024) {
            return floatForm((double) j) + " B";
        } else if (j >= 1024 && j < 1048576) {
            StringBuilder sb = new StringBuilder();
            double d = (double) j;
            Double.isNaN(d);
            Double.isNaN(1024.0d);
            Double.isNaN(d);
            sb.append(floatForm(d / 1024.0d));
            sb.append(" KB");
            return sb.toString();
        } else if (j >= 1048576 && j < FileUtils.ONE_GB) {
            StringBuilder sb2 = new StringBuilder();
            double d2 = (double) j;
            Double.isNaN(d2);
            Double.isNaN(1048576.0d);
            Double.isNaN(d2);
            sb2.append(floatForm(d2 / 1048576.0d));
            sb2.append(" MB");
            return sb2.toString();
        } else if (j >= FileUtils.ONE_GB && j < FileUtils.ONE_TB) {
            StringBuilder sb3 = new StringBuilder();
            double d3 = (double) j;
            Double.isNaN(d3);
            Double.isNaN(1.073741824E9d);
            Double.isNaN(d3);
            sb3.append(floatForm(d3 / 1.073741824E9d));
            sb3.append(" GB");
            return sb3.toString();
        } else if (j >= FileUtils.ONE_TB && j < FileUtils.ONE_PB) {
            StringBuilder sb4 = new StringBuilder();
            double d4 = (double) j;
            Double.isNaN(d4);
            Double.isNaN(1.099511627776E12d);
            Double.isNaN(d4);
            sb4.append(floatForm(d4 / 1.099511627776E12d));
            sb4.append(" TB");
            return sb4.toString();
        } else if (j >= FileUtils.ONE_PB && j < 1152921504606846976L) {
            StringBuilder sb5 = new StringBuilder();
            double d5 = (double) j;
            Double.isNaN(d5);
            Double.isNaN(1.125899906842624E15d);
            Double.isNaN(d5);
            sb5.append(floatForm(d5 / 1.125899906842624E15d));
            sb5.append(" PB");
            return sb5.toString();
        } else if (j < 1152921504606846976L) {
            return "???";
        } else {
            StringBuilder sb6 = new StringBuilder();
            double d6 = (double) j;
            Double.isNaN(d6);
            Double.isNaN(1.15292150460684698E18d);
            Double.isNaN(d6);
            sb6.append(floatForm(d6 / 1.15292150460684698E18d));
            sb6.append(" Eb");
            return sb6.toString();
        }
    }

    public static String getSize(long j) {
        long j2 = j;
        double d = (double) (j2 / 1000);
        Double.isNaN(d);
        Double.isNaN(1000.0d);
        Double.isNaN(d);
        double d2 = d / 1000.0d;
        Double.isNaN(1000.0d);
        double d3 = d2 / 1000.0d;
        Double.isNaN(1000.0d);
        double d4 = d3 / 1000.0d;
        if (j2 < 1000) {
            return j2 + " B";
        } else if (j2 >= 1000 && j2 < 1000000) {
            return String.format("%.2f", new Object[]{Double.valueOf(d)}) + " KB";
        } else if (j2 >= 1000000 && j2 < 1000000000) {
            return String.format("%.2f", new Object[]{Double.valueOf(d2)}) + " MB";
        } else if (j2 >= 1000000000 && j2 < 1000000000000L) {
            return String.format("%.2f", new Object[]{Double.valueOf(d3)}) + " GB";
        } else if (j2 < 1000000000000L) {
            return "";
        } else {
            return String.format("%.2f", new Object[]{Double.valueOf(d4)}) + " TB";
        }
    }

    public static String StoragePath(String str, Context context) {
        List<String> storageDirectories = getStorageDirectories(context);
        if (storageDirectories.size() <= 0) {
            return "";
        }
        try {
            if (str.equalsIgnoreCase("InternalStorage")) {
                return storageDirectories.get(0);
            }
            if (str.equalsIgnoreCase("ExternalStorage")) {
                if (storageDirectories.size() >= 1) {
                    return storageDirectories.get(0);
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean externalMemoryAvailable(Activity activity) {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(activity, (String) null);
        if (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) {
            return false;
        }
        Log.e("Utils", "storages: " + externalFilesDirs.toString());
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0040, code lost:
        if (r5 == false) goto L_0x0043;
     */
    public static List<String> getStorageDirectories(Context context) {
        String str;
        Pattern compile = Pattern.compile(InternalZipConstants.ZIP_FILE_SEPARATOR);
        ArrayList arrayList = new ArrayList();
        String str2 = System.getenv("EXTERNAL_STORAGE");
        String str3 = System.getenv("SECONDARY_STORAGE");
        String str4 = System.getenv("EMULATED_STORAGE_TARGET");
        if (!TextUtils.isEmpty(str4)) {
            if (Build.VERSION.SDK_INT >= 17) {
                String[] split = compile.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                boolean z = true;
                str = split[split.length - 1];
                try {
                    Integer.valueOf(str);
                } catch (NumberFormatException unused) {
                    z = false;
                }
            }
            str = "";
            if (TextUtils.isEmpty(str)) {
                arrayList.add(str4);
            } else {
                arrayList.add(str4 + File.separator + str);
            }
        } else if (TextUtils.isEmpty(str2)) {
            arrayList.add("/storage/sdcard0");
        } else {
            arrayList.add(str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            Collections.addAll(arrayList, str3.split(File.pathSeparator));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            arrayList.clear();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            for (String str5 : getExtSdCardPaths(context)) {
                File file = new File(str5);
                if (!arrayList.contains(str5) && canListFiles(file)) {
                    arrayList.add(str5);
                }
            }
        }
        return arrayList;
    }

    public static boolean canListFiles(File file) {
        return file.canRead() && file.isDirectory();
    }

    public static String[] getExtSdCardPaths(Context context) {
        ArrayList arrayList = new ArrayList();
        for (File file : ContextCompat.getExternalFilesDirs(context, "external")) {
            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
                int lastIndexOf = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (lastIndexOf < 0) {
                    Log.w("FileUtils", "Unexpected external manager dir: " + file.getAbsolutePath());
                } else {
                    String substring = file.getAbsolutePath().substring(0, lastIndexOf);
                    try {
                        substring = new File(substring).getCanonicalPath();
                    } catch (IOException unused) {
                    }
                    arrayList.add(substring);
                }
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add("/storage/sdcard1");
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static String getMimeType(Context context, Uri uri) {
        if ("content".equals(uri.getScheme())) {
            return context.getContentResolver().getType(uri);
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()).toLowerCase());
    }

    public static String formateSize(long j) {
        if (j <= 0) {
            return "0";
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(new String[]{"B", "KB", "MB", "GB", "TB"}[log10]);
        return sb.toString();
    }

    public static String getFileExtension(String str) {
        return str.substring(str.lastIndexOf(".")).replace(".", "");
    }

    private static long[] getThisMonth() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(5, 1);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return new long[]{instance.getTimeInMillis(), currentTimeMillis};
    }

    public static long[] getTodayRange() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return new long[]{instance.getTimeInMillis(), currentTimeMillis};
    }

    private static long[] getThisWeek() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(7, 2);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return new long[]{currentTimeMillis - 518400000, currentTimeMillis};
    }

    public static boolean isSystemApp(PackageManager packageManager, String str) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            if (applicationInfo == null) {
                return false;
            }
            if ((applicationInfo.flags & 1) == 0 && (applicationInfo.flags & 128) == 0) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInstalled(PackageManager packageManager, String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            return true;
        }
        return false;
    }

    public static String parsePackageName(PackageManager packageManager, String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        return applicationInfo != null ? (String) packageManager.getApplicationLabel(applicationInfo) : str;
    }

    public static String humanReadableMillis(long j) {
        long j2 = j / 1000;
        if (j2 < 60) {
            return String.format("%s sec", new Object[]{Long.valueOf(j2)});
        } else if (j2 < 3600) {
            return String.format("%s min %s sec", new Object[]{Long.valueOf(j2 / 60), Long.valueOf(j2 % 60)});
        } else {
            long j3 = j2 % 3600;
            return String.format("%s hrs %s min %s sec", new Object[]{Long.valueOf(j2 / 3600), Long.valueOf(j3 / 60), Long.valueOf(j3 % 60)});
        }
    }

    public static List<Long> getXAxisValue(long j) {
        ArrayList arrayList = new ArrayList();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        for (int i = 0; i < 7; i++) {
            if (i != 0) {
                instance.add(5, -1);
            }
            arrayList.add(Long.valueOf(instance.getTimeInMillis()));
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    public static String getDay(long j) {
        return DateFormat.format("EEE", new Date(j)).toString();
    }

    public static String humanReadableMillis1(long j) {
        long j2 = j / 1000;
        if (j2 < 60) {
            return String.format("%ss", new Object[]{Long.valueOf(j2)});
        } else if (j2 < 3600) {
            return String.format("%sm", new Object[]{Long.valueOf(j2 / 60)});
        } else {
            return String.format("%sh", new Object[]{Long.valueOf(j2 / 3600)});
        }
    }

    public static long[] getDayRangeChartTime(long j, boolean z) {
        Calendar instance = Calendar.getInstance();
        instance.set(11, 24);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        instance.setTimeInMillis(j);
        if (!z) {
            instance.add(5, 1);
        }
        long timeInMillis = instance.getTimeInMillis();
        Calendar instance2 = Calendar.getInstance();
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        instance2.setTimeInMillis(j);
        return new long[]{instance2.getTimeInMillis(), timeInMillis};
    }

    public static long[] getDayRangeTime(long j) {
        Calendar instance = Calendar.getInstance();
        instance.set(11, 24);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        instance.setTimeInMillis(j);
        long timeInMillis = instance.getTimeInMillis();
        Calendar instance2 = Calendar.getInstance();
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        return new long[]{instance2.getTimeInMillis(), timeInMillis};
    }

    public static boolean isImage(String str) {
        String fileType = getFileType(str);
        if (fileType == null) {
            return false;
        }
        if (fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("png") || fileType.equals("jpeg") || fileType.equals("bmp") || fileType.equals("wbmp") || fileType.equals("ico") || fileType.equals("jpe")) {
            return true;
        }
        return false;
    }

    public static String getFileType(String str) {
        int lastIndexOf = 0;
        return (str == null || lastIndexOf == -1) ? "" : str.substring(lastIndexOf + 1).toLowerCase();
    }

    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkUsagePermission(Context context) {
        AppOpsManager appOpsManager = null;
        return Build.VERSION.SDK_INT >= 19 && appOpsManager != null && appOpsManager.checkOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName()) == 0;
    }

    public static boolean canDrawOverlayViews(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return true;
        }
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                return Settings.canDrawOverlays(context);
            }
            return true;
        } catch (NoSuchMethodError unused) {
            return canDrawOverlaysUsingReflection(context);
        }
    }

    public static boolean canDrawOverlaysUsingReflection(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 19) {
                return false;
            }
            Class[] clsArr = {Integer.TYPE, Integer.TYPE, String.class};
            if (((Integer) AppOpsManager.class.getMethod("checkOp", clsArr).invoke((AppOpsManager) context.getSystemService("appops"), new Object[]{24, Integer.valueOf(Binder.getCallingUid()), context.getApplicationContext().getPackageName()})).intValue() == 0) {
                return true;
            }
            return false;
        } catch (Exception unused) {
        }
        return false;
    }

    public static void showSDCardPermissionDialog(final Activity activity, String str) {
        final Dialog dialog = new Dialog(activity, R.style.WideDialog);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.setContentView(R.layout.layout_sdcard_permission_dialog);
        ((TextView) dialog.findViewById(R.id.title)).setText(activity.getResources().getString(R.string.needsaccesssummary) + str + activity.getResources().getString(R.string.needsaccesssummary1));
        ((TextView) dialog.findViewById(R.id.actionCancel)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.actionOk)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                StorageUtils.lambda$showSDCardPermissionDialog$1(dialog, activity, view);
            }
        });
        dialog.show();
    }

    static void lambda$showSDCardPermissionDialog$1(Dialog dialog, Activity activity, View view) {
        dialog.dismiss();
        triggerStorageAccessFramework(activity);
    }

    private static void triggerStorageAccessFramework(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.addFlags(2);
            safedk_Activity_startActivityForResult_206f42f0b65887e835d87ee52d14d221(activity, intent, 300);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static int checkFSDCardPermission(File file, Activity activity) {
        boolean z = Build.VERSION.SDK_INT >= 21;
        boolean isOnExtSdCard = isOnExtSdCard(file, activity);
        if (!z || !isOnExtSdCard) {
            if ((Build.VERSION.SDK_INT != 19 || !isOnExtSdCard(file, activity)) && !isWritable(new File(file, "DummyFile"))) {
                return 0;
            }
            return 1;
        } else if (!file.exists() || !file.isDirectory()) {
            return 0;
        } else {
            if (isWritableNormalOrSaf(file, activity)) {
                return 1;
            }
            showSDCardPermissionDialog(activity, file.getPath());
            return 2;
        }
    }

    public static final boolean isWritableNormalOrSaf(File file, Context context) {
        File file2;
        boolean z = false;
        if (file == null) {
            return false;
        }
        if (file.exists() && file.isDirectory()) {
            int i = 0;
            do {
                StringBuilder sb = new StringBuilder();
                sb.append("AugendiagnoseDummyFile");
                i++;
                sb.append(i);
                file2 = new File(file, sb.toString());
            } while (file2.exists());
            if (isWritable(file2)) {
                return true;
            }
            DocumentFile documentFile = getDocumentFile(file2, false, context);
            if (documentFile == null) {
                return false;
            }
            if (documentFile.canWrite() && file2.exists()) {
                z = true;
            }
            deleteFile(file2, context);
        }
        return z;
    }

    public static final boolean deleteFile(File file, Context context) {
        if (file == null) {
            return true;
        }
        boolean deleteFilesInFolder = deleteFilesInFolder(file, context);
        if (file.delete() || deleteFilesInFolder) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 21 && isOnExtSdCard(file, context)) {
            DocumentFile documentFile = getDocumentFile(file, false, context);
            if (documentFile != null) {
                return documentFile.delete();
            }
            return false;
        } else if (Build.VERSION.SDK_INT != 19) {
            return !file.exists();
        } else {
            try {
                context.getContentResolver().delete(getUriFromFile(file.getAbsolutePath(), context), (String) null, (String[]) null);
                return !file.exists();
            } catch (Exception e) {
                Log.e("FileUtils", "Error when deleting manager " + file.getAbsolutePath(), e);
                return false;
            }
        }
    }

    public static final boolean deleteFilesInFolder(File file, Context context) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            for (File deleteFilesInFolder : file.listFiles()) {
                deleteFilesInFolder(deleteFilesInFolder, context);
            }
            if (!file.delete()) {
                return false;
            }
            return true;
        } else if (!file.delete()) {
            return false;
        } else {
            return true;
        }
    }

    public static final boolean isWritable(File file) {
        if (file == null) {
            return false;
        }
        boolean exists = file.exists();
        try {
            new FileOutputStream(file, true).close();
        } catch (IOException unused) {
        }
        boolean canWrite = file.canWrite();
        if (!exists) {
            file.delete();
        }
        return canWrite;
    }

    public static boolean isOnExtSdCard(File file, Context context) {
        return getExtSdCardFolder(file, context) != null;
    }

    public static String getExtSdCardFolder(File file, Context context) {
        String[] extSdCardPaths = getExtSdCardPaths(context);
        int i = 0;
        while (i < extSdCardPaths.length) {
            try {
                if (file.getCanonicalPath().startsWith(extSdCardPaths[i])) {
                    return extSdCardPaths[i];
                }
                i++;
            } catch (IOException unused) {
            }
        }
        return null;
    }

    public static DocumentFile getDocumentFile(File file, boolean z, Context context) {
        String extSdCardFolder = getExtSdCardFolder(file, context);
        if (extSdCardFolder == null) {
            return null;
        }
        try {
            String canonicalPath = file.getCanonicalPath();
            Log.e("StorageUtils", "fullPath: " + canonicalPath);
            if (!extSdCardFolder.equals(canonicalPath)) {
                String substring = canonicalPath.substring(extSdCardFolder.length() + 1);
                String sDCardTreeUri = PreferencesManager.getSDCardTreeUri(context);
                Uri parse = TextUtils.isEmpty(sDCardTreeUri) ? Uri.parse(sDCardTreeUri) : null;
                if (parse != null) {
                    return null;
                }
                Log.e("StorageUtils", "treeUri: " + parse + " as: " + sDCardTreeUri);
                StringBuilder sb = new StringBuilder();
                sb.append("relativePath: ");
                sb.append(substring);
                Log.e("StorageUtils", sb.toString());
                Log.e("StorageUtils", "baseFolder: " + extSdCardFolder);
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, parse);
                String[] split = substring.split("\\/");
                Log.e("StorageUtils", "parts: " + split.toString());
                if (fromTreeUri != null) {
                    for (int i = 0; i < split.length; i++) {
                        if (!(split[i] == null || fromTreeUri == null)) {
                            DocumentFile findFile = fromTreeUri.findFile(split[i]);
                            if (findFile == null) {
                                if (i >= split.length - 1) {
                                    if (!z) {
                                        fromTreeUri = fromTreeUri.createFile("image", split[i]);
                                    }
                                }
                                fromTreeUri = fromTreeUri.createDirectory(split[i]);
                            } else {
                                fromTreeUri = findFile;
                            }
                        }
                    }
                }
                return fromTreeUri;
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static Uri getUriFromFile(String str, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentResolver contentResolver2 = contentResolver;
        Cursor query = contentResolver2.query(MediaStore.Files.getContentUri("external"), new String[]{"_id"}, "_data = ?", new String[]{str}, "date_added desc");
        query.moveToFirst();
        if (query.isAfterLast()) {
            query.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
        }
        Uri build = MediaStore.Files.getContentUri("external").buildUpon().appendPath(Integer.toString(query.getInt(query.getColumnIndex("_id")))).build();
        query.close();
        return build;
    }

    public static int getBottomNavigationHeight(Context context) {
        int identifier;
        Resources resources = context.getResources();
        if (!hasNavigationBar(resources) || (identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android")) <= 0) {
            return 0;
        }
        return resources.getDimensionPixelSize(identifier);
    }

    private static boolean hasNavigationBar(Resources resources) {
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return identifier > 0 && resources.getBoolean(identifier);
    }

    public static boolean moveFile(File file, File file2, Context context) throws IOException {
        if (!copyFile(file, file2, context) || !deleteFile(file, context)) {
            return false;
        }
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
            }
        });
        return true;
    }

    public static boolean renameFile(File file, String str, Context context) {
        DocumentFile documentFile = getDocumentFile(file, false, context);
        boolean renameTo = documentFile.renameTo(str);
        Log.e("renameFileUtils", "ParentFile: " + documentFile.getParentFile() + " Name: " + documentFile.getName());
        return renameTo;
    }

    public static boolean copyFile(File file, File file2, Context context) throws IOException {
        String[] r6 = new String[0];
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        FileChannel fileChannel2 = null;
        FileOutputStream fileOutputStream;
        OutputStream outputStream;
        FileInputStream fileInputStream2;
        File file3 = file;
        File file4 = file2;
        Context context2 = context;
        if (file.isDirectory()) {
            if (!file2.exists()) {
                file2.mkdir();
            }
            try {
                String[] list = file.list();
                for (int i = 0; i < file.listFiles().length; i++) {
                    copyFile(new File(file3, list[i]), new File(file4, list[i]), context2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           //String[] r6 = 0;
            try {
                fileInputStream = new FileInputStream(file3);
                try {
                    if (isWritable(file2)) {
                        fileOutputStream = new FileOutputStream(file4);
                        try {
                            fileChannel2 = fileInputStream.getChannel();
                            try {
                                fileChannel = fileOutputStream.getChannel();
                                fileChannel2.transferTo(0, fileChannel2.size(), fileChannel);
                            } catch (Exception e2) {
                               // e = e2;
                                //fileInputStream2 = r6;
                            } catch (Throwable th) {
                            }
                        } catch (Exception th) {
                            throw th;
                        } catch (Throwable th2) {
                            fileInputStream.close();
                            fileOutputStream.close();
                            //r6.close();
                            //r6.close();
                            throw th2;
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 21) {
                            outputStream = context.getContentResolver().openOutputStream(getDocumentFile(file4, false, context2).getUri());
                        } else if (Build.VERSION.SDK_INT == 19) {
                            outputStream = context.getContentResolver().openOutputStream(getUriFromFile(file2.getAbsolutePath(), context2));
                        } else {
                            fileInputStream.close();
                            //r6.close();
                            return false;
                        }
                        if (outputStream != null) {
                            byte[] bArr = new byte[16384];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(bArr, 0, read);
                            }
                        }
                        fileOutputStream = (FileOutputStream) outputStream;
                        //fileChannel2 = r6;
                        fileChannel = fileChannel2;
                    }
                    MediaScannerConnection.scanFile(context2, new String[]{file2.getPath()}, r6, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    fileInputStream.close();
                    fileOutputStream.close();
                    fileChannel2.close();
                    fileChannel.close();
                } catch (Exception unused) {
                    fileInputStream.close();
                    return false;
                } catch (Throwable th3) {
                }
            } catch (Exception e4) {
            } catch (Throwable unused2) {
                fileInputStream.close();
                return true;
            }
        }
        return true;
    }

    public static boolean copyFile1(File file, File file2, Context context) {
        if (file.isDirectory()) {
            if (!file2.exists()) {
                file2.mkdir();
            }
            try {
                String[] list = file.list();
                for (int i = 0; i < file.listFiles().length; i++) {
                    copyFile(new File(file, list[i]), new File(file2, list[i]), context);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        } else {
            return true;
        }
    }

    public static void getSdCardPath(File file, Context context) {
        DocumentFile documentFile = getDocumentFile(file, false, context);
        Log.e("StorageUtils", " rename ParentFile: " + documentFile.getParentFile() + " Name: " + documentFile.getName());
    }
}
