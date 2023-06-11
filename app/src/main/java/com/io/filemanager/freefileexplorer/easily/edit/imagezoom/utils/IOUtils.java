package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.Closeable;

public class IOUtils {
    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void closeSilently(ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void closeSilently(Cursor cursor) {
        if (cursor != null && cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static String getRealFilePath(Context context, Uri uri) {
        Cursor query;
        int columnIndex;
        String str = null;
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null) {
            return uri.getPath();
        }
        if ("file".equals(scheme)) {
            return uri.getPath();
        }
        if ("content".equals(scheme) && (query = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null)) != null) {
            if (query.moveToFirst() && (columnIndex = query.getColumnIndex("_data")) > -1) {
                str = query.getString(columnIndex);
            }
            query.close();
        }
        return str;
    }
}
