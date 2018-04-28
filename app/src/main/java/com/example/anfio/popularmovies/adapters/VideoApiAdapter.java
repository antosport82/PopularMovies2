package com.example.anfio.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.models.Video;
import com.example.anfio.popularmovies.utilities.Constants;
import com.squareup.picasso.Picasso;

public class VideoApiAdapter extends RecyclerView.Adapter<VideoApiAdapter.VideoViewHolder>  {

    private Video[] mVideoData;
    private final VideoApiAdapter.VideoApiAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface VideoApiAdapterOnClickHandler {
        void onClick(String key);
    }

    /**
     * Creates a VideoApiAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public VideoApiAdapter(VideoApiAdapter.VideoApiAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new VideoApiAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        String path = Constants.BASE_URL_IMAGE_VIDEO + mVideoData[position].getKey() + Constants.BASE_URL_SUFFIX_VIDEO;
        Picasso.with(holder.imageView.getContext())
                .load(path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mVideoData) {
            return 0;
        } else {
            return mVideoData.length;
        }
    }

    // custom ViewHolder that implements OnClickListener
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_video_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String key = mVideoData[adapterPosition].getKey();
            mClickHandler.onClick(key);
        }
    }

    public void setVideoData(Video[] videoData) {
        mVideoData = videoData;
        notifyDataSetChanged();
    }
}
