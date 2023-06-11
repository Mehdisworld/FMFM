package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.service.ImageDataService;


public class PermissionActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 111;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_dec)
    AppCompatTextView txtDec;

    public static void safedk_PermissionActivity_startActivityForResult_ab79c35c575699bf63763cc1902209d9(PermissionActivity p0, Intent p1, int p2) {
        if (p1 != null) {
            p0.startActivityForResult(p1, p2);
        }
    }

    public static void safedk_PermissionActivity_startActivity_13f4510170625b052cafb9f9b06839f5(PermissionActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(-1);
        }
        setContentView((int) R.layout.activity_permission);
        ButterKnife.bind((Activity) this);
        intView();
    }

    private void intView() {
        setSupportActionBar(this.toolbar);
        if (getSupportActionBar() != null) {
            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.setTitle((CharSequence) "   " + getResources().getString(R.string.app_name));
        }
        if (isPermissionGranted()) {
            showHomeScreen();
        }
        ((AppCompatTextView) findViewById(R.id.txt_dec)).setText(getResources().getString(R.string.permission_txt1) + " " + getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.permission_txt2));
    }

    private void showHomeScreen() {
        startService(new Intent(this, ImageDataService.class));
        safedk_PermissionActivity_startActivity_13f4510170625b052cafb9f9b06839f5(this, new Intent(this, HomeActivity.class));
        finish();
    }

    /*@OnClick({R.id.btn_allow})
    public void onViewClicked() {
        if (isPermissionGranted()) {
            showHomeScreen();
        } else {
            requestPermission();
        }
    }*/

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 30) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                safedk_PermissionActivity_startActivityForResult_ab79c35c575699bf63763cc1902209d9(this, intent, 2296);
            } catch (Exception unused) {
                Intent intent2 = new Intent();
                intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                safedk_PermissionActivity_startActivityForResult_ab79c35c575699bf63763cc1902209d9(this, intent2, 2296);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 111);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2296 && Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
                showHomeScreen();
            } else {
                Toast.makeText(this, "Allow permission for storage access!", 0).show();
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 111 && iArr.length > 0) {
            if (iArr[0] == 0) {
                showHomeScreen();
            } else {
                Toast.makeText(this, "Allow permission for storage access!", 0).show();
            }
        }
    }

    public void applypermission(View view) {
        if (isPermissionGranted()) {
            showHomeScreen();
        } else {
            requestPermission();
        }
    }
}
