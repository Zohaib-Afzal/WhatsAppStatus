package com.jaivin.saver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DS on 13/12/2017.
 */

public class Utils {
    public static void mf(File file, File file1)
            throws IOException {
        File localFile = new File(file1, file.getName());
        FileChannel localFileChannel1 = null;
        FileChannel localFileChannel2 = null;
        try {
            localFileChannel1 = new FileOutputStream(localFile).getChannel();
            localFileChannel2 = new FileInputStream(file).getChannel();
            localFileChannel2.transferTo(0L, localFileChannel2.size(), localFileChannel1);
            localFileChannel2.close();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (localFileChannel2 != null) {
                localFileChannel2.close();
            }
            if (localFileChannel1 != null) {
                localFileChannel1.close();
            }
        }
    }

    private static boolean doesPackageExist(String targetPackage, Context context) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void mPlayer(String filepath, Activity activity) {
        File file = new File(filepath);
        Intent intent;
        if (!Utils.getBack(filepath, "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                if (doesPackageExist("com.google.android.gallery3d", activity)) {
                    intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.android.gallery3d", activity)) {
                    intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.cooliris.media", activity)) {
                    intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                try {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                    activity.startActivity(intent);
                } catch (Exception e2) {
                    Toast.makeText(activity, "Sorry, Play video not working properly , try again!", Toast.LENGTH_LONG).show();
                }
            }
        } else if (!Utils.getBack(filepath, "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            activity.startActivity(intent);
        } else if (!Utils.getBack(filepath, "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                activity.startActivity(intent);
            } catch (Exception e3) {
                Toast.makeText(activity, "Sorry. We can't Display Images. try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String getBack(String paramString1, String paramString2) {
        Matcher localMatcher = Pattern.compile(paramString2).matcher(paramString1);
        if (localMatcher.find()) {
            return localMatcher.group(1);
        }
        return "";
    }

    public static void mShare(String filepath, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(String.valueOf(filepath)));
        File file = new File(filepath);
        if (filepath.contains(".mp4")) {
            intent.setDataAndType(Uri.fromFile(file), "video/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            activity.startActivity(Intent.createChooser(intent, "Share video using"));
        } else {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            activity.startActivity(Intent.createChooser(intent, "Share Image using"));
        }
    }

    public static void mShareText(String text, Activity activity) {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(myapp);
    }
}
