package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.io.filemanager.freefileexplorer.easily.App.activity.VideoPlayActivity;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.oncliclk.VideoPreviousNext;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DisplayVideoAdapter extends PagerAdapter {
    Context context;
    Handler h;
    List<PhotoData> imageList = new ArrayList();
    boolean isFirst = false;
    LayoutInflater layoutInflater;
    public Runnable onEverySecond;
    Runnable run;
    VideoPreviousNext videoPreviousNext;
    VideoView videoView;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public DisplayVideoAdapter(Context context2, List<PhotoData> list, VideoPreviousNext videoPreviousNext2) {
        this.context = context2;
        this.imageList = list;
        this.videoPreviousNext = videoPreviousNext2;
        this.layoutInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getCount() {
        Log.i("TAG", "getCount: imageList:" + this.imageList.size());
        return this.imageList.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        final int i2 = i;
        View inflate = this.layoutInflater.inflate(R.layout.item_video_play, viewGroup, false);
        CardView cardView = (CardView) inflate.findViewById(R.id.btn_play_pause);
        final ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_center_play_pause);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.previous);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.next);
        ImageView imageView4 = (ImageView) inflate.findViewById(R.id.ic_lock);
        this.videoView = (VideoView) inflate.findViewById(R.id.video_view);
        final SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.video_seek);
        final AppCompatTextView appCompatTextView = (AppCompatTextView) inflate.findViewById(R.id.txt_video_current_dur);
        final AppCompatTextView appCompatTextView2 = (AppCompatTextView) inflate.findViewById(R.id.txt_video_total_dur);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.lout_bottom);
        this.run = new Runnable() {
            public void run() {
                VideoPlayActivity.toolbar.setVisibility(8);
            }
        };
        this.h = new Handler();
        this.onEverySecond = new Runnable() {
            public void run() {
                //SeekBar seekBar = seekBar;
                if (seekBar != null) {
                    seekBar.setProgress(DisplayVideoAdapter.this.videoView.getCurrentPosition());
                    int currentPosition = DisplayVideoAdapter.this.videoView.getCurrentPosition() / 1000;
                    int i = currentPosition / 3600;
                    int i2 = (currentPosition / 60) - (i * 60);
                    appCompatTextView.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf((currentPosition - (i * 3600)) - (i2 * 60))}));
                }
                seekBar.postDelayed(DisplayVideoAdapter.this.onEverySecond, 1000);
                if (DisplayVideoAdapter.this.videoView.isPlaying()) {
                    imageView.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_pause));
                } else {
                    imageView.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_play));
                }
            }
        };
        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DisplayVideoAdapter.this.pause(imageView);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DisplayVideoAdapter.this.videoPreviousNext.OnNext(i2);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DisplayVideoAdapter.this.videoPreviousNext.OnPrevious(i2);
            }
        });
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                imageView.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_pause));
                seekBar.setMax(DisplayVideoAdapter.this.videoView.getDuration());
                int duration = mediaPlayer.getDuration() / 1000;
                int i = duration / 3600;
                int i2 = (duration / 60) - (i * 60);
                appCompatTextView2.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf((duration - (i * 3600)) - (i2 * 60))}));
                seekBar.postDelayed(DisplayVideoAdapter.this.onEverySecond, 1000);
            }
        });
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                imageView.setVisibility(0);
                imageView.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_play));
                DisplayVideoAdapter.this.videoPreviousNext.OnNext(i2);
            }
        });
        //AnonymousClass8 r13 = r0;
        final SeekBar seekBar2 = seekBar;
        AppCompatTextView appCompatTextView3 = appCompatTextView2;
        final ImageView imageView5 = imageView4;
        AppCompatTextView appCompatTextView4 = appCompatTextView;
        SeekBar seekBar3 = seekBar;
        final LinearLayout linearLayout2 = linearLayout;
        ImageView imageView6 = imageView4;
        final AppCompatTextView appCompatTextView5 = appCompatTextView3;
        ImageView imageView7 = imageView3;
        final ImageView imageView8 = imageView2;
        ImageView imageView9 = imageView2;
        final ImageView imageView10 = imageView7;
        final ImageView imageView11 = imageView;
        final CardView cardView2 = cardView;
        View.OnClickListener r0 = new View.OnClickListener() {
            public void onClick(View view) {
                if (seekBar2.getVisibility() == 0) {
                    imageView5.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_unlock));
                    seekBar2.setVisibility(4);
                    appCompatTextView.setVisibility(4);
                    linearLayout2.setBackgroundColor(DisplayVideoAdapter.this.context.getResources().getColor(R.color.transparent));
                    appCompatTextView5.setVisibility(4);
                    imageView8.setVisibility(4);
                    imageView10.setVisibility(4);
                    cardView2.setVisibility(4);
                    VideoPlayActivity.toolbar.setVisibility(8);
                    return;
                }
                imageView5.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_loack));
                seekBar2.setVisibility(0);
                linearLayout2.setBackground(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.bg_transparent_gradient));
                appCompatTextView.setVisibility(0);
                appCompatTextView5.setVisibility(0);
                imageView5.setVisibility(0);
                imageView8.setVisibility(0);
                imageView10.setVisibility(0);
                cardView2.setVisibility(0);
                VideoPlayActivity.toolbar.setVisibility(0);
            }
        };
        imageView6.setOnClickListener(r0);
        final ImageView imageView12 = imageView6;
        final LinearLayout linearLayout3 = linearLayout;
        final SeekBar seekBar4 = seekBar3;
        final AppCompatTextView appCompatTextView6 = appCompatTextView4;
        final ImageView imageView13 = imageView9;
        inflate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (imageView12.getVisibility() == 0) {
                    linearLayout3.setBackgroundColor(DisplayVideoAdapter.this.context.getResources().getColor(R.color.transparent));
                    seekBar4.setVisibility(4);
                    appCompatTextView6.setVisibility(4);
                    appCompatTextView5.setVisibility(4);
                    imageView12.setVisibility(4);
                    imageView13.setVisibility(4);
                    imageView10.setVisibility(4);
                    cardView2.setVisibility(4);
                    VideoPlayActivity.toolbar.setVisibility(8);
                    return;
                }
                seekBar4.setVisibility(0);
                appCompatTextView6.setVisibility(0);
                linearLayout3.setBackground(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.bg_transparent_gradient));
                appCompatTextView5.setVisibility(0);
                imageView12.setVisibility(0);
                imageView13.setVisibility(0);
                imageView10.setVisibility(0);
                cardView2.setVisibility(0);
                VideoPlayActivity.toolbar.setVisibility(0);
            }
        });
        final AppCompatTextView appCompatTextView7 = appCompatTextView4;
        final SeekBar seekBar5 = seekBar3;
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                DisplayVideoAdapter.this.videoView.start();
                imageView11.setImageDrawable(DisplayVideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_pause));
                if (DisplayVideoAdapter.this.videoView.isPlaying()) {
                    seekBar5.postDelayed(DisplayVideoAdapter.this.onEverySecond, 1000);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                DisplayVideoAdapter.this.videoView.pause();
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int currentPosition = DisplayVideoAdapter.this.videoView.getCurrentPosition() / 1000;
                int i2 = currentPosition / 3600;
                int i3 = (currentPosition / 60) - (i2 * 60);
                appCompatTextView7.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(i3), Integer.valueOf((currentPosition - (i2 * 3600)) - (i3 * 60))}));
                if (z) {
                    DisplayVideoAdapter.this.videoView.seekTo(i);
                }
            }
        });
        seekBar5.setProgress(0);
        StringBuilder sb = new StringBuilder();
        sb.append("instantiateItem: ");
        int i3 = i;
        sb.append(this.imageList.get(i3).getFilePath());
        Log.i("TAG", sb.toString());
        this.videoView.setVideoPath(this.imageList.get(i3).getFilePath());
        this.videoView.requestFocus();
        this.videoView.start();
        Log.i("TAG", "instantiateItem: " + Constant.isbackImage);
        viewGroup.addView(inflate);
        return inflate;
    }

    public void pause(ImageView imageView) {
        if (this.videoView.isPlaying()) {
            imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_play));
            this.videoView.pause();
            return;
        }
        imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_pause));
        this.videoView.start();
        this.h.removeCallbacks(this.run);
        this.h.postDelayed(this.run, 2000);
    }

    public Bitmap rotateImage(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
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

    public void setvideoshow() {
        VideoView videoView2 = this.videoView;
        if (videoView2 != null) {
            videoView2.seekTo(1);
        }
    }

    public void pausevideo() {
        VideoView videoView2 = this.videoView;
        if (videoView2 != null && videoView2.isPlaying()) {
            this.videoView.pause();
        }
    }

    public void playvideo() {
        VideoView videoView2 = this.videoView;
        if (videoView2 != null && !videoView2.isPlaying()) {
            this.videoView.start();
        }
    }
}
