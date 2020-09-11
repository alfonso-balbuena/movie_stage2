package com.example.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.models.SummaryMovie;
import com.example.movies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SummaryMoviesAdapter extends RecyclerView.Adapter<SummaryMoviesAdapter.SummaryMoviesAdapterViewHolder> {

    private List<SummaryMovie> movieList;
    private final IClickHandlerAdapter<SummaryMovie> clickHandler;

    public SummaryMoviesAdapter(IClickHandlerAdapter<SummaryMovie> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setMovieList(List<SummaryMovie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SummaryMoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItem = R.layout.movie_rv;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutItem,parent,false);
        return new SummaryMoviesAdapterViewHolder(view,clickHandler, position ->
            movieList.get(position)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryMoviesAdapterViewHolder holder, int position) {
        String poster_path = movieList.get(position).getPoster_path();
        Picasso.get().load(ImageUtils.getImagePath(poster_path)).into(holder.posterIV);
    }

    @Override
    public int getItemCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    public static class SummaryMoviesAdapterViewHolder extends ViewHolderClick<SummaryMovie> {
        public final ImageView posterIV;
        public SummaryMoviesAdapterViewHolder(@NonNull View itemView, @NonNull IClickHandlerAdapter<SummaryMovie> clickHandlerAdapter, @NonNull IModelPosition<SummaryMovie> position) {
            super(itemView, clickHandlerAdapter, position);
            posterIV = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
