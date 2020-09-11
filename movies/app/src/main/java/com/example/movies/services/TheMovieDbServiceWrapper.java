package com.example.movies.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.movies.models.Movie;
import com.example.movies.models.ResponseMovies;
import com.example.movies.models.ResponseResults;
import com.example.movies.models.ReviewMovie;
import com.example.movies.models.SummaryMovie;
import com.example.movies.models.VideoMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheMovieDbServiceWrapper {
    private final String TAG = TheMovieDbServiceWrapper.class.getSimpleName();

    private final String API_KEY = "";

    private final TheMovieDbService theMovieDbService;

    public TheMovieDbServiceWrapper() {
        theMovieDbService = TheMoviewDbServiceFactory.create();
    }

    public void getMostPopular(MutableLiveData<List<SummaryMovie>> listLiveData)  {
        Call<ResponseMovies> responseMoviesCall = theMovieDbService.getMostPopular(API_KEY);
        getMovies(listLiveData, responseMoviesCall);
    }

    public void getTopRated(MutableLiveData<List<SummaryMovie>> listLiveData) {
        Call<ResponseMovies> responseMoviesCall = theMovieDbService.getTopRated(API_KEY);
        getMovies(listLiveData, responseMoviesCall);
    }

    public void getMovie(String id,MutableLiveData<Movie> data,boolean favorite) {

        theMovieDbService.getMovie(id,API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.body() != null) {
                    Movie movie = response.body();
                    movie.setFavorite(favorite);
                    data.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                data.setValue(new Movie());
            }
        });
    }

    public void getVideos(String id, MutableLiveData<List<VideoMovie>> data) {
        Call<ResponseResults<VideoMovie>> response = theMovieDbService.getVideosMovie(id,API_KEY);
        getResultsFromCall(data,response);
    }

    public void getReviews(String id, MutableLiveData<List<ReviewMovie>> data) {
        Call<ResponseResults<ReviewMovie>> response = theMovieDbService.getReviewsMovie(id,API_KEY);
        getResultsFromCall(data,response);
    }

    private <T> void getResultsFromCall(MutableLiveData<List<T>> liveData,Call<ResponseResults<T>> response) {
        response.enqueue(new Callback<ResponseResults<T>>() {
            @Override
            public void onResponse(Call<ResponseResults<T>> call, Response<ResponseResults<T>> response) {
                if(response.body() != null) {
                    liveData.setValue(response.body().getResults());
                }
            }
            @Override
            public void onFailure(Call<ResponseResults<T>> call, Throwable t) {
                liveData.setValue(new ArrayList<>());
            }
        });
    }

    private void getMovies(MutableLiveData<List<SummaryMovie>> listLiveData, Call<ResponseMovies> responseMoviesCall) {
        responseMoviesCall.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                Log.d(TAG,call.request().toString());
                listLiveData.postValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Log.e(TAG,"Message: " + t.getMessage());
                Log.e(TAG,call.toString());
                listLiveData.postValue(new ArrayList<>());
            }
        });
    }
}
