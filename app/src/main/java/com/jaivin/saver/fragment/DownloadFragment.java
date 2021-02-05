package com.jaivin.saver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jaivin.saver.adapter.DownloadAdapter;
import com.jaivin.saver.R;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by DS on 12/12/2017.
 */

public class DownloadFragment extends Fragment {

    File file;
    public String folderName = "";
    ArrayList<DataModel> downloadImageList = new ArrayList<>();
    ArrayList<DataModel> downloadVideoList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    TextView isEmptyList;
    RadioButton buttonimages, buttonVideos;
    DownloadAdapter mAdapter;

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.fragment_download, paramViewGroup, false);
        folderName = getResources().getString(R.string.foldername);
        mRecyclerView = view.findViewById(R.id.my_recycler_view_1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        isEmptyList = view.findViewById(R.id.isEmptyList);
        buttonimages = view.findViewById(R.id.buttonimages);
        buttonVideos = view.findViewById(R.id.buttonVideos);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMedia();
    }

    public void loadMedia() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
        if (preferences.getString("path", "DEFAULT").equals("DEFAULT")) {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator);
        } else {
            file = new File(preferences.getString("path", "DEFAULT"));
        }
        downloadImageList.clear();
        downloadVideoList.clear();
        if (!file.isDirectory()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                displayfiles(file, mRecyclerView);
            }
        } else {
            displayfiles(file, mRecyclerView);
        }
    }

    void displayfiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        if (listfilemedia.length != 0) {
            isEmptyList.setVisibility(View.GONE);
        } else {
            isEmptyList.setVisibility(View.VISIBLE);
        }
        int i = 0;
        while (i < listfilemedia.length) {
            if (!(listfilemedia[i].isDirectory() || Utils.getBack(listfilemedia[i].getAbsolutePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty())) {
                downloadImageList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
            } else if (!(listfilemedia[i].isDirectory() || Utils.getBack(listfilemedia[i].getAbsolutePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty())) {
                downloadVideoList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
            }
            i++;
        }

        if (buttonimages.isChecked()) {
            if (downloadImageList.size() > 0) {
                isEmptyList.setVisibility(View.GONE);
            } else {
                isEmptyList.setVisibility(View.VISIBLE);
            }
            mAdapter = new DownloadAdapter(getActivity(), downloadImageList, "image");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            if (downloadVideoList.size() > 0) {
                isEmptyList.setVisibility(View.GONE);
            } else {
                isEmptyList.setVisibility(View.VISIBLE);
            }
            mAdapter = new DownloadAdapter(getActivity(), downloadVideoList, "video");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        buttonimages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (downloadImageList.size() > 0) {
                        isEmptyList.setVisibility(View.GONE);
                    } else {
                        isEmptyList.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new DownloadAdapter(getActivity(), downloadImageList, "image");
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        buttonVideos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (downloadVideoList.size() > 0) {
                        isEmptyList.setVisibility(View.GONE);
                    } else {
                        isEmptyList.setVisibility(View.VISIBLE);
                    }
                    mAdapter = new DownloadAdapter(getActivity(), downloadVideoList, "video");
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
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
}
