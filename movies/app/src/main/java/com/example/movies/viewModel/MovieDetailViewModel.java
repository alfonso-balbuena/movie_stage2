package com.example.movies.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.exceptions.NoDataAccessException;
import com.example.movies.models.Movie;
import com.example.movies.models.ReviewMovie;
import com.example.movies.models.SummaryMovie;
import com.example.movies.models.VideoMovie;
import com.example.movies.repository.MoviesRepository;

import java.util.List;

public class MovieDetailViewModel extends ViewModel {

    private final MutableLiveData<Movie> movie = new MutableLiveData<>();
    private final MutableLiveData<List<VideoMovie>> videos = new MutableLiveData<>();
    private final MutableLiveData<List<ReviewMovie>> reviews = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MutableLiveData<Boolean> error = new MutableLiveData<>();

    public MovieDetailViewModel(String id, Context context) {
        repository = MoviesRepository.getInstance(context);
        load(id);
    }

    public void favorite() {
        if(movie.getValue().getFavorite()) {
            repository.addToFavorite(getSummaryMovie());
        } else {
            repository.removeToFavorte(getSummaryMovie());
        }
    }

    private SummaryMovie getSummaryMovie() {
        SummaryMovie summaryMovie = new SummaryMovie();
        summaryMovie.setId(movie.getValue().getId());
        summaryMovie.setPoster_path(movie.getValue().getPoster_path());
        return summaryMovie;
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public LiveData<List<VideoMovie>> getVideos() { return  videos;}

    public LiveData<List<ReviewMovie>> getReviews() { return  reviews;}

    public void load(String id) {
        try {
            error.setValue(false);
            repository.getMovie(id,movie);
            repository.getVideos(id,videos);
            repository.getReviews(id,reviews);
        } catch (NoDataAccessException e) {
            error.setValue(true);
        }
    }
    public LiveData<Boolean> hasError() { return error;}
}
