package com.example.movies;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.movies.adapters.ReviewsMovieAdapter;
import com.example.movies.adapters.VideosMovieAdapter;
import com.example.movies.databinding.ActivityMovieDetailBinding;
import com.example.movies.models.Genre;
import com.example.movies.utils.ImageUtils;
import com.example.movies.viewModel.MovieDetailViewModelFactory;
import com.example.movies.viewModel.MovieDetailViewModel;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;


public class MovieDetail extends AppCompatActivity {

    private final String ID = "Id";
    private ActivityMovieDetailBinding activityMovieDetailBinding;
    private MovieDetailViewModel viewModel;
    private VideosMovieAdapter videosMovieAdapter;
    private ReviewsMovieAdapter reviewsMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        View view = activityMovieDetailBinding.getRoot();
        setContentView(view);
        initRecyclerViewVideos();
        initRecyclerViewReviews();
        Intent intent = getIntent();
        if(intent.hasExtra(ID)) {
            Integer id = intent.getIntExtra(ID,0);
            initViewModel(id.toString());
        }
        setTitle(R.string.movie);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initRecyclerViewReviews() {
        RecyclerView rv_reviews = activityMovieDetailBinding.layoutReviews.rvReviews;
        initRecyclerView(rv_reviews);
        reviewsMovieAdapter = new ReviewsMovieAdapter();
        rv_reviews.setAdapter(reviewsMovieAdapter);
    }

    private void initRecyclerViewVideos() {
        RecyclerView rv_movies = activityMovieDetailBinding.layoutMovies.rvVideos;
        initRecyclerView(rv_movies);
        videosMovieAdapter = new VideosMovieAdapter(model -> {
            showVideo(model.getKey());
        });
        rv_movies.setAdapter(videosMovieAdapter);

    }

    private void showVideo(String key) {
        Intent intentYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent intentBrowser = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=" + key));
        try {
            startActivity(intentYoutube);
        } catch (ActivityNotFoundException e) {
            startActivity(intentBrowser);
        }
    }

    private void initViewModel(String id) {
        viewModel = new ViewModelProvider(this,new MovieDetailViewModelFactory(id,this)).get(MovieDetailViewModel.class);
        viewModel.hasError().observe(this, aBoolean -> {
            if(aBoolean) {
                activityMovieDetailBinding.layoutError.setVisibility(View.VISIBLE);
                activityMovieDetailBinding.contentMovie.setVisibility(View.GONE);
            }
        });
        viewModel.getMovie().observe(this, movie -> {
            activityMovieDetailBinding.tvTitleMovie.setText(movie.getOriginal_title());
            if(!movie.isEmpty()) {
                Picasso.get().load(ImageUtils.getImagePath(movie.getPoster_path())).into(activityMovieDetailBinding.ivPoster);
                activityMovieDetailBinding.tvVote.setText(movie.getVote_average().toString());
                for (Genre genre: movie.getGenres()) {
                    Chip chipGenre = new Chip(activityMovieDetailBinding.chipGroupGenres.getContext());
                    chipGenre.setText(genre.getName());
                    activityMovieDetailBinding.chipGroupGenres.addView(chipGenre);
                }
            }
            activityMovieDetailBinding.tvReleasedDate.setText(movie.getRelease_date());
            activityMovieDetailBinding.tvOverview.setText(movie.getOverview());
            activityMovieDetailBinding.tvTitle.setText(movie.getTitle());
            changeImage();
        });
        viewModel.getVideos().observe(this, videoMovie -> {
            videosMovieAdapter.setVideoMovieList(videoMovie);
        });
        viewModel.getReviews().observe(this,reviewMovies -> {
            reviewsMovieAdapter.setReviewMovieList(reviewMovies);
        });
    }

    private void changeImage() {
        if(!viewModel.getMovie().getValue().getFavorite()) {
            activityMovieDetailBinding.ivFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            activityMovieDetailBinding.ivFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }

    public void changeFavorite(View view) {
        boolean status = viewModel.getMovie().getValue().getFavorite();
        viewModel.getMovie().getValue().setFavorite(!status);
        viewModel.favorite();
        changeImage();
    }
}