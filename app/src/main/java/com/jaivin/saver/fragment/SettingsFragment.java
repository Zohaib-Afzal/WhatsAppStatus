package com.jaivin.saver.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaivin.saver.R;
import com.jaivin.saver.utils.Utils;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;

/**
 * Created by DS on 12/12/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    int REQUEST_DIRECTORY = 198;
    private String mBaseFolderPath;
    Preference moreapp;
    Preference path;
    Preference rateus;
    Preference sharethisapp;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                SharedPreferences.Editor localEditor = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), Context.MODE_PRIVATE).edit();
                localEditor.putString("path", data.getStringExtra("selected_dir"));
                localEditor.commit();
                path.setSummary(data.getStringExtra("selected_dir"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        rateus = findPreference("rateus");
        sharethisapp = findPreference("sharethisapp");
        moreapp = findPreference("moreapp");
        path = findPreference("path");
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.pref_appname), 0);
        String folderName = getResources().getString(R.string.foldername);
        if (preferences.getString("path", "DEFAULT").equals("DEFAULT")) {
            mBaseFolderPath = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator;
        } else {
            mBaseFolderPath = preferences.getString("path", "DEFAULT");
        }
        path.setSummary(mBaseFolderPath);
        path.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Intent chooserIntent = new Intent(getActivity(), DirectoryChooserActivity.class);
                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .newDirectoryName(getResources()
                                .getString(R.string.foldername))
                        .allowReadOnlyDirectory(true)
                        .allowNewDirectoryNameModification(true)
                        .build();
                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
                return true;
            }
        });
        rateus.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                return true;
            }
        });
        sharethisapp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Utils.mShareText("Hey! Can you check out this app? I really Loved It\n https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " \n", getActivity());
                return true;
            }
        });
        moreapp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Success+Point")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}
