package com.jaivin.saver.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.jaivin.saver.fragment.DownloadFragment;

import java.io.File;

/**
 * Created by DS on 12/12/2017.
 */

public class RenameDialog extends DialogFragment {

    String extension = "";
    String filename = "";
    String name = "";
    String split = "";

    public static RenameDialog newInstance(String paramString) {
        RenameDialog localdialog_rename = new RenameDialog();
        Bundle localBundle = new Bundle();
        localBundle.putString("filename", paramString);
        localdialog_rename.setArguments(localBundle);
        return localdialog_rename;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText input = new EditText(getActivity());
        input.setLayoutParams(new LayoutParams(-1, -1));
        this.filename = getArguments().getString("filename");
        this.split = Utils.getBack(this.filename, "([^/]+$)");
        try {
            this.extension = this.split.substring(this.split.lastIndexOf(".") + 1);
            this.name = this.split.substring(0, this.split.lastIndexOf("."));
        } catch (Exception e) {
            this.name = "";
            this.extension = "emptyorerror";
            Toast.makeText(getActivity(), "There is Issue to change file name. try again!", Toast.LENGTH_LONG).show();
        }
        input.setText(this.name);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle((CharSequence) "Change Name :");
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton((CharSequence) "Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!RenameDialog.this.extension.equals("emptyorerror") && !RenameDialog.this.name.equals(input.getText().toString())) {
                    try {
                        new File(RenameDialog.this.filename).renameTo(new File(RenameDialog.this.filename.replaceAll(RenameDialog.this.split, "") + input.getText().toString() + "." + RenameDialog.this.extension));
                        ((DownloadFragment) RenameDialog.this.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:2131558511:1")).loadMedia();
                        Toast.makeText(RenameDialog.this.getActivity(), "Renamed Successful.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(RenameDialog.this.getActivity(), "There is Issue to change file name. try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        alertDialogBuilder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return alertDialogBuilder.create();

    }
}
