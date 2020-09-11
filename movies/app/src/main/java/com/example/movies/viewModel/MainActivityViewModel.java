package com.example.movies.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import com.example.movies.exceptions.NoDataAccessException;
import com.example.movies.models.Action;
import com.example.movies.models.SummaryMovie;
import com.example.movies.repository.MoviesRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final MoviesRepository repository;
    private final MutableLiveData<List<SummaryMovie>> movies = new MutableLiveData<>();
    private final LiveData<List<SummaryMovie>> favorites;
    private final MutableLiveData<Boolean> errorConnection = new MutableLiveData<>();

    private Action current = Action.DEFAULT;

    public MainActivityViewModel(Context context) {
        repository = MoviesRepository.getInstance(context);
        favorites = repository.getFavoriteCollection();
        errorConnection.setValue(false);
    }

    public void loadMovies(Action action) {
        boolean prev_error = errorConnection.getValue();
        if(prev_error) {
            chooseAction(action);
        } else {
            if(current != action) {
                chooseAction(action);
            }
        }
    }

    public void loadCurrent() {
        if(current == Action.DEFAULT) {
            chooseAction(Action.POPULAR);
        }
    }

    private void chooseAction(Action action) {
        try{
            current = action;
            switch (action) {
                case RATED:
                    repository.getTopRated(movies);
                    break;
                case POPULAR:
                    repository.getMostPopular(movies);
                    break;
                default:
                    break;
            }
            errorConnection.setValue(false);
        } catch (NoDataAccessException e ) {
            Log.e("MAINACTIVITY",e.getMessage());
            errorConnection.setValue(true);
        }
    }

    public LiveData<List<SummaryMovie>> getMovies() {
        return movies;
    }
    public LiveData<List<SummaryMovie>> getFavorites() {return favorites;}
    public LiveData<Boolean> hasError() { return errorConnection;}
    public void setAction(Action action) {
        current = action;
    }
    public Action getCurrentAction() { return  current;}
}
