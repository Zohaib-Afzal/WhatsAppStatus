package com.jaivin.saver.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jaivin.saver.FullscreenActivity;
import com.jaivin.saver.R;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DS on 12/12/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Activity activity;
    private File file;
    ArrayList<DataModel> mData;
    String type;

    public DownloadAdapter(Activity paramActivity, ArrayList<DataModel> paramArrayList, String type) {
        this.mData = paramArrayList;
        this.activity = paramActivity;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataModel jpast = (DataModel) this.mData.get(position);
        this.file = new File(jpast.getFilePath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imagevi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imagevi);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private CardView cardView;
        private ImageView imagePlayer;
        private ImageView imagevi;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imagevi = ((ImageView) itemView.findViewById(R.id.imageView));
            this.imagePlayer = ((ImageView) itemView.findViewById(R.id.iconplayer));
            this.cardView = ((CardView) itemView.findViewById(R.id.card_view));
            this.cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, FullscreenActivity.class);
            intent.putParcelableArrayListExtra("images", mData);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("type", type);
            intent.putExtra("statusdownload", "app");
            activity.startActivity(intent);
        }
    }
}
