package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.movies.adapters.IClickHandlerAdapter;
import com.example.movies.adapters.SummaryMoviesAdapter;
import com.example.movies.databinding.ActivityMainBinding;
import com.example.movies.models.Action;

import com.example.movies.models.SummaryMovie;
import com.example.movies.viewModel.MainActivityViewModel;
import com.example.movies.viewModel.MainActivityViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private final String TAG = "MAIN";
    private SummaryMoviesAdapter moviesAdapter;
    private SummaryMoviesAdapter favoritesAdapter;
    private final String ID = "Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        RecyclerView rv_movies = activityMainBinding.rvMovies;
        rv_movies.setHasFixedSize(true);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager layoutManager = new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false);
            rv_movies.setLayoutManager(layoutManager);
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager layoutManager = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
            rv_movies.setLayoutManager(layoutManager);
        }

        IClickHandlerAdapter<SummaryMovie> clickHandlerAdapter = model -> {
            Intent detailIntent = new Intent(MainActivity.this, MovieDetail.class);
            detailIntent.putExtra(ID,model.getId());
            startActivity(detailIntent);
        };
        moviesAdapter = new SummaryMoviesAdapter(clickHandlerAdapter);
        favoritesAdapter = new SummaryMoviesAdapter(clickHandlerAdapter);
        rv_movies.setAdapter(moviesAdapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,new MainActivityViewModelFactory(this)).get(MainActivityViewModel.class);

        viewModel.hasError().observe(this, error -> {
            if(error) {
                activityMainBinding.pbLoadingmMovies.setVisibility(View.GONE);
                showErrorLayout(true);
            }
        });

        viewModel.getMovies().observe(this, movies -> {
            activityMainBinding.pbLoadingmMovies.setVisibility(View.GONE);
            moviesAdapter.setMovieList(movies);
            showErrorLayout(movies.isEmpty());
        });

        viewModel.getFavorites().observe(this, movies -> {
            if(viewModel.getCurrentAction() == Action.FAVORITES) {
                showErrorLayout(false);
            }
            favoritesAdapter.setMovieList(movies);
            activityMainBinding.pbLoadingmMovies.setVisibility(View.GONE);
        });

        viewModel.loadCurrent();
        setTitleAndAdapterFromAction(viewModel.getCurrentAction());
    }

    private void setTitleAndAdapterFromAction(Action action) {
        switch (action) {
            case FAVORITES:
                setTitle(R.string.favorite_collection);
                activityMainBinding.rvMovies.setAdapter(favoritesAdapter);
                break;
            case POPULAR:
                setTitle(R.string.popular);
                activityMainBinding.rvMovies.setAdapter(moviesAdapter);
                break;
            case RATED:
                setTitle(R.string.rated);
                activityMainBinding.rvMovies.setAdapter(moviesAdapter);
                break;
            default:
                break;
        }
    }

    private void showErrorLayout(boolean isShow) {
        if(isShow) {
            activityMainBinding.rvMovies.setVisibility(View.GONE);
            activityMainBinding.tvMoviesError.setVisibility(View.VISIBLE);
        } else {
            activityMainBinding.rvMovies.setVisibility(View.VISIBLE);
            activityMainBinding.tvMoviesError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    private void clickOption(Action new_action) {
        if(viewModel.getCurrentAction() != new_action) {
            activityMainBinding.rvMovies.setAdapter(moviesAdapter);
            activityMainBinding.pbLoadingmMovies.setVisibility(View.VISIBLE);
            activityMainBinding.tvMoviesError.setVisibility(View.GONE);
            viewModel.loadMovies(new_action);
            //viewModel.setAction(new_action);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                setTitle(R.string.popular);
                clickOption(Action.POPULAR);
                return true;
            case R.id.action_sort_rated:
                setTitle(R.string.rated);
                clickOption(Action.RATED);
                return true;
            case R.id.action_favorite_collection:
                setTitle(getString(R.string.favorite_collection));
                showErrorLayout(false);
                activityMainBinding.rvMovies.setAdapter(favoritesAdapter);
                viewModel.setAction(Action.FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}