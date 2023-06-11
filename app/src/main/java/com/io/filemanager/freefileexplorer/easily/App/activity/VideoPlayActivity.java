package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.adapter.DisplayVideoAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.oncliclk.VideoPreviousNext;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
//import com.safedk.android.utils.Logger;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VideoPlayActivity extends AppCompatActivity implements VideoPreviousNext {
    private static final String TAG = "VideoPlayActivity";
    public static RelativeLayout toolbar;
    DisplayVideoAdapter adapter;
    int cachedHeight;
    int currentDur = 0;
    List<PhotoData> displayImageList = new ArrayList();
    String duration;
    boolean isCustomChangeSeekbar = false;
    boolean isFirstTime = true;
    boolean isFullscreen = false;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    Handler mHandler = new Handler();
    int position = -1;
    int sdCardPermissionType = 0;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.media_controller)
    UniversalMediaController universalMediaController;
    @BindView(R.id.videoLayout)
    RelativeLayout videoLayout;
    @BindView(R.id.videoView)
    UniversalVideoView videoView;

    public static void safedk_VideoPlayActivity_startActivity_029061d18ff770871baf54113c413c97(VideoPlayActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void OnPrevious(int i) {
        int i2 = this.position;
        if (i2 != 0) {
            this.position = i2 - 1;
        }
    }

    public void OnNext(int i) {
        if (this.position != this.displayImageList.size() - 1) {
            this.position++;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_video_play);
        ButterKnife.bind((Activity) this);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        intView();
        copyMoveEvent();
        boolean z = Constant.isbackImage;
    }

    private void intView() {
        Intent intent = getIntent();
        this.displayImageList.addAll(Constant.displayVideoList);
        if (intent != null) {
            this.position = intent.getIntExtra("pos", 0);
            try {
                setVideoAreaSize();
                this.videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
                    public void onScaleChange(boolean z) {
                        VideoPlayActivity.this.isFullscreen = z;
                        if (VideoPlayActivity.this.isFullscreen) {
                            ViewGroup.LayoutParams layoutParams = VideoPlayActivity.this.videoLayout.getLayoutParams();
                            layoutParams.width = -1;
                            layoutParams.height = -1;
                            VideoPlayActivity.this.videoLayout.setLayoutParams(layoutParams);
                            return;
                        }
                        ViewGroup.LayoutParams layoutParams2 = VideoPlayActivity.this.videoLayout.getLayoutParams();
                        layoutParams2.width = -1;
                        layoutParams2.height = VideoPlayActivity.this.cachedHeight;
                        VideoPlayActivity.this.videoLayout.setLayoutParams(layoutParams2);
                    }

                    public void onPause(MediaPlayer mediaPlayer) {
                        Log.d(VideoPlayActivity.TAG, "onPause UniversalVideoView callback");
                    }

                    public void onStart(MediaPlayer mediaPlayer) {
                        Log.d(VideoPlayActivity.TAG, "onStart UniversalVideoView callback");
                    }

                    public void onBufferingStart(MediaPlayer mediaPlayer) {
                        Log.d(VideoPlayActivity.TAG, "onBufferingStart UniversalVideoView callback");
                    }

                    public void onBufferingEnd(MediaPlayer mediaPlayer) {
                        Log.d(VideoPlayActivity.TAG, "onBufferingEnd UniversalVideoView callback");
                    }
                });
                this.txtTitle.setText(this.displayImageList.get(this.position).getFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setVideoAreaSize() {
        this.videoLayout.post(new Runnable() {
            public void run() {
                VideoPlayActivity.this.cachedHeight = (int) ((((float) VideoPlayActivity.this.videoLayout.getWidth()) * 405.0f) / 720.0f);
                VideoPlayActivity.this.videoView.setMediaController(VideoPlayActivity.this.universalMediaController);
                VideoPlayActivity.this.videoView.setVideoPath(VideoPlayActivity.this.displayImageList.get(VideoPlayActivity.this.position).getFilePath());
                VideoPlayActivity.this.videoView.setAutoRotation(false);
                VideoPlayActivity.this.videoView.requestFocus();
                VideoPlayActivity.this.videoView.start();
            }
        });
    }

    public void onResume() {
        super.onResume();
        DisplayVideoAdapter displayVideoAdapter = this.adapter;
        if (displayVideoAdapter != null) {
            displayVideoAdapter.setvideoshow();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            onBackPressed();
        } else if (id == R.id.iv_more) {
            showMoreMenu();
        }
    }

    private void showMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_video, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.menu_details) {
                    return false;
                }
                VideoPlayActivity.this.showDetailDialog();
                return false;
            }
        });
        popupMenu.show();
    }

    private void setCopyMoveOptinOn() {
        Constant.pastList = new ArrayList<>();
        String externalStoragePath = Utils.getExternalStoragePath(this, true);
        File file = new File(this.displayImageList.get(this.position).getFilePath());
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
        safedk_VideoPlayActivity_startActivity_029061d18ff770871baf54113c413c97(this, intent);
    }

    private void deleteDialog() {
        this.sdCardPermissionType = 1;
        this.videoView.pause();
        final int i = this.position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String externalStoragePath = Utils.getExternalStoragePath(VideoPlayActivity.this, true);
                if (externalStoragePath == null || externalStoragePath.equalsIgnoreCase("")) {
                    VideoPlayActivity.this.setDeleteFile();
                } else if (!VideoPlayActivity.this.displayImageList.get(i).getFilePath().contains(externalStoragePath)) {
                    VideoPlayActivity.this.setDeleteFile();
                } else if (StorageUtils.checkFSDCardPermission(new File(externalStoragePath), VideoPlayActivity.this) == 2) {
                    Toast.makeText(VideoPlayActivity.this, "Please give a permission for manager operation", 0).show();
                } else {
                    VideoPlayActivity.this.setDeleteFile();
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

    
    public void setDeleteFile() {
        this.sdCardPermissionType = 1;
        int i = this.position;
        ArrayList arrayList = new ArrayList();
        File file = new File(String.valueOf(this.displayImageList.get(this.position)));
        if (StorageUtils.checkFSDCardPermission(file, this) == 2) {
            Toast.makeText(this, "Please give a permission for manager operation", 0).show();
        } else if (StorageUtils.deleteFile(file, this)) {
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            this.displayImageList.remove(this.position);
            onBackPressed();
            RxBus.getInstance().post(new DisplayDeleteEvent(arrayList));
            if (i < this.displayImageList.size() - 1) {
                this.position = i;
            } else if (this.displayImageList.size() == 0) {
                onBackPressed();
            }
            arrayList.add(file.getPath());
            RxBus.getInstance().post(new DisplayDeleteEvent(arrayList));
        } else {
            Toast.makeText(this, "Error", 0).show();
        }
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

    
    public void showDetailDialog() {
        DisplayVideoAdapter displayVideoAdapter = this.adapter;
        if (displayVideoAdapter != null) {
            displayVideoAdapter.pausevideo();
        }
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_format);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_time);
        TextView textView3 = (TextView) dialog.findViewById(R.id.txt_resolution);
        TextView textView4 = (TextView) dialog.findViewById(R.id.txt_file_size);
        TextView textView5 = (TextView) dialog.findViewById(R.id.txt_duration);
        TextView textView6 = (TextView) dialog.findViewById(R.id.txt_path);
        TextView textView7 = (TextView) dialog.findViewById(R.id.btn_ok);
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(this.displayImageList.get(this.position).getFileName());
        File file = new File(this.displayImageList.get(this.position).getFilePath());
        if (file.exists()) {
            textView.setText(Utils.getMimeTypeFromFilePath(this.displayImageList.get(this.position).getFilePath()));
            textView4.setText(Formatter.formatShortFileSize(this, file.length()));
            textView2.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm a").format(Long.valueOf(file.lastModified())));
            textView5.setText(this.duration);
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(this.displayImageList.get(this.position).getFilePath());
                String extractMetadata = mediaMetadataRetriever.extractMetadata(19);
                extractMetadata.getClass();
                int parseInt = Integer.parseInt(extractMetadata);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
                extractMetadata2.getClass();
                int parseInt2 = Integer.parseInt(extractMetadata2);
                textView3.setText(parseInt2 + "X" + parseInt);
                textView3.setVisibility(0);
                String str = this.duration;
                if (str == null && !str.equalsIgnoreCase("")) {
                    textView5.setText(getDurationString((int) Long.parseLong(mediaMetadataRetriever.extractMetadata(9))));
                    textView3.setText(parseInt2 + "X" + parseInt);
                }
            } catch (Exception e) {
                e.printStackTrace();
                textView3.setVisibility(8);
            }
        }
        textView6.setText(this.displayImageList.get(this.position).getFilePath());
        textView7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getDurationString(int i) {
        String str;
        String str2;
        String str3;
        long j = (long) i;
        long hours = TimeUnit.MILLISECONDS.toHours(j);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(j);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j));
        if (minutes < 10) {
            str = "0" + minutes;
        } else {
            str = String.valueOf(minutes);
        }
        if (seconds < 10) {
            str2 = "0" + seconds;
        } else {
            str2 = String.valueOf(seconds);
        }
        if (hours < 10) {
            str3 = "0" + hours;
        } else {
            str3 = String.valueOf(hours);
        }
        if (hours == 0) {
            return str + ":" + str2;
        }
        return str3 + ":" + str + ":" + str2;
    }

    public void onDestroy() {
        super.onDestroy();
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
                                if (deleteList.get(i).equalsIgnoreCase(VideoPlayActivity.this.displayImageList.get(VideoPlayActivity.this.position).getFilePath())) {
                                    int i2 = VideoPlayActivity.this.position;
                                    VideoPlayActivity.this.displayImageList.remove(VideoPlayActivity.this.position);
                                    VideoPlayActivity.this.adapter.notifyDataSetChanged();
                                    VideoPlayActivity videoPlayActivity = VideoPlayActivity.this;
                                    videoPlayActivity.adapter = new DisplayVideoAdapter(videoPlayActivity, videoPlayActivity.displayImageList, VideoPlayActivity.this);
                                    if (i2 < VideoPlayActivity.this.displayImageList.size() - 1) {
                                        VideoPlayActivity.this.position = i2;
                                    } else if (VideoPlayActivity.this.displayImageList.size() == 0) {
                                        VideoPlayActivity.this.onBackPressed();
                                    } else {
                                        try {
                                            VideoPlayActivity.this.onBackPressed();
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
}
