package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.utils;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;

public class ExifUtils {
    public static final String[] EXIF_TAGS = {ExifInterface.TAG_F_NUMBER, ExifInterface.TAG_DATETIME, ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_FLASH, ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.TAG_ISO_SPEED_RATINGS, ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL, ExifInterface.TAG_WHITE_BALANCE};

    public static int getExifOrientation(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return getExifOrientation(new android.media.ExifInterface(str));
        } catch (IOException unused) {
            return 0;
        }
    }

    public static int getExifOrientation(android.media.ExifInterface exifInterface) {
        if (exifInterface == null) {
            return 0;
        }
        int attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        if (attributeInt == -1) {
            return attributeInt;
        }
        if (attributeInt == 3) {
            return 180;
        }
        if (attributeInt != 6) {
            return attributeInt != 8 ? 0 : 270;
        }
        return 90;
    }

    public static boolean loadAttributes(String str, Bundle bundle) {
        try {
            android.media.ExifInterface exifInterface = new android.media.ExifInterface(str);
            for (String str2 : EXIF_TAGS) {
                bundle.putString(str2, exifInterface.getAttribute(str2));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveAttributes(String str, Bundle bundle) {
        try {
            android.media.ExifInterface exifInterface = new android.media.ExifInterface(str);
            for (String str2 : EXIF_TAGS) {
                if (bundle.containsKey(str2)) {
                    exifInterface.setAttribute(str2, bundle.getString(str2));
                }
            }
            try {
                exifInterface.saveAttributes();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getExifOrientation(int i) {
        if (i == 0) {
            return String.valueOf(1);
        }
        if (i == 90) {
            return String.valueOf(6);
        }
        if (i == 180) {
            return String.valueOf(3);
        }
        if (i == 270) {
            return String.valueOf(8);
        }
        throw new AssertionError("invalid: " + i);
    }

    public static int getExifOrientation(Context context, Uri uri) {
        Cursor query = null;
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return getExifOrientation(uri.getPath());
        }
        if (scheme.equals("content")) {
            try {
                ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(uri);
                if (acquireContentProviderClient != null) {
                    try {
                        query = acquireContentProviderClient.query(uri, new String[]{"orientation", "_data"}, (String) null, (String[]) null, (String) null);
                        if (query == null) {
                            return 0;
                        }
                        int columnIndex = query.getColumnIndex("orientation");
                        int columnIndex2 = query.getColumnIndex("_data");
                        if (query.getCount() > 0) {
                            query.moveToFirst();
                            int i = columnIndex > -1 ? query.getInt(columnIndex) : 0;
                            if (columnIndex2 > -1) {
                                i |= getExifOrientation(query.getString(columnIndex2));
                            }
                            query.close();
                            return i;
                        }
                        query.close();
                        query.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Throwable th) {
                        query.close();
                        throw th;
                    }
                }
            } catch (SecurityException unused) {
            }
        }
        return 0;
    }
}
