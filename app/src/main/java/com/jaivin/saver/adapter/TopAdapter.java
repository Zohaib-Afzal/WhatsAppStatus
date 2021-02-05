package com.jaivin.saver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jaivin.saver.R;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.model.TopItems;
import com.jaivin.saver.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHoder> {
    private ArrayList<TopItems> topItems ;
    public TopAdapter(ArrayList<TopItems> topItems){
        this.topItems=topItems;
    }
    private Context context;
    @NonNull
    @Override
    public TopViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new TopViewHoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopViewHoder holder, int position) {
        File file;
        TopItems jpast = this.topItems.get(position);
        file = new File(jpast.getFilepath());
        if (!file.isDirectory()) {
            if (!Utils.getBack(jpast.getFilepath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                Glide.with(context).load(file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.holo_blue_bright).optionalTransform(new RoundedCorners(5))).into(holder.circleImageView);
            }
            else{
                holder.v.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public int getItemCount() {


    return topItems.size();
    }

    public class TopViewHoder extends RecyclerView.ViewHolder{
public CircleImageView circleImageView;
public View v;
       public TopViewHoder(@NonNull View itemView) {
           super(itemView);
           circleImageView = itemView.findViewById(R.id.profile_image);
           v=itemView;

       }
   }
}
