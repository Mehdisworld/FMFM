package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.DisplayImageAdapter;
import com.io.filemanager.freefileexplorer.easily.edit.EditActivity;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayFavoriteEvent;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.oncliclk.ImageToolbar;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
//import com.applovin.mediation.ads.MaxAdView;
//import com.safedk.android.utils.Logger;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DisplayImageActivity extends AppCompatActivity implements ImageToolbar {

    DisplayImageAdapter adapter;
    List<PhotoData> displayImageList = new ArrayList();
    @BindView(R.id.iv_back)
    AppCompatImageView ivBack;
    @BindView(R.id.iv_favourite)
    ImageView ivFav;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    int position = -1;
    int sdCardPermissionType = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_favourite)
    TextView txtFav;
    @BindView(R.id.txt_header_title)
    TextView txtTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static void safedk_DisplayImageActivity_startActivity_7102f342198b3be553c09d109522d6f1(DisplayImageActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_display_image);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        boolean z = Constant.isbackImage;
        intView();
        copyMoveEvent();
    }

    public void OnImageToolbar(View view) {
        if (this.llBottom.getVisibility() == 0 || this.toolbar.getVisibility() == 0) {
            this.toolbar.setVisibility(8);
            this.llBottom.setVisibility(8);
            return;
        }
        this.toolbar.setVisibility(0);
        this.llBottom.setVisibility(0);
    }

    private void intView() {
        this.displayImageList = Constant.displayImageList;
        Intent intent = getIntent();
        if (intent != null) {
            this.position = intent.getIntExtra("pos", 0);
        }
        try {
            DisplayImageAdapter displayImageAdapter = new DisplayImageAdapter(this, this.displayImageList, this);
            this.adapter = displayImageAdapter;
            this.viewPager.setAdapter(displayImageAdapter);
            this.viewPager.setCurrentItem(this.position);
            updateFavData();
            this.txtTitle.setText(this.displayImageList.get(this.position).getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                DisplayImageActivity.this.position = i;
                DisplayImageActivity.this.txtTitle.setText(DisplayImageActivity.this.displayImageList.get(DisplayImageActivity.this.position).getFileName());
                DisplayImageActivity.this.updateFavData();
            }
        });
    }

    
    public void updateFavData() {
        if (this.displayImageList.get(this.viewPager.getCurrentItem()).isFavorite()) {
            this.ivFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_image_fill));
            this.txtFav.setTextColor(getResources().getColor(R.color.theme_color));
            return;
        }
        this.ivFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_image_unfill));
        this.txtFav.setTextColor(getResources().getColor(R.color.white));
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.lout_delete, R.id.lout_share, R.id.lout_edit, R.id.lout_fav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                return;
            case R.id.iv_more:
                showMoreMenu();
                return;
            case R.id.lout_delete:
                deleteDialog();
                return;
            case R.id.lout_edit:
                File file = new File(Environment.getExternalStorageDirectory().getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.edit_file));
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                String str = file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + "IMG_" + new SimpleDateFormat("HHmmss_dMyy").format(new Date()) + ".jpeg";
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("imagePath", this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath());
                intent.putExtra("outputPath", str);
                //safedk_DisplayImageActivity_startActivity_7102f342198b3be553c09d109522d6f1(this, intent);
                SplashLaunchActivity.InterstitialAdsCall(this, intent);
                return;
            case R.id.lout_fav:
                setClickFav();
                return;
            case R.id.lout_share:
                File file3 = new File(this.displayImageList.get(this.position).getFilePath());
                Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file3);
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.SEND");
                intent2.setType(Utils.getMimeTypeFromFilePath(file3.getPath()));
                intent2.addFlags(1);
                intent2.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getPackageName());
                intent2.setFlags(268435456);
                intent2.putExtra("android.intent.extra.STREAM", uriForFile);
                safedk_DisplayImageActivity_startActivity_7102f342198b3be553c09d109522d6f1(this, Intent.createChooser(intent2, "Share with..."));
                return;
            default:
                return;
        }
    }

    public void setClickFav() {
        if (this.displayImageList.get(this.viewPager.getCurrentItem()).isFavorite()) {
            this.ivFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_image_unfill));
            this.txtFav.setTextColor(getResources().getColor(R.color.white));
            this.displayImageList.get(this.viewPager.getCurrentItem()).setFavorite(false);
            setUnFavorite(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath());
            RxBus.getInstance().post(new DisplayFavoriteEvent(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath(), false));
            return;
        }
        this.ivFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_image_fill));
        this.txtFav.setTextColor(getResources().getColor(R.color.theme_color));
        this.displayImageList.get(this.viewPager.getCurrentItem()).setFavorite(true);
        setFavorite(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath());
        RxBus.getInstance().post(new DisplayFavoriteEvent(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath(), true));
    }

    private void showMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.more_menu_video, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menu_delete).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_set_as_wallpaper).setVisible(true);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_copy:
                        Constant.isCopyData = true;
                        DisplayImageActivity.this.setCopyMoveOptinOn();
                        break;
                    case R.id.menu_delete:
                        DisplayImageActivity.this.deleteDialog();
                        break;
                    case R.id.menu_details:
                        DisplayImageActivity.this.showDetailDialog();
                        break;
                    case R.id.menu_move:
                        Constant.isCopyData = false;
                        DisplayImageActivity.this.setCopyMoveOptinOn();
                        break;
                    case R.id.menu_set_as_wallpaper:
                        WallpaperManager instance = WallpaperManager.getInstance(DisplayImageActivity.this);
                        try {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inMutable = true;
                            Bitmap decodeFile = BitmapFactory.decodeFile(DisplayImageActivity.this.displayImageList.get(DisplayImageActivity.this.position).getFilePath(), options);
                            if (decodeFile != null) {
                                instance.setBitmap(decodeFile);
                                Toast.makeText(DisplayImageActivity.this, "Set Wallpaper Successfully", 0).show();
                                break;
                            } else {
                                Toast.makeText(DisplayImageActivity.this, "Error!", 0).show();
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(DisplayImageActivity.this, "Error!", 0).show();
                            break;
                        }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setUnFavorite(String str) {
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        while (true) {
            if (i < favouriteList.size()) {
                if (favouriteList.get(i) != null && !favouriteList.get(i).equalsIgnoreCase("") && favouriteList.get(i).equalsIgnoreCase(str)) {
                    favouriteList.remove(i);
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    private void setFavorite(String str) {
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        favouriteList.add(str);
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    
    public void setCopyMoveOptinOn() {
        Constant.pastList = new ArrayList<>();
        String externalStoragePath = Utils.getExternalStoragePath(this, true);
        File file = new File(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath());
        if (file.exists()) {
            Constant.pastList.add(file);
            if (externalStoragePath != null) {
                externalStoragePath.equalsIgnoreCase("");
            }
        }
        Constant.isFileFromSdCard = false;
        Intent intent = new Intent(this, StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        setResult(-1);
        safedk_DisplayImageActivity_startActivity_7102f342198b3be553c09d109522d6f1(this, intent);
    }

    public void setDeleteFile() {
        int currentItem = this.viewPager.getCurrentItem();
        ArrayList arrayList = new ArrayList();
        File file = new File(this.displayImageList.get(this.viewPager.getCurrentItem()).getFilePath());
        if (StorageUtils.checkFSDCardPermission(file, this) == 2) {
            Toast.makeText(this, "Please give a permission for manager operation", 0).show();
        } else if (StorageUtils.deleteFile(file, this)) {
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            arrayList.add(file.getPath());
            this.displayImageList.remove(this.viewPager.getCurrentItem());
            this.adapter.notifyDataSetChanged();
            DisplayImageAdapter displayImageAdapter = new DisplayImageAdapter(this, this.displayImageList, this);
            this.adapter = displayImageAdapter;
            this.viewPager.setAdapter(displayImageAdapter);
            RxBus.getInstance().post(new DisplayDeleteEvent(arrayList));
            if (currentItem < this.displayImageList.size() - 1) {
                this.position = currentItem;
                this.viewPager.setCurrentItem(currentItem);
            } else if (this.displayImageList.size() == 0) {
                onBackPressed();
            } else {
                try {
                    this.viewPager.setCurrentItem(currentItem - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "Error", 0).show();
        }
    }

    
    public void deleteDialog() {
        this.sdCardPermissionType = 1;
        final int currentItem = this.viewPager.getCurrentItem();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String externalStoragePath = Utils.getExternalStoragePath(DisplayImageActivity.this, true);
                if (externalStoragePath == null || externalStoragePath.equalsIgnoreCase("")) {
                    DisplayImageActivity.this.setDeleteFile();
                } else if (!DisplayImageActivity.this.displayImageList.get(currentItem).getFilePath().contains(externalStoragePath)) {
                    DisplayImageActivity.this.setDeleteFile();
                } else if (StorageUtils.checkFSDCardPermission(new File(externalStoragePath), DisplayImageActivity.this) == 2) {
                    Toast.makeText(DisplayImageActivity.this, "Please give a permission for manager operation", 0).show();
                } else {
                    DisplayImageActivity.this.setDeleteFile();
                }
            }
        });
        builder.setNegativeButton((CharSequence) Html.fromHtml("<font color='#ffba00'>No</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    
    public void showDetailDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        File file = new File(this.displayImageList.get(this.position).getFilePath());
        TextView textView = (TextView) dialog.findViewById(R.id.txt_title);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_format);
        TextView textView3 = (TextView) dialog.findViewById(R.id.txt_time);
        TextView textView4 = (TextView) dialog.findViewById(R.id.txt_resolution);
        TextView textView5 = (TextView) dialog.findViewById(R.id.txt_file_size);
        TextView textView6 = (TextView) dialog.findViewById(R.id.txt_duration);
        TextView textView7 = (TextView) dialog.findViewById(R.id.txt_path);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lout_resolution);
        ((LinearLayout) dialog.findViewById(R.id.lout_duration)).setVisibility(8);
        TextView textView8 = (TextView) dialog.findViewById(R.id.btn_ok);
        if (file.exists()) {
            textView.setText(file.getName());
            textView7.setText(file.getPath());
            textView2.setText(Utils.getMimeTypeFromFilePath(file.getPath()));
            textView5.setText(Formatter.formatShortFileSize(this, file.length()));
            textView3.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm a").format(Long.valueOf(file.lastModified())));
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                int i = options.outHeight;
                int i2 = options.outWidth;
                textView4.setText(i2 + " x " + i);
                linearLayout.setVisibility(0);
            } catch (Exception e) {
                e.printStackTrace();
                linearLayout.setVisibility(8);
            }
        }
        textView8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                if (copyMoveEvent.getCopyMoveList() != null) {
                    copyMoveEvent.getCopyMoveList().size();
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                    if (deleteList != null && deleteList.size() != 0) {
                        for (int i = 0; i < deleteList.size(); i++) {
                            try {
                                if (deleteList.get(i).equalsIgnoreCase(DisplayImageActivity.this.displayImageList.get(DisplayImageActivity.this.viewPager.getCurrentItem()).getFilePath())) {
                                    int currentItem = DisplayImageActivity.this.viewPager.getCurrentItem();
                                    DisplayImageActivity.this.displayImageList.remove(DisplayImageActivity.this.viewPager.getCurrentItem());
                                    DisplayImageActivity.this.adapter.notifyDataSetChanged();
                                    DisplayImageActivity displayImageActivity = DisplayImageActivity.this;
                                    displayImageActivity.adapter = new DisplayImageAdapter(displayImageActivity, displayImageActivity.displayImageList, DisplayImageActivity.this);
                                    DisplayImageActivity.this.viewPager.setAdapter(DisplayImageActivity.this.adapter);
                                    if (currentItem < DisplayImageActivity.this.displayImageList.size() - 1) {
                                        DisplayImageActivity.this.position = currentItem;
                                        DisplayImageActivity.this.viewPager.setCurrentItem(currentItem);
                                    } else if (DisplayImageActivity.this.displayImageList.size() == 0) {
                                        DisplayImageActivity.this.onBackPressed();
                                    } else {
                                        try {
                                            DisplayImageActivity.this.viewPager.setCurrentItem(currentItem - 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 300) {
            String sDCardTreeUri = PreferencesManager.getSDCardTreeUri(this);
            Uri uri = null;
            Uri parse = sDCardTreeUri != null ? Uri.parse(sDCardTreeUri) : null;
            if (i2 == -1 && (uri = intent.getData()) != null) {
                PreferencesManager.setSDCardTreeUri(this, uri.toString());
                if (this.sdCardPermissionType == 1) {
                    setDeleteFile();
                }
            }
            if (i2 == -1) {
                try {
                    int flags = intent.getFlags() & 3;
                    if (Build.VERSION.SDK_INT >= 19) {
                        getContentResolver().takePersistableUriPermission(uri, flags);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (uri != null) {
                PreferencesManager.setSDCardTreeUri(this, parse.toString());
                if (this.sdCardPermissionType == 1) {
                    setDeleteFile();
                }
            }
        }
    }
}
