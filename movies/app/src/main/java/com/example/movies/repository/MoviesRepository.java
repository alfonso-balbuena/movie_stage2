package com.example.movies.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movies.database.AppDataBase;
import com.example.movies.database.SummaryMovieDao;
import com.example.movies.exceptions.NoDataAccessException;
import com.example.movies.models.Movie;
import com.example.movies.models.ReviewMovie;
import com.example.movies.models.SummaryMovie;
import com.example.movies.models.VideoMovie;
import com.example.movies.services.TheMovieDbServiceWrapper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MoviesRepository {

    private final TheMovieDbServiceWrapper theMovieDbServiceWrapper;
    private final SummaryMovieDao summaryMovieDao;
    private final Context context;
    private final Executor executor;

    private static MoviesRepository instance;
    private final static Object LOCK = new Object();
    private final String connectionMessage = "Error: there is not internet connection";
    public static MoviesRepository getInstance(Context context) {
        if(instance == null) {
            synchronized (LOCK) {
                instance = new MoviesRepository(context);
            }
        }
        return instance;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private MoviesRepository(Context context) {
        AppDataBase appDataBase = AppDataBase.getInstance(context);
        summaryMovieDao = appDataBase.summaryMovieDao();
        theMovieDbServiceWrapper = new TheMovieDbServiceWrapper();
        executor = Executors.newSingleThreadExecutor();
        this.context = context.getApplicationContext();
    }

    public void getMostPopular(MutableLiveData<List<SummaryMovie>> listLiveData) throws NoDataAccessException {
        if(!isConnected()) {
            throw new NoDataAccessException(connectionMessage);
        }
        theMovieDbServiceWrapper.getMostPopular(listLiveData);
    }

    public void getTopRated(MutableLiveData<List<SummaryMovie>> listLiveData) throws NoDataAccessException {
        if(!isConnected()) {
            throw new NoDataAccessException(connectionMessage);
        }
        theMovieDbServiceWrapper.getTopRated(listLiveData);
    }

    public void getMovie(String id,MutableLiveData<Movie> data) throws NoDataAccessException {
        if(!isConnected()) {
            throw new NoDataAccessException(connectionMessage);
        }
        executor.execute(() -> {
            SummaryMovie movie = summaryMovieDao.getMovie(Integer.parseInt(id));
            boolean isFavorite = (movie != null);
            theMovieDbServiceWrapper.getMovie(id,data,isFavorite);
        });
    }

    public void getVideos(String id,MutableLiveData<List<VideoMovie>> data) throws NoDataAccessException {
        if(!isConnected()) {
            throw new NoDataAccessException(connectionMessage);
        }
        theMovieDbServiceWrapper.getVideos(id,data);
    }

    public void getReviews(String id,MutableLiveData<List<ReviewMovie>> data) throws NoDataAccessException {
        if(!isConnected()) {
            throw new NoDataAccessException(connectionMessage);
        }
        theMovieDbServiceWrapper.getReviews(id,data);
    }


    public LiveData<List<SummaryMovie>> getFavoriteCollection() {
        return summaryMovieDao.getAll();
    }

    public void addToFavorite(SummaryMovie summaryMovie) {
        executor.execute(() -> summaryMovieDao.insert(summaryMovie));
    }

    public void removeToFavorte(SummaryMovie summaryMovie) {
        executor.execute(() -> summaryMovieDao.delete(summaryMovie));
    }
}
