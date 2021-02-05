package com.jaivin.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;

import com.jaivin.saver.adapter.StatusAdapter;
import com.jaivin.saver.adapter.TopAdapter;
import com.jaivin.saver.adapter.TopAdapters;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.model.TopItems;
import com.jaivin.saver.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    TopAdapter mAdapter;
    ArrayList<TopItems> statusImageList;
    public String folderName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.status);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        statusImageList = new ArrayList<>();
        File file;
        folderName = "WhatsApp/Media/.Statuses";
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
        if (preferences.getString("WhatsApp/Media/.Statuses", "DEFAULT").equals("DEFAULT")) {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + this.folderName + File.separator);
        } else {
            file = new File(preferences.getString("path", "DEFAULT"));
        }
        statusImageList.clear();

        if (!file.isDirectory()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                displayfiles(file, recyclerView);
            }
        } else {
            displayfiles(file, recyclerView);
        }
    }




        public static File[] dirListByAscendingDate(File folder) {
            if (!folder.isDirectory()) {
                return null;
            }
            File[] sortedByDate = folder.listFiles();
            if (sortedByDate == null || sortedByDate.length <= 1) {
                return sortedByDate;
            }
            Arrays.sort(sortedByDate, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return (int) (object1.lastModified() > object2.lastModified() ? object1.lastModified() : object2.lastModified());

                }
            });
            return sortedByDate;
        }


        void displayfiles(File file, final RecyclerView mRecyclerView) {
            File[] listfilemedia = dirListByAscendingDate(file);

            int i = 0;
            while (i < listfilemedia.length) {

                if (!(listfilemedia[i].isDirectory() || Utils.getBack(listfilemedia[i].getAbsolutePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty())) {
                    statusImageList.add(new TopItems(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
                }
                i++;
            }
//            if (buttonimages.isChecked()) {
                mAdapter = new TopAdapter(statusImageList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//            }

//            buttonimages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b) {
//                        mAdapter = new StatusAdapter(getActivity(), statusImageList, "image");
//                        mRecyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                        //  loadInterstitialAdMob();
//                    }
//                }
//            });
//            buttonVideos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b) {
//                        mAdapter = new StatusAdapter(getActivity(), statusVideoList, "video");
//                        mRecyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                        // loadInterstitialAdMob();
//                    }
//                }
//            });
        }




}