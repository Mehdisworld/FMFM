package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.apache.commons.io.IOUtils;

public class SettingActivity extends AppCompatActivity {

    public static void safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(SettingActivity p0, Intent p1) {

        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        char c = 1;
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_setting);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        //Mix Native Ads Call
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_300);
        FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
        ImageView image = (ImageView) findViewById(R.id.banner_image);
        CardView qurekanative = (CardView) findViewById(R.id.qurekanative);
        RelativeLayout NativeAdContainer = (RelativeLayout) findViewById(R.id.nativeAds);
        RelativeLayout NativeAdsStartApp = (RelativeLayout) findViewById(R.id.sNativeAds);
        RecyclerView nativeMoPub = (RecyclerView) findViewById(R.id.nativemopub);
        FrameLayout maxNative = (FrameLayout) findViewById(R.id.max_native_ad_layout);
        //SplashLaunchActivity.MixNativeAdsCall(this, shimmerFrameLayout, frameLayout, image, qurekanative, NativeAdContainer, NativeAdsStartApp, nativeMoPub, maxNative);

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.back_setting, R.id.lout_about_us, R.id.lout_more_app, R.id.lout_privacy_policy, R.id.lout_rate_us, R.id.lout_share_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_setting:
                finish();
                return;
            case R.id.lout_about_us:
                safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(this, new Intent(this, AboutUsActivity.class));
                return;
            case R.id.lout_more_app:
                try {
                    safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(this, new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:" + SplashLaunchActivity.MoreApps)));
                    return;
                } catch (ActivityNotFoundException unused) {
                    safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(this, new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/developer?id=" + SplashLaunchActivity.MoreApps)));
                    return;
                }
            case R.id.lout_privacy_policy:
                Intent intentP = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + SplashLaunchActivity.MoreApps));
                startActivity(intentP);
                return;
            case R.id.lout_rate_us:
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(this, intent);
                return;
            case R.id.lout_share_us:
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                intent2.addFlags(524288);
                intent2.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                intent2.putExtra("android.intent.extra.TEXT", "Click on below link to download " + getResources().getString(R.string.app_name) + IOUtils.LINE_SEPARATOR_UNIX + "https://play.google.com/store/apps/details?id=" + getPackageName() + "");
                safedk_SettingActivity_startActivity_c2cfe1578a4cb2ed2d98dbc660453149(this, Intent.createChooser(intent2, "Share Using!"));
                return;
            default:
                return;
        }
    }
}
