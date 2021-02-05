package com.jaivin.saver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.jaivin.saver.FullscreenActivity;
import com.jaivin.saver.R;
import com.jaivin.saver.model.DataModel;
import com.jaivin.saver.utils.Constants;
import com.jaivin.saver.utils.Utils;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by DS on 12/12/2017.
 */

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    static boolean flag=true;
    static boolean flag2=false;


    private File file;
    ArrayList<DataModel> mData;
    String type = "image";
    View v;
    LayoutInflater inflater;
    private UnifiedNativeAd nativeAd;
    private FrameLayout frameLayout;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCheckButtonClick(DataModel data, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public StatusAdapter(FragmentActivity activity, ArrayList<DataModel> jData, String type) {
        this.mData = jData;
        this.activity = activity;
        this.type = type;
        MobileAds.initialize(activity.getApplicationContext(), Constants.ADS_ADMOB_ADS_ID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_recycler_view,parent,false);
                AdViewHolder view= new AdViewHolder(v);
                return  view;
            case 1:
                return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false), mListener);
            default:
                return  null;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type=getItemViewType(position);
        switch (type) {

            case 1:
                ContentViewHolder viewHolder=(ContentViewHolder) holder;
                DataModel jpast = this.mData.get(position);

                if(jpast.isChecked()){
                    viewHolder.checkButton.setBackgroundResource(R.drawable.icon_check);
                } else {
                    viewHolder.checkButton.setBackgroundResource(R.drawable.roundbtn);
                }

                this.file = new File(jpast.getFilePath());
                if (!this.file.isDirectory()) {
                    if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                        try {
                            Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(viewHolder.imagevi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!Utils.getBack(jpast.getFilePath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
                        viewHolder.imagePlayer.setVisibility(View.GONE);
                    } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                        viewHolder.imagePlayer.setVisibility(View.GONE);
                        Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(viewHolder.imagevi);
                    }
                }
                break;
            case 0:
                AdViewHolder viewHolder1=(AdViewHolder)holder;
                inflater=(LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v=inflater.inflate(R.layout.ad_recycler_view,null);
                frameLayout=viewHolder1.layout;
                refreshAd();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==4)
        {
            return 0;
        }
        else if((position+1)%5==0&&position>=9){
            return 0;
        }
        else {
           return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{

        FrameLayout layout;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.adView);
        }
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private ImageView imagePlayer;
        private ImageView imagevi;
        RelativeLayout checkButton;


        public ContentViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.imagevi = ((ImageView) itemView.findViewById(R.id.imageView));
            this.imagePlayer = ((ImageView) itemView.findViewById(R.id.iconplayer));
            this.cardView = ((CardView) itemView.findViewById(R.id.card_view));
            this.checkButton = itemView.findViewById(R.id.check_button);
            this.imagePlayer.setOnClickListener(this);
            this.imagevi.setOnClickListener(this);

            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCheckButtonClick(mData.get(position), position);

                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, FullscreenActivity.class);
            intent.putParcelableArrayListExtra("images", mData);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("type", type);
            intent.putExtra("statusdownload", "status");
            activity.startActivity(intent);
//                Utils.mPlayer(vi.getFilePath(), StatusAdapter.this.activity);
        }

    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd() {

        try {
            AdLoader.Builder builder = new AdLoader.Builder(activity.getApplicationContext(), "ca-app-pub-8941410356205346/4869728316");

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }

            });

            VideoOptions videoOptions = new VideoOptions.Builder().build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
        catch (Exception e){
            Toast.makeText(activity.getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isHeader(int position){

        if(position==4)
        {
            return true;
        }
        else if((position+1)%5==0&&position>=9){
            return true;
        }
        else {
            return false;
        }
    }
}
