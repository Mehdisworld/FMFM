package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;

public class AboutUsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_version)
    AppCompatTextView txtVersion;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_us);
        ButterKnife.bind((Activity) this);
        intView();
    }

    private void intView() {
        try {
            String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            AppCompatTextView appCompatTextView = this.txtVersion;
            appCompatTextView.setText("(" + str + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ic_back})
    public void onViewClicked() {
        finish();
    }
}
