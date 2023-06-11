package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.service.ImageDataService;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.Method;

public class SplashActivity extends AppCompatActivity {

    Method method;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        getWindow().addFlags(1024);
        getWindow().getDecorView().setSystemUiVisibility(2);



        Constant.isbackImage = false;
        init();
        Method method2 = new Method(this);
        this.method = method2;
        method2.login();
        this.method.forceRTLIfSupported();
        if ("system".equals(this.method.themMode())) {
            AppCompatDelegate.setDefaultNightMode(-1);
        }
    }

    public void init() {
        final boolean isPermissionGranted = isPermissionGranted();
        if (isPermissionGranted) {
            if (Build.VERSION.SDK_INT >= 26) {
                startService(new Intent(this, ImageDataService.class));
            } else {
                startService(new Intent(this, ImageDataService.class));
            }
        }
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            public void safedk_SplashActivity_startActivity_57fc2003ba0f879b148528276db2ca44(SplashActivity p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void run() {
                Intent intent;
                if (isPermissionGranted) {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, PermissionActivity.class);
                }
                safedk_SplashActivity_startActivity_57fc2003ba0f879b148528276db2ca44(SplashActivity.this, intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    public boolean isPermissionGranted() {
        return Build.VERSION.SDK_INT < 23 || ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
    
}
