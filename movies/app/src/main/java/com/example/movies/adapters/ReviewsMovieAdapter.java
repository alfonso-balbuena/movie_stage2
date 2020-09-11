package com.example.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.models.ReviewMovie;

import java.util.List;

public class ReviewsMovieAdapter extends RecyclerView.Adapter<ReviewsMovieAdapter.ReviewsMovieViewHolder> {

    private List<ReviewMovie> reviewMovieList;

    @NonNull
    @Override
    public ReviewsMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItem = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutItem,parent,false);
        return new ReviewsMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsMovieViewHolder holder, int position) {
        holder.tvAuthor.setText(reviewMovieList.get(position).getAuthor());
        holder.tvContent.setText(reviewMovieList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return (reviewMovieList == null) ? 0 : reviewMovieList.size();
    }

    public void setReviewMovieList(List<ReviewMovie> reviewMovieList) {
        this.reviewMovieList = reviewMovieList;
        notifyDataSetChanged();
    }

    public static class ReviewsMovieViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAuthor;
        final TextView tvContent;
        public ReviewsMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author_review);
            tvContent = itemView.findViewById(R.id.tV_content_review);
        }
    }
}
