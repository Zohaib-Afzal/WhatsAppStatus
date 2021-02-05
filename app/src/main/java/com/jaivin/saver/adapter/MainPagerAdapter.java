package com.jaivin.saver.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jaivin.saver.fragment.DownloadFragment;
import com.jaivin.saver.fragment.StatusFragment;

import java.util.ArrayList;

/**
 * Created by DS on 12/12/2017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> mTabHeader;

    public MainPagerAdapter(FragmentManager fm, ArrayList<String> paramArrayList) {
        super(fm);
        this.mTabHeader = paramArrayList;
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new StatusFragment();
        } else {
            return new DownloadFragment();
        }
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeader.get(position);
    }
}
