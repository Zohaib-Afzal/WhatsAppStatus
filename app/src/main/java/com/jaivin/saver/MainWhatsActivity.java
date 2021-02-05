package com.jaivin.saver;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.jaivin.saver.adapter.MainPagerAdapter;
import com.jaivin.saver.fragment.DownloadFragment;
import com.jaivin.saver.fragment.StatusFragment;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Constants;
import com.jaivin.saver.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.onesignal.OneSignal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainWhatsActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {

    private static final int REQUEST_PERMISSIONS = 20;
    private static final int STORAGE_PERMISSION_CODE = 1;
    public static String filepath = "";
    boolean doubleBackToExitPressedOnce = false;
    MainPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout linearAdsBanner, linearFacebookAds;
    AdView mAdView, adView;
    com.facebook.ads.AdView fbAdView;
    InterstitialAd interstitialAdMob;
    com.facebook.ads.InterstitialAd interstitialAd;
    private int PermCheck = 20;
    Button buttonSelectAll, buttonSave;
    ArrayList<DataModel> dataList = new ArrayList<>();
    boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_whats);
        linearAdsBanner = findViewById(R.id.linearBannerAds);
        addBannerAds();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        //  toolbar.setNavigationIcon(R.drawable.logo);
        reqPermission();

        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
        if (sharedPref.getBoolean("isFistTime", true)) {
            String mBaseFolderPath = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.foldername) + File.separator;
            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isFistTime", false);
            editor.putString("path", mBaseFolderPath);
            editor.commit();
        }
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add(getResources().getString(R.string.tab_home));
        tabs.add(getResources().getString(R.string.tab_download));
        viewPager = findViewById(R.id.view_pager_main);
        buttonSave = findViewById(R.id.button_save);
        buttonSelectAll = findViewById(R.id.button_select_all);
        viewPager.setOffscreenPageLimit(3);
        adapter = new MainPagerAdapter(getSupportFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        onClickListeners();

    }

    void addBannerAds() {
        if (Constants.isNetworkConnected(MainWhatsActivity.this)) {
            //if (linearAdsBanner.getChildCount() <= 0) {
            mAdView = new AdView(MainWhatsActivity.this);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(Constants.ADS_ADMOB_BANNER_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            linearAdsBanner.addView(mAdView);
            mAdView.loadAd(adRequest);
            //}
        }
    }

    private void onClickListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager_main + ":" + viewPager.getCurrentItem());
                if (fragment instanceof DownloadFragment) {
                    ((DownloadFragment) fragment).loadMedia();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buttonSelectAll.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager_main + ":" + viewPager.getCurrentItem());
            if (fragment instanceof StatusFragment) {
                dataList =  ((StatusFragment) fragment).getDataList();
                int counter = 0;
                isAllSelected = !isAllSelected;
                for (DataModel dataModel:dataList) {
                    if(isAllSelected){
                        dataModel.setChecked(true);
                    } else {
                        dataModel.setChecked(false);
                    }
                    dataList.set(counter, dataModel);
                    counter = counter+1;
                }
                ((StatusFragment) fragment).updateData();
            }

        });

        buttonSave.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager_main + ":" + viewPager.getCurrentItem());
            if (fragment instanceof StatusFragment) {
                dataList =  ((StatusFragment) fragment).getDataList();
                ((StatusFragment) fragment).saveStatus(dataList);
            }

        });

    }


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
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.setting_guide:
                Intent localIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
                    try {
                        startActivity(localIntent);
                        return true;
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                    }
                } else {
                    Toast.makeText(this, "Whatsapp not install in your device!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return true;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    @Override
    public void onBackPressed() {
        ShowDialogue();
    }

    private void ShowDialogue() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(
                MainWhatsActivity.this);
        alert.setTitle(getString(R.string.app_name));
        // alert.setIcon(R.drawable.icon);
        alert.setMessage("Hey Wait!:");
        alert.setMessage("Rate Us If You Liked the App, \nProbably a 5 Star");
        alert.setNegativeButton("Rate App",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


                        final String appName = getPackageName();//your application package name i.e play store application url
                        Log.e("package:", appName);
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id="
                                            + appName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                            + appName)));
                        }
                    }
                });
        alert.setPositiveButton("No",
                (dialog, whichButton) -> {

                    finish();
                    //    loadInterstitialAdMob();
                });
        alert.show();
    }


    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}).setCallback(this).setErrorCallback(this).request(REQUEST_PERMISSIONS);
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage("We need permissions for this app.");
        localBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {//2131165238
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                permissionInterface.onDialogShown();
            }
        });
        localBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        localBuilder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage("We need permissions for this app. Open setting screen?");
        localBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                permissionInterface.onSettingsShown();
            }
        });
        localBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        localBuilder.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 34 || resultCode != 1) {
            return;
        }
        if (filepath.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Sorry we can't move file. try Other file!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Utils.mf(new File(filepath), new File(data.getStringExtra("selected_dir")));
            ((DownloadFragment) MainWhatsActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + 1)).loadMedia();
            Toast.makeText(this, "Moved Successful.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Sorry we can't move file. try Other file!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }
}
