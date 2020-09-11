package com.example.movies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movies.models.SummaryMovie;

import java.util.List;

@Dao
public interface SummaryMovieDao {

    @Query("SELECT * FROM summary")
    LiveData<List<SummaryMovie>> getAll();

    @Query("SELECT * FROM summary WHERE id = :idMovie")
    SummaryMovie getMovie(int idMovie);

    @Delete
    void delete(SummaryMovie summaryMovie);

    @Insert
    void insert(SummaryMovie summaryMovie);


}
