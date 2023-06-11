package com.io.filemanager.freefileexplorer.easily.utils;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.oncliclk.BottomListner;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    RadioGroup group_order;
    BottomListner listner;
    RadioButton radio_Name_Asc;
    RadioButton radio_Name_Des;
    RadioButton radio_Size_Asc;
    RadioButton radio_Size_Des;
    RadioButton radio_Time_Asc;
    RadioButton radio_Time_Dec;
    RadioButton radio_ascending;
    RadioGroup radio_button_group;
    RadioButton radio_descending;
    View view;

    public BottomSheetFragment() {
    }

    public BottomSheetFragment(BottomListner bottomListner) {
        this.listner = bottomListner;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Window window;
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        if (Build.VERSION.SDK_INT >= 23 && (window = onCreateDialog.getWindow()) != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            GradientDrawable gradientDrawable = new GradientDrawable();
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(getResources().getColor(R.color.white));
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable, gradientDrawable2});
            if (Build.VERSION.SDK_INT >= 23) {
                layerDrawable.setLayerInsetTop(1, displayMetrics.heightPixels);
            }
            window.setBackgroundDrawable(layerDrawable);
        }
        return onCreateDialog;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.dialog_sort, viewGroup, false);
        initView();
        return this.view;
    }

    private void initView() {
        this.radio_button_group = (RadioGroup) this.view.findViewById(R.id.radio_button_group);
        this.group_order = (RadioGroup) this.view.findViewById(R.id.group_order);
        this.radio_Name_Asc = (RadioButton) this.view.findViewById(R.id.radio_Name_Asc);
        this.radio_Name_Des = (RadioButton) this.view.findViewById(R.id.radio_Name_Des);
        this.radio_Size_Asc = (RadioButton) this.view.findViewById(R.id.radio_Size_Asc);
        this.radio_Size_Des = (RadioButton) this.view.findViewById(R.id.radio_Size_Des);
        this.radio_Time_Asc = (RadioButton) this.view.findViewById(R.id.radio_Time_Asc);
        this.radio_Time_Dec = (RadioButton) this.view.findViewById(R.id.radio_Time_Dec);
        this.radio_ascending = (RadioButton) this.view.findViewById(R.id.radio_ascending);
        this.radio_descending = (RadioButton) this.view.findViewById(R.id.radio_descending);
        LinearLayout linearLayout = (LinearLayout) this.view.findViewById(R.id.btn_cancel);
        LinearLayout linearLayout2 = (LinearLayout) this.view.findViewById(R.id.btn_done);
        int sortType = PreferencesManager.getSortType(getActivity());
        if (sortType == 1) {
            this.radio_Name_Asc.setChecked(true);
            this.radio_ascending.setChecked(true);
        } else if (sortType == 2) {
            this.radio_Name_Asc.setChecked(true);
            this.radio_descending.setChecked(true);
        } else if (sortType == 3) {
            this.radio_Size_Asc.setChecked(true);
            this.radio_descending.setChecked(true);
        } else if (sortType == 4) {
            this.radio_Size_Asc.setChecked(true);
            this.radio_ascending.setChecked(true);
        } else if (sortType == 5) {
            this.radio_Time_Asc.setChecked(true);
            this.radio_descending.setChecked(true);
        } else if (sortType == 6) {
            this.radio_Time_Asc.setChecked(true);
            this.radio_ascending.setChecked(true);
        } else {
            this.radio_Name_Asc.setChecked(true);
        }
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int checkedRadioButtonId = BottomSheetFragment.this.group_order.getCheckedRadioButtonId();
                int checkedRadioButtonId2 = BottomSheetFragment.this.radio_button_group.getCheckedRadioButtonId();
                boolean z = checkedRadioButtonId == R.id.radio_ascending;
                if (checkedRadioButtonId2 == R.id.radio_Name_Asc) {
                    if (z) {
                        BottomSheetFragment.this.listner.onBottomClick(1);
                    } else {
                        BottomSheetFragment.this.listner.onBottomClick(2);
                    }
                } else if (checkedRadioButtonId2 == R.id.radio_Time_Asc) {
                    if (z) {
                        BottomSheetFragment.this.listner.onBottomClick(6);
                    } else {
                        BottomSheetFragment.this.listner.onBottomClick(5);
                    }
                } else if (checkedRadioButtonId2 == R.id.radio_Size_Asc) {
                    if (z) {
                        BottomSheetFragment.this.listner.onBottomClick(4);
                    } else {
                        BottomSheetFragment.this.listner.onBottomClick(3);
                    }
                }
                BottomSheetFragment.this.dismiss();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BottomSheetFragment.this.dismiss();
            }
        });
    }
}
