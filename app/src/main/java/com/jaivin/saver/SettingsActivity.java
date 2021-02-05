package com.jaivin.saver;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.LinearLayout;

import com.jaivin.saver.fragment.SettingsFragment;
import com.jaivin.saver.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class SettingsActivity extends AppCompatActivity {

    LinearLayout linearAdsBanner;
    AdView mAdView;
    com.facebook.ads.AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        linearAdsBanner = findViewById(R.id.linearAds);
        if (Constants.isNetworkConnected(SettingsActivity.this) && Constants.ADS_STATUS) {
            if (Constants.ADS_TYPE.equals("admob")) {
                try {
                    addBannnerAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Constants.ADS_TYPE.equals("facebook")) {
                try {
                  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.contents, new SettingsFragment()).commit();
    }

    void addBannnerAds() {
        if (Constants.isNetworkConnected(SettingsActivity.this)) {
            if (linearAdsBanner.getChildCount() <= 0) {
                mAdView = new AdView(SettingsActivity.this);
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(Constants.ADS_ADMOB_BANNER_ID);
                AdRequest adRequest = new AdRequest.Builder().build();
                linearAdsBanner.addView(mAdView);
                mAdView.loadAd(adRequest);
            }
        }
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
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
