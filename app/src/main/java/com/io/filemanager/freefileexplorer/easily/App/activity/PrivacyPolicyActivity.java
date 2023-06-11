package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webview;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_privacy_policy);
        ButterKnife.bind((Activity) this);
        init();
    }

    private void init() {
        this.webview.setWebViewClient(new WebViewClient());
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.loadUrl(getResources().getString(R.string.privacy_url));
        this.webview.requestFocus();
    }

    @OnClick({2131296632})
    public void onViewClicked() {
        finish();
    }
}
