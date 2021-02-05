package com.jaivin.saver;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaivin.saver.adapter.FullscreenImageAdapter;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Constants;
import com.jaivin.saver.utils.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DS on 14/12/2017.
 */

public class FullscreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<DataModel> imageList;
    int position;
    FloatingActionButton menu_save, menu_Repost, menu_share, menu_setas, menu_delete;
    FloatingActionMenu menuMain;
    FullscreenImageAdapter fullscreenImageAdapter;
    String type, statusdownload;
    LinearLayout linearFacebookBanner, linearBannerAds;
    AdView mAdView;
    com.facebook.ads.AdView fbAdView;
//    InterstitialAd mInterstitialAd;
//    com.facebook.ads.InterstitialAd interstitialAd;
    int mAdCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        linearFacebookBanner = findViewById(R.id.linearFacebookAds);
        linearBannerAds = findViewById(R.id.linearBannerAds);
        addBannnerAds();

        if (Constants.isNetworkConnected(FullscreenActivity.this) && Constants.ADS_STATUS) {
            if (Constants.ADS_TYPE.equals("admob")) {
                try {
                    //addBannnerAds();
                    int intRandom = new Random().nextInt(2);
                    if (intRandom == 1) {
                        //loadFullScreenAds();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Constants.ADS_TYPE.equals("facebook")) {
                try {
                    //addFBBannnerAds();
                    //loadFBFullscreenAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = findViewById(R.id.view_pager_full_screen);

        menuMain = findViewById(R.id.menuMain);
        menu_save = findViewById(R.id.menu_save);
        menu_Repost = findViewById(R.id.menu_Repost);
        menu_share = findViewById(R.id.menu_share);
        menu_setas = findViewById(R.id.menu_setas);
        menu_delete = findViewById(R.id.menu_delete);

        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        type = getIntent().getStringExtra("type");
        statusdownload = getIntent().getStringExtra("statusdownload");

        if (position %2 == 0){
            try {
               // loadFullScreenAds();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (type.equals("video")) {
            menu_setas.setVisibility(View.GONE);
            //loadFBFullscreenAds();
            //Toast.makeText(this, "video", Toast.LENGTH_SHORT).show();
        } else {
            menu_setas.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "image", Toast.LENGTH_SHORT).show();

        }
        if (statusdownload.equals("app")) {
            setTitle("Download");
            menu_save.setVisibility(View.GONE);
        } else {
            setTitle("Status");
            menu_save.setVisibility(View.VISIBLE);
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("deleteFab", true)) {
                menu_delete.setVisibility(View.GONE);
            }
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(FullscreenActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAdCount++;
                if (mAdCount % 3 == 0) {
                //    loadFBFullscreenAds();
                    //Display your add
                    //It's better if you load the ad before you need to display it, you can hide the adview and load it
                } else {
                    //Hide the adView
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        menu_save.setOnClickListener(clickListener);
        menu_Repost.setOnClickListener(clickListener);
        menu_share.setOnClickListener(clickListener);
        menu_setas.setOnClickListener(clickListener);
        menu_delete.setOnClickListener(clickListener);
    }

    void addBannnerAds() {
        if (Constants.isNetworkConnected(FullscreenActivity.this)) {
            //if (linearBannerAds.getChildCount() <= 0) {
                mAdView = new AdView(FullscreenActivity.this);
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(Constants.ADS_ADMOB_BANNER_ID);
                AdRequest adRequest = new AdRequest.Builder().build();
                linearBannerAds.addView(mAdView);
                mAdView.loadAd(adRequest);
            //}
        }
    }



//    void loadFullScreenAds() throws Exception {
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(Constants.ADS_ADMOB_FULLSCREEN_ID);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//        });
//        AdRequest localAdRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(localAdRequest);
//
//    }
//
//    void loadFBFullscreenAds() {
//
//        interstitialAd = new com.facebook.ads.InterstitialAd(this, Constants.ADS_FACEBOOK_FULLSCREEN_ID);
//        interstitialAd.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                if (interstitialAd.isAdLoaded()) {
//                    interstitialAd.show();
//                }
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        });
//        interstitialAd.loadAd();
//    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (fbAdView != null) {
            fbAdView.destroy();
        }
//        if (interstitialAd != null) {
//            interstitialAd.destroy();
//        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu_save:
                    menuMain.toggle(true);
                    if (imageList.size() > 0) {
                        final Storage storage = new Storage(v.getContext());
                        String path;
                        try {
                            SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
                            if (preferences.getString("path", "DEFAULT").equals("DEFAULT")) {
                                path = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.foldername) + File.separator;
                            } else {
                                path = preferences.getString("path", "DEFAULT");
                            }
                            if (!new File(path).exists()) {
                                new File(path).mkdirs();
                            }
                            storage.copy(imageList.get(viewPager.getCurrentItem()).getFilePath(), path + File.separator + new File(imageList.get(viewPager.getCurrentItem()).getFilePath()).getName());
                            Toast.makeText(FullscreenActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(FullscreenActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;
                case R.id.menu_Repost:
                    menuMain.toggle(true);
                    if (imageList.size() > 0) {
                        Parcelable uriForFile = Uri.parse(new StringBuffer().append("file://").append(new File(imageList.get(viewPager.getCurrentItem()).getFilePath()).getAbsolutePath()).toString());
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.setPackage("com.whatsapp");
                            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(FullscreenActivity.this, "WhatsApp Not Found on this Phone :(", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        finish();
                    }
                    break;
                case R.id.menu_share:
                    menuMain.toggle(true);
                    if (imageList.size() > 0) {
                        Utils.mShare(imageList.get(viewPager.getCurrentItem()).getFilePath(), FullscreenActivity.this);
                    } else {
                        finish();
                    }
                    break;
                case R.id.menu_setas:
                    menuMain.toggle(true);
                    if (imageList.size() > 0) {
                        Uri uriForFiles = Uri.parse(new StringBuffer().append("file://").append(new File(imageList.get(viewPager.getCurrentItem()).getFilePath()).getAbsolutePath()).toString());
                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.setDataAndType(uriForFiles, "image/*");
                        intent.putExtra("mimeType", "image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Set as: "));

                    } else {
                        finish();
                    }
                    break;
                case R.id.menu_delete:
                    if (imageList.size() > 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FullscreenActivity.this);
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Sure to Delete this Image?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                menuMain.toggle(true);
                                dialog.dismiss();
                                int currentItem = 0;

                                File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                                if (file.exists()) {
                                    boolean del = file.delete();
                                    if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                        currentItem = viewPager.getCurrentItem();
                                    }
                                    imageList.remove(viewPager.getCurrentItem());
                                    fullscreenImageAdapter = new FullscreenImageAdapter(FullscreenActivity.this, imageList);
                                    viewPager.setAdapter(fullscreenImageAdapter);
                                    if (imageList.size() > 0) {
                                        viewPager.setCurrentItem(currentItem);
                                    } else {
                                        finish();
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
