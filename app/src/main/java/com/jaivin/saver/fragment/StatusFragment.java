package com.jaivin.saver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jaivin.saver.FullscreenActivity;
import com.jaivin.saver.adapter.StatusAdapter;
import com.jaivin.saver.R;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Constants;
import com.jaivin.saver.utils.Utils;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class StatusFragment extends Fragment {

    File file;
    public String folderName = "";
    ArrayList<DataModel> statusImageList = new ArrayList<>();
    ArrayList<DataModel> statusVideoList = new ArrayList<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView,adRecyclerView;
    TextView isEmptyList;
    RadioButton buttonimages, buttonVideos;
    StatusAdapter mAdapter;
    InterstitialAd interstitialAdMob;
    FrameLayout layout;
    int counter = 0;
    boolean isAllSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        folderName = "WhatsApp/Media/.Statuses";
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_0);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if(mAdapter.isHeader(i)){
                    return 2;
                }
                else{
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        isEmptyList = view.findViewById(R.id.isEmptyList);
        buttonimages = view.findViewById(R.id.buttonimages);
        buttonVideos = view.findViewById(R.id.buttonVideos);

//        buttonDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (buttonimages.isChecked()) {
//                    if(!statusImageList.isEmpty()){
//                        saveStatus(statusImageList);
//                    } else {
//                        Toast.makeText(getActivity(), "No item selected", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    if(!statusVideoList.isEmpty()){
//                        saveStatus(statusVideoList);
//                    } else {
//                        Toast.makeText(getActivity(), "No item selected", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });

//        buttonSelectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(buttonimages.isChecked()){
//                    counter = 0;
//                    isAllSelected = !isAllSelected;
//                    for (DataModel dataModel:statusImageList) {
//                        if(isAllSelected){
//                            dataModel.setChecked(true);
//                        } else {
//                            dataModel.setChecked(false);
//                            buttonDownload.setVisibility(View.VISIBLE);
//                        }
//                        statusImageList.set(counter, dataModel);
//                        counter = counter+1;
//                    }
//                } else {
//                    counter = 0;
//                    isAllSelected = !isAllSelected;
//                    for (DataModel dataModel:statusVideoList) {
//                        if(isAllSelected){
//                            dataModel.setChecked(true);
//                        } else {
//                            dataModel.setChecked(false);
//                            buttonDownload.setVisibility(View.VISIBLE);
//                        }
//                        statusVideoList.set(counter, dataModel);
//                        counter = counter+1;
//                    }
//                }
//                updateData();
//                counter = 0;
//            }
//        });

        return view;
    }
    private void onClicks(){
        mAdapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
            @Override
            public void onCheckButtonClick(DataModel data, int position) {
                data.setChecked(!data.isChecked());

               // buttonSelectAll.setVisibility(View.VISIBLE);
               // buttonDownload.setVisibility(View.VISIBLE);
                if(buttonimages.isChecked()){
                    statusImageList.set(position,data);

                } else {
                    statusVideoList.set(position,data);
                }
                updateData();
            }
        });

    }
    public void updateData(){
        if (buttonimages.isChecked()) {
            mAdapter = new StatusAdapter(getActivity(), statusImageList, "image");
            onClicks();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new StatusAdapter(getActivity(), statusVideoList, "video");
            onClicks();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    public void loadMedia() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
        if (preferences.getString("WhatsApp/Media/.Statuses", "DEFAULT").equals("DEFAULT")) {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + this.folderName + File.separator);
        } else {
            file = new File(preferences.getString("path", "DEFAULT"));
        }
        statusImageList.clear();
        statusVideoList.clear();
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

    @Override
    public void onResume() {
        super.onResume();
        loadMedia();
    }

    void displayfiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        if (listfilemedia.length != 0) {
            isEmptyList.setVisibility(View.GONE);
        } else {
            isEmptyList.setVisibility(View.VISIBLE);
        }
        int i = 0;
        int count=0;
        while (i < listfilemedia.length) {

            if (!(listfilemedia[i].isDirectory() || Utils.getBack(listfilemedia[i].getAbsolutePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty())) {
                statusImageList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
            } else if (!(listfilemedia[i].isDirectory() || Utils.getBack(listfilemedia[i].getAbsolutePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty())) {
                statusVideoList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
            }
            i++;
        }
        if (buttonimages.isChecked()) {
            mAdapter = new StatusAdapter(getActivity(), statusImageList, "image");
            onClicks();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new StatusAdapter(getActivity(), statusVideoList, "video");
            onClicks();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        buttonimages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loadMedia();
                    mAdapter = new StatusAdapter(getActivity(), statusImageList, "image");
                    onClicks();
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                  //  loadInterstitialAdMob();
                }
            }
        });
        buttonVideos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loadMedia();
                    mAdapter = new StatusAdapter(getActivity(), statusVideoList, "video");
                    onClicks();
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                   // loadInterstitialAdMob();
                }
            }
        });
    }
    public void saveStatus(ArrayList<DataModel> dataList){
            if (dataList.size() > 0) {
                final Storage storage = new Storage(getActivity());
                String path;
                try {
                    SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
                    if (preferences.getString("path", "DEFAULT").equals("DEFAULT")) {
                        path = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.foldername) + File.separator;
                    } else {
                        path = preferences.getString("path", "DEFAULT");
                    }
                    if (!new File(path).exists()) {
                        new File(path).mkdirs();
                    }
                    counter = 0;
                    for (DataModel dataModel : dataList) {
                        if (dataModel.isChecked()) {
                            storage.copy(dataList.get(counter).getFilePath(), path + File.separator + new File(dataList.get(counter).getFilePath()).getName());
                        }
                        counter = counter + 1;
                    }
                    Toast.makeText(getActivity(), "Saved successfully!", Toast.LENGTH_LONG).show();
                    loadMedia();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                }
            } else {
                //finish();
            }
        }

        //to send data to main activity to save and select media from top navigation buttons
        public ArrayList<DataModel> getDataList(){
            if (buttonimages.isChecked()) {
                if(!statusImageList.isEmpty()){
                    return statusImageList;
                }
            } else {
                if(!statusVideoList.isEmpty()){
                    return statusVideoList;
                }
            }
            return statusImageList;
        }

}



//    private void loadInterstitialAdMob() {
//        interstitialAdMob = new InterstitialAd(getContext());
//        interstitialAdMob.setAdUnitId(Constants.ADS_ADMOB_FULLSCREEN_ID);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        interstitialAdMob.loadAd(adRequest);
//        interstitialAdMob.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                interstitialAdMob.show();
//            }
//        });
//    }


