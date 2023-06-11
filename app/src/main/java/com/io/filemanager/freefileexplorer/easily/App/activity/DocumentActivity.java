package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.App.fragment.AllDocumentFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.ExcelFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.PdfFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.PptFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.TextFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.WordFragment;
import com.io.filemanager.freefileexplorer.easily.App.fragment.XmlFragment;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.event.DocumentCloseEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentFavouriteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentSelectEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentSortEvent;
import com.io.filemanager.freefileexplorer.easily.oncliclk.BottomListner;
import com.io.filemanager.freefileexplorer.easily.oncliclk.OnSelectedHome;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
/*import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;*/
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class DocumentActivity extends AppCompatActivity implements OnSelectedHome {
    boolean isCheckAll = false;
    @BindView(R.id.iv_check_all)
    ImageView ivCheckAll;
    @BindView(R.id.iv_close)
    AppCompatImageView ivClose;
    @BindView(R.id.iv_fav_fill)
    ImageView ivFavFill;
    @BindView( R.id.iv_fav_unfill)
    ImageView ivFavUnfill;
    @BindView(R.id.iv_more)
    AppCompatImageView ivMore;
    @BindView(R.id.iv_uncheck)
    ImageView ivUncheck;
    @BindView(R.id.ll_check_all)
    RelativeLayout llCheckAll;
    @BindView(R.id.ll_favourite)
    RelativeLayout llFavourite;
    @BindView(R.id.ll_banner)
    FrameLayout ll_banner;
    @BindView(R.id.lout_selected)
    RelativeLayout loutSelected;
    @BindView(R.id.lout_toolbar)
    RelativeLayout loutToolbar;
    int selected_Item = 0;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    int tabPos = 0;
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.txt_select)
    AppCompatTextView txtSelect;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_document);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        intView();

    }


    public void intView() {
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        TabLayout tabLayout2 = this.tabLayout;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "All"));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "PDF"));
        TabLayout tabLayout4 = this.tabLayout;
        tabLayout4.addTab(tabLayout4.newTab().setText((CharSequence) "Word"));
        TabLayout tabLayout5 = this.tabLayout;
        tabLayout5.addTab(tabLayout5.newTab().setText((CharSequence) "Excel"));
        TabLayout tabLayout6 = this.tabLayout;
        tabLayout6.addTab(tabLayout6.newTab().setText((CharSequence) "PPT"));
        TabLayout tabLayout7 = this.tabLayout;
        tabLayout7.addTab(tabLayout7.newTab().setText((CharSequence) "Text"));
        TabLayout tabLayout8 = this.tabLayout;
        tabLayout8.addTab(tabLayout8.newTab().setText((CharSequence) "Xml"));
        this.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this.tabLayout.getTabCount()));
        this.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.viewpager.setOffscreenPageLimit(7);
        this.tabLayout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                DocumentActivity.this.viewpager.setCurrentItem(tab.getPosition());
                if (DocumentActivity.this.loutSelected.getVisibility() == 0) {
                    DocumentActivity.this.setClose();
                }
                DocumentActivity.this.tabPos = tab.getPosition();
                DocumentActivity.this.isCheckAll = false;
            }
        });
    }

    public void onBackPressed() {
        if (this.loutSelected.getVisibility() == 0) {
            setClose();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_close, R.id.iv_more, R.id.ll_check_all, R.id.ll_favourite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back /*2131296682*/:
                onBackPressed();
                return;
            case R.id.iv_close /*2131296689*/:
                setClose();
                return;
            case R.id.iv_more /*2131296703*/:
                setMoreMenu();
                return;
            case R.id.ll_check_all /*2131296746*/:
                setCheckAll();
                return;
            case R.id.ll_favourite /*2131296750*/:
                setFavouriteData();
                return;
            default:
                return;
        }
    }

    private void setFavouriteData() {
        if (this.selected_Item != 0) {
            String str = null;
            int i = this.tabPos;
            boolean z = true;
            if (i == 0) {
                str = "ALLDoc";
            } else if (i == 1) {
                str = "Pdf";
            } else if (i == 2) {
                str = "Word";
            } else if (i == 3) {
                str = "Excel";
            } else if (i == 4) {
                str = "Ppt";
            } else if (i == 5) {
                str = "Text";
            } else if (i == 6) {
                str = "Xml";
            }
            if (this.ivFavFill.getVisibility() == 0) {
                z = false;
            }
            RxBus.getInstance().post(new DocumentFavouriteEvent(str, z));
        }
    }

    public void OnSelected(boolean z, boolean z2, int i) {
        if (z) {
            this.loutToolbar.setVisibility(0);
        } else {
            this.loutToolbar.setVisibility(8);
        }
        this.selected_Item = i;
        if (z2) {
            this.loutSelected.setVisibility(0);
        } else {
            this.loutSelected.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
    }

    public void OnSelected(boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5) {
        if (z) {
            this.loutToolbar.setVisibility(0);
        } else {
            this.loutToolbar.setVisibility(8);
        }
        this.selected_Item = i;
        if (z2) {
            this.loutSelected.setVisibility(0);
        } else {
            this.loutSelected.setVisibility(8);
        }
        if (z5) {
            this.llFavourite.setVisibility(0);
            if (z3) {
                this.ivFavFill.setVisibility(0);
            } else {
                this.ivFavFill.setVisibility(8);
            }
            if (z4) {
                this.ivFavUnfill.setVisibility(0);
            } else {
                this.ivFavUnfill.setVisibility(8);
            }
        } else {
            this.llFavourite.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
    }

    public void setMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.storage_menu, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menu_hidden).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_create_folder).setVisible(false);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.menu_sort) {
                    return false;
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(DocumentActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
                return false;
            }
        });
        popupMenu.show();
    }

    private void setCheckAll() {
        int i = this.tabPos;
        String str = i == 0 ? "ALLDoc" : i == 1 ? "Pdf" : i == 2 ? "Word" : i == 3 ? "Excel" : i == 4 ? "Ppt" : i == 5 ? "Text" : i == 6 ? "Xml" : null;
        if (str != null && !str.equalsIgnoreCase("")) {
            if (this.isCheckAll) {
                this.isCheckAll = false;
                this.ivCheckAll.setVisibility(8);
            } else {
                this.isCheckAll = true;
                this.ivCheckAll.setVisibility(0);
            }
            RxBus.getInstance().post(new DocumentSelectEvent(str, this.isCheckAll));
        }
    }

    
    public void setClose() {
        int i = this.tabPos;
        String str = i == 0 ? "ALLDoc" : i == 1 ? "Pdf" : i == 2 ? "Word" : i == 3 ? "Excel" : i == 4 ? "Ppt" : i == 5 ? "Text" : i == 6 ? "Xml" : null;
        this.selected_Item = 0;
        if (str != null && !str.equalsIgnoreCase("")) {
            RxBus.getInstance().post(new DocumentCloseEvent(str));
            this.loutSelected.setVisibility(8);
            this.loutToolbar.setVisibility(0);
            this.isCheckAll = false;
            this.ivCheckAll.setVisibility(8);
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        public ViewPagerAdapter(FragmentManager fragmentManager, int i) {
            super(fragmentManager);
            this.tabCount = i;
        }

        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new AllDocumentFragment(DocumentActivity.this);
                case 1:
                    return new PdfFragment(DocumentActivity.this);
                case 2:
                    return new WordFragment(DocumentActivity.this);
                case 3:
                    return new ExcelFragment(DocumentActivity.this);
                case 4:
                    return new PptFragment(DocumentActivity.this);
                case 5:
                    return new TextFragment(DocumentActivity.this);
                case 6:
                    return new XmlFragment(DocumentActivity.this);
                default:
                    return null;
            }
        }

        public int getCount() {
            return this.tabCount;
        }
    }

    public static class BottomSheetFragment extends BottomSheetDialogFragment {
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
            setStyle(0, R.style.CustomBottomSheetDialogTheme);
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
                    int i = 0;
                    int i2 = 1;
                    boolean z = checkedRadioButtonId == R.id.radio_ascending;
                    if (checkedRadioButtonId2 != R.id.radio_Name_Asc) {
                        if (checkedRadioButtonId2 == R.id.radio_Time_Asc) {
                            i = z ? 6 : 5;
                        } else if (checkedRadioButtonId2 == R.id.radio_Size_Asc) {
                            i = z ? 4 : 3;
                        }
                        i2 = i;
                    } else if (!z) {
                        i2 = 2;
                    }
                    RxBus.getInstance().post(new DocumentSortEvent(i2));
                    PreferencesManager.saveToSortType(BottomSheetFragment.this.getActivity(), i2);
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
}
