package com.example.movies.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final String id;
    private final Context context;
    public MovieDetailViewModelFactory(String id,@NonNull Context context) {
        this.id = id;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(id,context);
    }
}
