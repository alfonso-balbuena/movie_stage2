package com.example.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.models.VideoMovie;

import java.util.List;

public class VideosMovieAdapter extends RecyclerView.Adapter<VideosMovieAdapter.VideosMovieAdapterViewHolder> {

    private List<VideoMovie> videoMovieList;
    private final IClickHandlerAdapter<VideoMovie> clickHandler;

    public VideosMovieAdapter(IClickHandlerAdapter<VideoMovie> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setVideoMovieList(List<VideoMovie> data) {
        videoMovieList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosMovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItem = R.layout.item_video;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutItem,parent,false);
        return new VideosMovieAdapterViewHolder(view,clickHandler, position -> videoMovieList.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull VideosMovieAdapterViewHolder holder, int position) {
        String name = videoMovieList.get(position).getName();
        holder.tvName.setText(name);
    }

    @Override
    public int getItemCount() {
        return (videoMovieList == null) ? 0 : videoMovieList.size();
    }

    public static class VideosMovieAdapterViewHolder extends ViewHolderClick<VideoMovie> {
        private final TextView tvName;

        public VideosMovieAdapterViewHolder(@NonNull View itemView, @NonNull IClickHandlerAdapter<VideoMovie> clickHandlerAdapter, @NonNull IModelPosition<VideoMovie> position) {
            super(itemView, clickHandlerAdapter, position);
            tvName = itemView.findViewById(R.id.tv_name_video);
        }
    }
}
